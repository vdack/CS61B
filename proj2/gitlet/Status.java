package gitlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        tempFiles.addAll(staged.keySet());
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

    public void addFile(String filename) {
        String workingContent = working.get(filename);
        String commitContent = currentCommit.getFileNameBlob().get(filename);

        if (workingContent == null) {
            throw new GitletException("Could not find file " + filename + " in working directory");
        }

        if (!workingContent.equals(commitContent)) {
            stageFile(filename);
            return;
        }

        if (staged.containsKey(filename)) {
            staged.remove(filename);
            unstageFile(filename);
        }

    }
}
