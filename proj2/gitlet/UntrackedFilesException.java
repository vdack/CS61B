package gitlet;

class UntrackedFilesException extends GitletException {
    UntrackedFilesException() {
        super("There is an untracked file in the way; delete it, or add and commit it first.");
    }
}
