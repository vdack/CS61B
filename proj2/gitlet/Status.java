package gitlet;

import java.util.*;

import static gitlet.Repository.*;


public class Status {
    private String currentBranch;
    private Commit currentCommit;
    private List<String> removed;
    private Map<String, String> working;
    private Map<String, String> staged;

    public Status() {
        currentBranch = readCurrentBranch();
        currentCommit = readBranchCommit(currentBranch);
        working = readPlainFiles(CWD);
        staged = readPlainFiles(STAGE_DIR);
        removed = readRemovedFiles();
//        for (String file : currentCommit.getFileNameBlob().keySet()) {
//            System.out.println("---" + file);
//        }
//        System.out.println(currentCommit.show());
//        System.out.println("------running log------");
    }

    public String getCurrentBranch() {
        return currentBranch;
    }
    public List<String> getBranch() {
        return getBranches();
    }
    public List<String> getStagedFiles() {
        return new ArrayList<String>(staged.keySet());
    }
    public List<String> getRemovedFiles() {
        return removed;
    }
    public List<String> getModifiedFiles() {

        List<String> modifiedFiles = new ArrayList<>();
        List<String> tempFiles = new ArrayList<>(currentCommit.getFileNameBlob().keySet());
//        tempFiles.addAll(staged.keySet());
        for (String file : staged.keySet()) {
            if (!tempFiles.contains(file)) {
                tempFiles.add(file);
            }
        }
        tempFiles.removeAll(removed);

        for(String file : tempFiles) {
            if (!working.containsKey(file)) {
                modifiedFiles.add(file + " (delete)");
                continue;
            }
            String target = currentCommit.getFileNameBlob().get(file);
            if(staged.containsKey(file)) {
                target = staged.get(file);
            }
            String current = working.get(file);
//            System.out.println("for file: " + file);
//            System.out.println("--- target: " + target);
//            System.out.println("--- current: " + current);
            if (!current.equals(target)) {
                modifiedFiles.add(file + " (modified)");
            }
        }
        return modifiedFiles;
    }
    public List<String> getUntrackedFiles() {

        List<String> tempFiles = new ArrayList<>(currentCommit.getFileNameBlob().keySet());
        tempFiles.addAll(staged.keySet());
        tempFiles.removeAll(removed);
        for(String file : tempFiles) {}
        List<String> untrackedFiles = new ArrayList<>(working.keySet());
        untrackedFiles.removeAll(tempFiles);
        return untrackedFiles;
    }
    public List<String> getCommitFiles() {
        return new ArrayList<>(this.currentCommit.getFileNameBlob().keySet());
    }

    public void addFile(String filename) {
        String workingContent = working.get(filename);
        String commitContent = currentCommit.getFileNameBlob().get(filename);
        if (workingContent == null) {
            throw new GitletException("Could not find file " + filename + " in working directory");
        }
        if (!workingContent.equals(commitContent)) {
            stageFile(filename);
            if (removed.contains(filename)) {
                removed.remove(filename);
                writeRemovedFiles(removed);
            }
            return;
        }
        if (staged.containsKey(filename)) {
            staged.remove(filename);
//            unstageFile(filename);
            deleteFile(filename, STAGE_DIR);
        }
    }


    public void commit(String message) {
        if (removed.isEmpty() && staged.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }

        Map<String, String> filesToCommit = new HashMap<>(currentCommit.getFileNameBlob());

        for (String removedFile : removed) {
            filesToCommit.remove(removedFile);
        }
        writeFile(REMOVED_FILES, "");

        for (Map.Entry<String, String> entry : staged.entrySet()) {
            saveFile(entry.getKey());
            filesToCommit.put(entry.getKey(), entry.getValue());
        }

        Commit commit = new Commit(message, readCommitId(currentBranch), null, new Date(), filesToCommit);
        writeCommit(currentBranch, commit);
    }

