package gitlet;

import java.text.SimpleDateFormat;
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
                //TODO
                break;
            case "status":
                status();
                break;
            case "show":
//                testShowMessage();
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
        Status status = new Status();

        String currentHead = status.getCurrentBranch();
        Utils.message("=== Branches ===");
        for (String head : status.getBranch()) {
            if (head.equals(currentHead)) {
                Utils.message("*" + head);
            } else {
                Utils.message(head);
            }
        }

        Utils.message("\n=== Staged Files ===");
        for (String fileName : status.getStagedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Removed Files ===");
        for (String fileName : status.getRemovedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Modifications Not Staged For Commit ===");
        for (String fileName : status.getModifiedFiles()) {
            Utils.message(fileName);
        }

        Utils.message("\n=== Untracked Files ===");
        for (String fileName : status.getUntrackedFiles()) {
            Utils.message(fileName);
        }
    }

    private static void add(String [] filenames) {
        Status status = new Status();
        for (String filename : filenames) {
            try {
                status.addFile(filename);
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
        Status status = new Status();
        try {
            status.commit(message);
//            Utils.message("Commit successful.");
        } catch (Exception err ) {
            Utils.message(err.getMessage());
        }
    }

    private static void rm(String[] args) {
        Status status = new Status();
        try {
            String filename = args[1];
            status.rm(filename);
        } catch (Exception err) {
            Utils.message("No reason to remove the file.");
        }
        List<String> stagedFiles = status.getStagedFiles();
        List<String> commitFiles = status.getCommitFiles();

    }

    private static void log() {
        Status status = new Status();
        String currentCommitId = status.getCurrentCommitId();
        List<Commit> commits = status.getHistoryCommits();

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
        Status status = new Status();
        try {
            status.createBranch(args[1]);
        } catch (Exception err) {
            Utils.message("A branch with that name already exists.");
        }
    }

    private static void rmBranch(String[] args) {
        Status status = new Status();
        try {
            status.rmBranch(args[1]);
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }

    private static void checkout(String[] args) {
        int length = args.length;
        Status status = new Status();
        try {
            if (length == 2) {
                status.checkoutBranch(args[1]);
            } else if (length == 3) {
                status.checkoutFile(args[2]);
            } else if (args.length == 4) {
                // TODO checkout commit id file.
                status.checkoutFile(args[1], args[3]);
            } else {
                throw new GitletException("Wrong number of arguments.");
            }
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }

    private static void reset(String[] args) {
        try {
            Status status = new Status();
            status.reset(args[1]);
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }
}
