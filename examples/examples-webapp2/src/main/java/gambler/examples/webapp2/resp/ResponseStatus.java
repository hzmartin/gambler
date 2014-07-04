package gambler.examples.webapp2.resp;

public enum ResponseStatus {

    OK("0000", "操作成功"),

    PARAM_ILLEGAL("0001", "请求参数不合法"),

    USER_NOT_EXSIST("0002", "用户不存在"),

    USER_NOT_LOGGED("0003", "用户没有登录"),

    USER_ALREADY_EXSIST("0004", "用户%s已存在"),

    LOGIN_FAILED("0005", "用户名或密码错误"),

    NO_PERMISSION("0006", "权限不足，无法进行该操作"),
    
    EXCEED_MAX_TRYTIMES("0007", "尝试次数过多，请稍后再试"), //尝试次数过多
    
    PASSWORD_ERROR("0008", "密码校验错误"),

    UNKNOWN_ERROR("4004", "未知错误"),
    
    SERVER_BUSY("5000", "网络繁忙，请稍候再试！");

    private String code;

    private String message;

    private ResponseStatus(String code) {
        this.code = code;
    }

    private ResponseStatus(String code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", code, message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
