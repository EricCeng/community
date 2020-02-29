package life.drift.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCodeInterface errorCodeInterface) {
        this.code = errorCodeInterface.getCode();
        this.message = errorCodeInterface.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
