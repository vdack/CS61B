package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");

    public static final File CURRENT_BRANCH = join(GITLET_DIR, "current_branch");
    public static final File REMOVED_FILES = join(GITLET_DIR, "removed_files");
    /* TODO: fill in the rest of this class. */

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
    private static void writeFile(String filename, Object content, File path) {
        File newFile = join(path, filename);
        writeContents(newFile, content);
    }
    public static void writeBranch(String branchName, String commitName) {
        writeFile(branchName, commitName, BRANCH_DIR);
    }
    public static void writeCommit(String commitName, Commit commit) {
        byte[] commitContent = serialize(commit);
        writeFile(commitName, commitContent, COMMITS_DIR);
    }
    public static void writeCurrentBranch(String headName) {
        writeContents(CURRENT_BRANCH, headName);
    }

    // map files:
    public static Map<String, String> readPlainFiles(File path) {
        Map<String, String> result = new HashMap<>();
        for (String fileName : plainFilenamesIn(path)) {
            File plainFile = join(path, fileName);
            result.put(fileName, sha1(serialize(plainFile)));
        }
        return result;
    }

    // read some instances from files:
    public static String readCurrentBranch() {
        return readContentsAsString(CURRENT_BRANCH);
    }
    public static Commit readCommit(String commitName) {
        File commitFile = join(COMMITS_DIR, commitName);
        return readObject(commitFile, Commit.class);
    }
    public static Commit readBranchCommit(String branchName) {
        String commitName = readContentsAsString(join(BRANCH_DIR, branchName));
        return readCommit(commitName);
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

    // get all plain files in repository:
    public static List<String> getBranches() {
        return plainFilenamesIn(BRANCH_DIR);
    }
}
