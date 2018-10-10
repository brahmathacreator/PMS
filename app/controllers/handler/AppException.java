package controllers.handler;

public class AppException extends Exception {

    private final String msgKey;

    public AppException(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgKey() {
        return msgKey;
    }
}
