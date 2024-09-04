package gitlet;

import java.util.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
//    private static String currentHead;
//    private static Commit headCommit;
//    private static Map<String, String> workingFiles;
//    private static Map<String, String> stageFiles;

    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            return;
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                init();
                break;
            case "add":
                add(Arrays.copyOfRange(args, 1, args.length));
                break;
            case "commit":
                commit(args);
                break;
            case "rm":
                rm(args);
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break;
            case "find":
                find(args);
                break;
            case "branch":
                branch(args);
                break;
            case "rm-branch":
                rmBranch(args);
                break;
            case "checkout":
                checkout(args);
                break;
            case "reset":
                reset(args);
                break;
            case "merge":
                merge(args);
                break;
            case "status":
                status();
                break;
            default:
                Utils.message("No command with that name exists.");
                break;
        }
    }
    private static void init () {
        if (Repository.gitletExists()) {
            Utils.message("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        Repository.createDirectory();

        Date originDate = new Date(0);
        Map<String, String> filenameBlob = new HashMap<>();
        Commit initCommit = new Commit("initial commit", null, null, originDate, filenameBlob);
        Repository.writeCommit("master", initCommit);
    }

    private static void verifyGitlet() {
        if (!Repository.gitletExists()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    private static void status() {
        Gitter gitter = new Gitter();

        String currentHead = gitter.getCurrentBranch();
        Utils.message("=== Branches ===");
        for (String head : gitter.getBranch()) {
            if (head.equals(currentHead)) {
                Utils.message("*" + head);
            } else {
                Utils.message(head);
            }
        }

        Utils.message("\n=== Staged Files ===");
        for (String fileName : gitter.getStagedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Removed Files ===");
        for (String fileName : gitter.getRemovedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Modifications Not Staged For Commit ===");
        for (String fileName : gitter.getModifiedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Untracked Files ===");
        for (String fileName : gitter.getUntrackedFiles()) {
            Utils.message(fileName);
        }
        Utils.message(" ");
    }

    private static void add(String [] filenames) {
        Gitter gitter = new Gitter();
        for (String filename : filenames) {
            try {
                gitter.addFile(filename);
            } catch (Exception e) {
                Utils.message("File does not exist.");
            }
        }
    }

    private static void commit(String[] args) {
        String message = null;
        try {
            message = args[1];
        } catch (Exception e) {
            Utils.message("Please enter a commit message.");
        }
        Gitter gitter = new Gitter();
        try {
            gitter.commit(message);
//            Utils.message("Commit successful.");
        } catch (Exception err ) {
            Utils.message(err.getMessage());
        }
    }

    private static void rm(String[] args) {
        Gitter gitter = new Gitter();
        try {
            String filename = args[1];
            gitter.rm(filename);
        } catch (Exception err) {
            Utils.message("No reason to remove the file.");
        }
        List<String> stagedFiles = gitter.getStagedFiles();
        List<String> commitFiles = gitter.getCommitFiles();

    }

    private static void log() {
        Gitter gitter = new Gitter();
        String currentCommitId = gitter.getCurrentCommitId();
        List<Commit> commits = gitter.getHistoryCommits();

        for (Commit commit : commits) {
            Utils.message("===");
            Utils.message("commit " + currentCommitId);
            currentCommitId = commit.getPreCommitId();
            Utils.message(commit.toString());
        }
    }
    private static void globalLog() {
        Map<String, Commit> commits = Repository.readCommits();
        for (Map.Entry<String, Commit> entry : commits.entrySet()) {
            Utils.message("===");
            Utils.message("commit " + entry.getKey());
            Utils.message(entry.getValue().toString());
        }
    }

    private static void find(String[] args) {
        List<String> finds = new ArrayList<>();
        try {
          String message = args[1];
          Map<String, Commit> commits = Repository.readCommits();
          for (Map.Entry<String, Commit> entry : commits.entrySet()) {
              if (entry.getValue().getMessage().equals(message)) {
                  finds.add(entry.getKey());
              }
          }
          if (finds.isEmpty()) {
              throw new GitletException("Not Found Anything.");
          }
          for (String find : finds) {
              Utils.message(find);
          }
        } catch (Exception err) {
          Utils.message("Found no commit with that message.");
        }
    }

    private static void branch(String[] args) {
        Gitter gitter = new Gitter();
        try {
            gitter.createBranch(args[1]);
        } catch (Exception err) {
            Utils.message("A branch with that name already exists.");
        }
    }

    private static void rmBranch(String[] args) {
        Gitter gitter = new Gitter();
        try {
            gitter.rmBranch(args[1]);
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }

    private static void checkout(String[] args) {
        int length = args.length;
        Gitter gitter = new Gitter();
        try {
            if (length == 2) {
                gitter.checkoutBranch(args[1]);
            } else if (length == 3) {
                gitter.checkoutFile(args[2]);
            } else if (args.length == 4) {
                // TODO checkout commit id file.
                gitter.checkoutFile(args[1], args[3]);
            } else {
                throw new GitletException("Wrong number of arguments.");
            }
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }

    private static void reset(String[] args) {
        try {
            Gitter gitter = new Gitter();
            gitter.reset(args[1]);
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }

    private static void merge(String[] args) {
//        try {
            Gitter gitter = new Gitter();
            String message = gitter.merge(args[1]);
            Utils.message(message);
//        } catch (Exception err) {
//            Utils.message(err.getMessage());
//        }
    }
}
