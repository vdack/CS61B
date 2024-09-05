package gitlet;

import java.io.File;
import java.util.*;
import static gitlet.Utils.*;

public class Repository {

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");

    public static final File CURRENT_BRANCH = join(GITLET_DIR, "current_branch");
    public static final File REMOVED_FILES = join(GITLET_DIR, "removed_files");

    public static void createDirectory() {
        GITLET_DIR.mkdirs();
        COMMITS_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        STAGE_DIR.mkdirs();
        BRANCH_DIR.mkdirs();
        writeContents(REMOVED_FILES, "");
        writeContents(CURRENT_BRANCH, "");
    }
    public static boolean gitletExists() {
        return GITLET_DIR.exists();
    }

    // write files:
    public static void writeFile(File file, Object content) {
        writeContents(file, content);
    }
    public static void writeFile(String filename, Object content, File path) {
        File newFile = join(path, filename);
        writeContents(newFile, content);
    }

    public static void deleteFile(File path) {
        if (path.exists()) {
            path.delete();
        }
    }
    public static void deleteFile(String filename, File parentPath) {
        deleteFile(join(parentPath, filename));
    }

    public static void clearDirectory(File dir) {
        if (!dir.exists()) {
            throw new GitletException("Directory does not exist: " + dir);
        }
        if (!dir.isDirectory()) {
            throw new GitletException("Not a directory: " + dir);
        }
        for (String filename : plainFilenamesIn(dir)) {
            deleteFile(join(dir, filename));
        }
    }
    // map files:
    public static Map<String, String> readPlainFiles(File path) {
        Map<String, String> result = new HashMap<>();
        for (String fileName : plainFilenamesIn(path)) {
            File plainFile = join(path, fileName);
            byte[] content = readContents(plainFile);
            result.put(fileName, sha1(content));
        }
        return result;
    }

    // read some instances from files:
    public static String readCurrentBranch() {
        return readContentsAsString(CURRENT_BRANCH);
    }
    public static Commit readCommit(String commitId) {
        try {
            if (commitId.length() < 40) {
                for (String filename : plainFilenamesIn(COMMITS_DIR)) {
                    if (filename.substring(0, commitId.length()).equals(commitId)) {
                        commitId = filename;
                        break;
                    }
                }
            }
            File commitFile = join(COMMITS_DIR, commitId);
            return readObject(commitFile, Commit.class);
        } catch (Exception err) {
            throw new GitletException("No commit with that id exists.");
        }
    }
    public static Map<String, Commit> readCommits() {
        Map<String, Commit> result = new HashMap<>();
        for (String filename : plainFilenamesIn(COMMITS_DIR)) {
            File commitFile = join(COMMITS_DIR, filename);
            result.put(filename, readObject(commitFile, Commit.class));
        }
        return result;
    }
    public static Commit readBranchCommit(String branchName) {
        String commitId = readContentsAsString(join(BRANCH_DIR, branchName));
        return readCommit(commitId);
    }
    public static String readCommitId(String branchName) {
        return readContentsAsString(join(BRANCH_DIR, branchName));
    }
    public static List<String> readRemovedFiles() {
        String string = readContentsAsString(REMOVED_FILES);
        if (string.equals("")) {
            return new ArrayList<>();
        }
        String[] lines = string.split("\n");
        List<String> result = new ArrayList<>(Arrays.asList(lines));
        return result;
    }
    public static byte[] readBlob(String blobId) {
        return readContents(join(BLOBS_DIR, blobId));
    }
    public static String readFileAsString(String filename, File dir) {
        return readContentsAsString(join(dir, filename));
    }
    // get all plain files in repository:
    public static List<String> getBranches() {
        return plainFilenamesIn(BRANCH_DIR);
    }

    public static void stageFile(String filename) {
        byte[] content = readContents(join(CWD, filename));
        writeFile(filename, content, STAGE_DIR);
    }
    public static void saveFile(String filename) {
        File file = join(STAGE_DIR, filename);
        byte[] content = readContents(file);
        writeFile(sha1(content), content, BLOBS_DIR);
        deleteFile(file);
    }

    public static void writeCommit(String branchName, Commit commit) {
        byte[] commitContent = serialize(commit);
        String commitId = sha1(commitContent);
        writeFile(commitId, commitContent, COMMITS_DIR);
        writeFile(branchName, commitId, BRANCH_DIR);
        writeContents(CURRENT_BRANCH, branchName);

    }
    public static void writeRemovedFiles(List<String> removedFiles) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String fileName : removedFiles) {
            stringBuilder.append(fileName + "\n");
        }
        writeFile(REMOVED_FILES, stringBuilder.toString());
    }
    public static void writeWorking(String filename, byte[] content) {
        writeFile(filename, content, CWD);
    }
}
