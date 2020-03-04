package life.drift.community.exception;

//枚举类型
public enum CustomizeErrorCode implements CustomizeErrorCodeInterface {

    //异常封装

    QUESTION_NOT_FOUND(2001, "您所查找的问题不存在哦，要不换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "请您先选择你所需要回复的问题或评论哦！"),
    NOT_LOGIN(2003, "请您先登陆账号哦！"),
    SYS_ERROR(2004, "服务器冒烟啦，正在紧急维修中，请稍后再来试试吧！"),
    TYPE_PARAM_WRONG(2005, "您的评论类型有误或不存在，请重新输入哦！"),
    COMMENT_NOT_FOUND(2006, "您回复的评论已不存在，要不换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入的内容不能为空哦！"),
    READ_NOTIFICATION_FAIL(2008, "这不是给你的回复哦！"),
    NOTIFICATION_NOT_FOUND(2009, "你的消息不翼而飞了？！"),
    ;

    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
