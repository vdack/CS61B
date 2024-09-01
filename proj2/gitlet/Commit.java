package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    final private String message;

    final private String preCommitId;

    final private Date date;

    final private Map<String, String> fileNameBlob;

    public Commit (String message, String preCommitName,Date date, Map<String, String> fileNameBlob) {
        this.message = message;
        this.preCommitId = preCommitName;
        this.date = date;
        this.fileNameBlob = new HashMap<String, String>(fileNameBlob);
    }

    public String getMessage(){
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getPreCommitId() {return preCommitId;}

    public Map<String, String> getFileNameBlob() {
        return fileNameBlob;
    }
    /* TODO: fill in the rest of this class. */


    public String show() {
        StringBuilder result = new StringBuilder();
        result.append("message: " + message + "\n");
        result.append("preCommitId: " + preCommitId + "\n");
        result.append("date: " + date + "\n");
        result.append("------\nfile name and blobs: \n");
        for (Map.Entry<String, String> entry : fileNameBlob.entrySet()) {
            result.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        result.append("+---------------+\n");
        return result.toString();
    }
}
