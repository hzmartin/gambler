package gambler.examples.webapp2.resp;

public enum ResponseStatus {

    OK("OK", "操作成功"),

    PARAM_ILLEGAL("PARAM_ILLEGAL", "请求参数不合法"),

    USER_NOT_EXSIST("USER_NOT_EXSIST", "用户不存在"),

    USER_NOT_LOGGED("USER_NOT_LOGGED", "用户没有登录"),

    USER_ALREADY_EXSIST("USER_ALREADY_EXSIST", "用户%s已存在"),

    LOGIN_FAILED("LOGIN_FAILED", "用户名或密码错误"),

    NO_PERMISSION("NO_PERMISSION", "权限不足，无法进行该操作"),
    
    EXCEED_MAX_TRYTIMES("EXCEED_MAX_TRYTIMES", "尝试次数过多，请稍后再试"), //尝试次数过多
    
    PASSWORD_ERROR("PASSWORD_ERROR", "密码校验错误"),

    JOB_NOT_EXIST("JOB_NOT_EXIST", "Job(name=%s, group=%s)不存在"),
    
    UNKNOWN_ERROR("UNKNOWN_ERROR", "未知错误"),
    
    SERVER_BUSY("SERVER_BUSY", "网络繁忙，请稍候再试！"),
    
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "服务不可用！");

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
