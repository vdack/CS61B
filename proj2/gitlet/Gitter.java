package gitlet;

import java.util.*;

import static gitlet.Repository.*;


public class Gitter {
    private String currentBranch;
    private Commit currentCommit;
    private List<String> removed;
    private Map<String, String> working;
    private Map<String, String> staged;

    public Gitter() {
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
        List<String> result = getBranches();
        Collections.sort(result);
        return result;
    }
    public List<String> getStagedFiles() {
        List<String> result =  new ArrayList<String>(staged.keySet());
        Collections.sort(result);
        return result;
    }
    public List<String> getRemovedFiles() {
        List<String> result = new ArrayList<>(removed);
        Collections.sort(result);
        return result;
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
        Collections.sort(modifiedFiles);
        return modifiedFiles;
    }
    public List<String> getUntrackedFiles() {

        List<String> tempFiles = new ArrayList<>(currentCommit.getFileNameBlob().keySet());
        tempFiles.addAll(staged.keySet());
        tempFiles.removeAll(removed);
        for(String file : tempFiles) {}
        List<String> untrackedFiles = new ArrayList<>(working.keySet());
        untrackedFiles.removeAll(tempFiles);
        Collections.sort(untrackedFiles);
        return untrackedFiles;
    }
    public List<String> getCommitFiles() {
        return new ArrayList<>(this.currentCommit.getFileNameBlob().keySet());
    }

    public void addFile(String filename) {
        String workingBlob = working.get(filename);
        String commitBlob = currentCommit.getFileNameBlob().get(filename);
        if (workingBlob == null) {
            throw new GitletException("Could not find file " + filename + " in working directory");
        }

        if (removed.contains(filename)) {
            removed.remove(filename);
            writeRemovedFiles(removed);
        }

        if (workingBlob.equals(commitBlob)) {
            if (staged.containsKey(filename)) {
                deleteFile(filename, STAGE_DIR);
            }
        } else {
            stageFile(filename);
//            if (removed.contains(filename)) {
//                removed.remove(filename);
//                writeRemovedFiles(removed);
//            }
        }
//        if (staged.containsKey(filename)) {
//            staged.remove(filename);
//            deleteFile(filename, STAGE_DIR);
//        }

//        if (removed.contains(filename)) {
//            removed.remove(filename);
//            writeRemovedFiles(removed);
//            stageFile(filename);
//        } else {
//            // TODO
//        }
    }

