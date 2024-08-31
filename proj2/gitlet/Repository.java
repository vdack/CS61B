package gitlet;

import java.io.File;
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
    public static final File REPO_DIR = join(GITLET_DIR, "repo");
    public static final File HEADS_DIR = join(GITLET_DIR, "heads");
    /* TODO: fill in the rest of this class. */

    public static void createDirectory() {
        GITLET_DIR.mkdirs();
        COMMITS_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        REPO_DIR.mkdirs();
        HEADS_DIR.mkdirs();
    }
    public static boolean gitletExists() {
        return GITLET_DIR.exists();
    }
    private static void writeFile(String filename, Object content, File path) {
        File newFile = join(path, filename);
        writeContents(newFile, content);
    }
    public static void writeHead(String headName, String commitName) {
        writeFile(headName, commitName, HEADS_DIR);
    }
    public static void writeCommit(String commitName, Commit commit) {
        writeFile(commitName, commit, COMMITS_DIR);
    }
}
