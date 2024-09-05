package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commit implements Serializable {

    final private String message;
    final private String preCommitId;
    final private String preSubCommitId;
    final private int depth;
    final private Date date;
    final private Map<String, String> fileNameBlob;

    public Commit (String message, String preCommitId, String preSubCommitId, int depth, Date date, Map<String, String> fileNameBlob) {
        this.message = message;
        this.preCommitId = preCommitId;
        this.preSubCommitId = preSubCommitId;
        this.date = date;
        this.depth = depth;
        this.fileNameBlob = new HashMap<String, String>(fileNameBlob);
    }

    public String getMessage(){
        return message;
    }

    public Date getDate() {
        return date;
    }

    public int getDepth() {
        return depth;
    }

    public String getPreCommitId() {return preCommitId;}
    public String getPreSubCommitId() {return preSubCommitId;}
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
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (preSubCommitId != null) {
            result.append("Merge: ");
            result.append(preCommitId.substring(0, 7));
            result.append(" ");
            result.append(preSubCommitId.substring(0, 7));
            result.append("\n");

        }
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-8"));
        String formattedDate = sdf.format(this.date);
        result.append("Date: " + formattedDate + "\n");
        result.append(message+"\n");
        return result.toString();
    }
}
