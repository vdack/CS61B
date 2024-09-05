package gitlet;
import java.util.*;

public class Main {


    public static void main(String[] args) {

        if (args.length == 0) {
            Utils.message("Please enter a command.");
            return;
        }
        if (!Repository.gitletExists()) {
            if (args[0].equals("init")) {
                init();
                return;
            }
            Utils.message("Not in an initialized Gitlet directory.");
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
        Commit initCommit = new Commit("initial commit", null, null, 0,originDate);
        Repository.writeCommit("master", initCommit);
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
            message = "";
        }

        Gitter gitter = new Gitter();
        try {
            gitter.commit(message);
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
    }

    private static void log() {
        Gitter gitter = new Gitter();
        String currentCommitId = gitter.getCurrentCommitId();
        List<Commit> commits = gitter.getHistoryCommits();

        for (Commit commit : commits) {
            Utils.message("===");
            Utils.message("commit " + currentCommitId);
            currentCommitId = commit.getPreId();
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
                if (!args[1].equals("--")) {
                    throw new GitletException("Incorrect operands.");
                }
                gitter.checkoutFile(args[2]);
            } else if (args.length == 4) {
                if (!args[2].equals("--")) {
                    throw new GitletException("Incorrect operands.");
                }
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
        try {
            Gitter gitter = new Gitter();
            String message = gitter.merge(args[1]);
            Utils.message(message);
        } catch (Exception err) {
            Utils.message(err.getMessage());
        }
    }
}