    public void rm(String filename) {
        boolean flag = true;
        if (staged.containsKey(filename)) {
//            unstageFile(filename);
            deleteFile(filename, STAGE_DIR);
            flag = false;
        }
        if (currentCommit.getFileNameBlob().containsKey(filename)) {
            removed.add(filename);
            writeRemovedFiles(removed);
            flag = false;
        }
        if (flag) {
            throw new GitletException("Could not find file " + filename);
        }
        if (working.containsKey(filename)) {
//            rmWorkFile(filename);
            deleteFile(filename, CWD);
        }
    }

    public List<Commit> getHistoryCommits() {
        List<Commit> commits = new ArrayList<>();
        commits.add(currentCommit);
        String previousCommitId = currentCommit.getPreCommitId();
        while (previousCommitId != null) {
            Commit previousCommit = readCommit(previousCommitId);
            commits.add(previousCommit);
            previousCommitId = previousCommit.getPreCommitId();
        }
        return commits;
    }
    public String getCurrentCommitId() {
        return readCommitId(currentBranch);
    }

    public void createBranch(String branchName) {
        if (getBranches().contains(branchName)) {
            throw new GitletException("Branch " + branchName + " already exists");
        }
        writeFile(branchName, getCurrentCommitId(), BRANCH_DIR);
    }
    public void rmBranch(String branchName) {
        if (!getBranches().contains(branchName)) {
            throw new GitletException("A branch with that name does not exist.");
        }
        if (currentBranch.equals(branchName)) {
            throw new GitletException("Cannot remove the current branch.");
        }
//        rmBranchFile(branchName);
        deleteFile(branchName, BRANCH_DIR);
    }
    private void checkout(String filename, String blobId) {
        byte[] content = readBlob(blobId);
        writeWorking(filename, content);
    }
    private void checkoutFile(Commit commit, String filename) {
        Map<String, String> filesOfCommit = commit.getFileNameBlob();
        if (!filesOfCommit.containsKey(filename)) {
            throw new GitletException("File does not exist in that commit.");
        }
        String blob = filesOfCommit.get(filename);
//        byte[] content = readBlob(blob);
//        writeWorking(filename, content);
        checkout(filename, blob);
    }
    public void checkoutFile(String filename) {
        checkoutFile(currentCommit, filename);
    }
    public void checkoutFile(String commitId, String filename) {
        Commit commit = readCommit(commitId);
        checkoutFile(commit, filename);
    }
    public void checkoutBranch(String branchName) {
        if (!getBranches().contains(branchName)) {
            throw new GitletException("No such branch exists.");
        }
        if (currentBranch.equals(branchName)) {
            throw new GitletException("No need to checkout the current branch.");
        }
        if (!getUntrackedFiles().isEmpty()) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }

        Commit commit = readBranchCommit(branchName);
        Map<String, String> commitFiles = commit.getFileNameBlob();
//        clearDirectory(CWD);

        for (String filename : working.keySet()) {
            if (!commitFiles.containsKey(filename)) {
                deleteFile(filename, CWD);
            }
        }
        clearDirectory(STAGE_DIR);

        for (Map.Entry<String, String> entry : commitFiles.entrySet()) {
//            byte[] content = readBlob(entry.getValue());
//            writeWorking(entry.getKey(), content);
            checkout(entry.getKey(), entry.getValue());
        }

        writeFile(REMOVED_FILES, "");
        writeFile(CURRENT_BRANCH, branchName);
    }

    public void reset(String commitId) {
        Commit commit = readCommit(commitId);
        Map<String, String> commitFiles = commit.getFileNameBlob();
        List<String> untrackedFiles = getUntrackedFiles();
        for (String filename : untrackedFiles) {
            if (commitFiles.containsKey(filename)) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }
        for (String filename : working.keySet()) {
            if (!(untrackedFiles.contains(filename) || commitFiles.containsKey(filename))) {
                deleteFile(filename, CWD);
            }
        }
        for (Map.Entry<String, String> entry : commitFiles.entrySet()) {
            checkout(entry.getKey(), entry.getValue());
        }
        writeFile(REMOVED_FILES, "");
        writeFile(currentBranch, commitId, BRANCH_DIR);

    }
}
