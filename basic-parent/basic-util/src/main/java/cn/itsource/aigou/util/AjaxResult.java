package cn.itsource.aigou.util;

public class AjaxResult {

    private boolean success = true;
    private String message;
    private String errorCode;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public AjaxResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public AjaxResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Object getData() {
        return data;
    }

    public AjaxResult setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 成功
     * @return
     */
    public static AjaxResult me(){
        return new AjaxResult();
    }


}
