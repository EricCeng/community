package life.drift.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(CustomizeErrorCodeInterface errorCodeInterface) {
        this.message = errorCodeInterface.getMessage();
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
