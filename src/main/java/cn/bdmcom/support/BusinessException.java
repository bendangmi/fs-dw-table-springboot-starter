package cn.bdmcom.support;


/**
 * 业务逻辑异常,全局异常拦截后统一返回ResponseCodeConst.SYSTEM_ERROR
 *
 * @Date 2020/8/25 21:57
 */
public class BusinessException extends RuntimeException {

    /**
     * 创建业务异常。
     */
    public BusinessException() {
    }

    /**
     * 创建业务异常并使用错误码消息。
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    /**
     * 创建业务异常并指定消息。
     *
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 创建业务异常并指定消息与原因。
     *
     * @param message 异常消息
     * @param cause   根因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建业务异常并指定原因。
     *
     * @param cause 根因
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建业务异常并指定完整构造参数。
     *
     * @param message            异常消息
     * @param cause              根因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写堆栈
     */
    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
