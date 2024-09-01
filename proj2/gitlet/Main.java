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
                // TODO: handle the `init` command
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                add(Arrays.copyOfRange(args, 1, args.length));
                break;
                // TODO: FILL THE REST IN
            case "commit":
                //TODO:
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
        // TODO write all files in current directory into blob
        // TODO and save them into a Map.
        Map<String, String> filenameBlob = new HashMap<>();
        Commit initCommit = new Commit("initial commit", null, originDate, filenameBlob);

        String initCommitName = Utils.sha1(Utils.serialize(initCommit));

        Repository.writeCommit(initCommitName, initCommit);
        Repository.writeBranch("master", initCommitName);
        Repository.writeCurrentBranch("master");
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
//    private static void testShowMessage() {
//        Utils.message("currentHead: " + currentHead);
//        Utils.message("headCommit: "  + headCommit.show());
//        showWorkingFiles();
//        showStagFiles();
//    }
//    private static void showWorkingFiles() {
//        Utils.message("Working Files:");
//        for (Map.Entry<String, String> entry : workingFiles.entrySet()) {
//            Utils.message(entry.getKey() + ": " + entry.getValue());
//        }
//        Utils.message("------");
//    }
//    private static void showStagFiles() {
//        Utils.message("Stage Files:");
//        for (Map.Entry<String, String> entry : stageFiles.entrySet()) {
//            Utils.message(entry.getKey() + ": " + entry.getValue());
//        }
//        Utils.message("------");
//    }
}
