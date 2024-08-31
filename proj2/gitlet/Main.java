package gitlet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
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
                break;
                // TODO: FILL THE REST IN
            case "commit":
                //TODO:
                break;
            case "merge":
                //TODO
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
        



    }
    private static void verifyGitlet() {
        if (!Repository.gitletExists()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

}
