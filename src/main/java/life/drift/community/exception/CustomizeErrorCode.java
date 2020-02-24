package life.drift.community.exception;

//枚举类型
public enum CustomizeErrorCode implements CustomizeErrorCodeInterface {

    //异常封装

    QUESTION_NOT_FOUND("您所查找的问题不存在哦，要不换个试试？");

    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message){
        this.message = message;
    }
}
