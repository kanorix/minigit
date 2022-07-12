package net.kanorix.minigit;

public class MiniGitException extends Exception {

    private String code;

    public MiniGitException(final String code) {
        this.code = code;
    }

    public MiniGitException() {
    }

    public void print() {
        if (code != null) {
            System.err.println(code);
        }
    }
}
