package gitlet;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    final private String message;

    final private String preCommitName;

    final private Date date;

    final private Map<String, String> fileNameBlob;

    public Commit (String message, String preCommitName,Date date, Map<String, String> fileNameBlob) {
        this.message = message;
        this.preCommitName = preCommitName;
        this.date = date;
        this.fileNameBlob = new HashMap<String, String>(fileNameBlob);
    }

    public String getMessage(){
        return message;
    }

    public Date getDate() {
        return date;
    }

    /* TODO: fill in the rest of this class. */
}