    private void commit(String message, String preCommitId_2) {
        Map<String, String> filesToCommit = new HashMap<>(currentCommit.getFileNameBlob());

        for (String removedFile : removed) {
            filesToCommit.remove(removedFile);
        }
        writeFile(REMOVED_FILES, "");

        for (Map.Entry<String, String> entry : staged.entrySet()) {
            saveFile(entry.getKey());
            filesToCommit.put(entry.getKey(), entry.getValue());
        }

        Commit commit = new Commit(message, readCommitId(currentBranch), preCommitId_2, new Date(), filesToCommit);
        writeCommit(currentBranch, commit);
    }
    public void commit(String message) {
        if (message.isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        if (removed.isEmpty() && staged.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }
        commit(message, null);
    }

    public void rm(String filename) {
        boolean flag = true;
        if (staged.containsKey(filename)) {
            deleteFile(filename, STAGE_DIR);
            flag = false;
        }
        if (currentCommit.getFileNameBlob().containsKey(filename)) {
            removed.add(filename);
            writeRemovedFiles(removed);
            if (working.containsKey(filename)) {
                deleteFile(filename, CWD);
            }
            flag = false;
        }
        if (flag) {
            throw new GitletException("Could not find file " + filename);
        }
    }
    private List<String> getHistoryCommitIds(String branchName) {
        List<String> commitIds = new ArrayList<>();
        String preCommitId = readCommitId(branchName);
        while (preCommitId != null) {
            commitIds.add(preCommitId);
            Commit preCommit = readCommit(preCommitId);
            preCommitId = preCommit.getPreCommitId();
        }
        return commitIds;
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

    private void mergeConflict(String filename, String blobA, String blobB) {
//        Utils.message("merge conflict filename: " + filename);
        String contentA = "";
        if (blobA != null) {
            contentA = readFileAsString(blobA, BLOBS_DIR);
        }
        String contentB = "";
        if(blobB != null) {
            contentB = readFileAsString(blobB, BLOBS_DIR);
        }
        String content = "<<<<<<< HEAD\n" + contentA + "=======\n" + contentB + ">>>>>>>";
//        Utils.message("write content: " + content);
        writeFile(filename, content, STAGE_DIR);
        writeFile(filename, content, CWD);
    }
    public String merge(String branchName) {
        if (!getBranches().contains(branchName)) {
            throw new GitletException("A branch with that name does not exist.");
        }
        if (!(staged.isEmpty()&&removed.isEmpty())) {
            throw new GitletException("You have uncommitted changes.");
        }
        if (!getUntrackedFiles().isEmpty()) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        if (currentBranch.equals(branchName)) {
            throw new GitletException("Cannot merge a branch with itself.");
        }

        String mergedCommitId = readCommitId(branchName);
        String currentCommitId = readCommitId(currentBranch);

        List<String> currentHistory = getHistoryCommitIds(currentBranch);
        Collections.reverse(currentHistory);
        if (currentHistory.contains(mergedCommitId)) {
            throw new GitletException("Given branch is an ancestor of the current branch.");
        }

        List <String> mergedHistory = getHistoryCommitIds(branchName);
        Collections.reverse(mergedHistory);
        if (mergedHistory.contains(currentCommitId)) {
            writeFile(currentBranch, mergedCommitId, BRANCH_DIR);
            checkoutBranch(branchName);
            return "Current branch fast-forwarded.";
        }

        boolean inConflict = false;
        String spiltId = null;

        for (int i = 0; i < Math.min(currentHistory.size(), mergedHistory.size()); i++) {
            String currentId = currentHistory.get(i);
            String mergedId = mergedHistory.get(i);
            if (!mergedId.equals(currentId)) {
                spiltId = currentHistory.get(i-1);
                break;
            }
        }
//        Utils.message("spilt id: " + spiltId);
        if (spiltId == null) {
            throw new GitletException("No common ancestor!");
        }
        Map<String, String> currentFiles = currentCommit.getFileNameBlob();
        Map<String, String> mergedFiles = readCommit(mergedCommitId).getFileNameBlob();
        Map<String, String> spiltFiles = readCommit(spiltId).getFileNameBlob();

        Set<String> possibleFiles = new HashSet<>();
        possibleFiles.addAll(currentFiles.keySet());
        possibleFiles.addAll(mergedFiles.keySet());
        possibleFiles.addAll(spiltFiles.keySet());

        for (Map.Entry<String, String> entry : currentFiles.entrySet()) {
            if (entry.getValue().equals(mergedFiles.get(entry.getKey()))) {

//                Utils.message("Common files:" + entry.getKey());

                possibleFiles.remove(entry.getKey());
            }
        }

        //      files in possibleFiles are
        //      either not in currentFiles
        //      or in currentFiles but not equal to mergedFiles

        for (String filename : possibleFiles) {

            String currentBlob = currentFiles.get(filename);
            String mergedBlob = mergedFiles.get(filename);
            String spiltBlob = spiltFiles.get(filename);

//            Utils.message("spilt blob: " + spiltBlob);
//            Utils.message("current blob: " + currentBlob);
//            Utils.message("merge blob: " + mergedBlob);

            if (currentBlob == null) {
                if (mergedBlob == null || mergedBlob.equals(spiltBlob)) {
                    continue;
                }
                if (spiltBlob == null) {
                    writeFile(filename, readBlob(mergedBlob), STAGE_DIR);
                    writeFile(filename, readBlob(mergedBlob), CWD);
                } else {
                    mergeConflict(filename, null, mergedBlob);
                    inConflict = true;
                }
            } else {
                if (spiltBlob == null) {
                    if (mergedBlob != null) {
                        mergeConflict(filename, currentBlob, mergedBlob);
                        inConflict = true;
                    }
                    continue;
                }
                if (spiltBlob.equals(currentBlob)) {
                    if (mergedBlob != null) {
                        writeFile(filename, readBlob(mergedBlob), STAGE_DIR);
                        writeFile(filename, readBlob(mergedBlob), CWD);
                    } else {
                        removed.add(filename);
                        deleteFile(filename, CWD);
                    }
                } else {
                    if (! spiltBlob.equals(mergedBlob)) {
                        mergeConflict(filename, currentBlob, mergedBlob);
                        inConflict = true;
                    }
                }
            }
        }

        staged = readPlainFiles(STAGE_DIR);
        commit("Merged " + branchName +" into " + currentBranch + ".", mergedCommitId);

        if (inConflict) {
            return "Encountered a merge conflict.";
        } else {
            return  "";
        }
    }
}
