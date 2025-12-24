package cn.bdmcom.support;

import lombok.Getter;


/**
 * 飞书多维表格异常。
 *
 * <p>在 {@link BusinessException} 基础上补充错误码信息。</p>
 */
@Getter
public class BitableException extends BusinessException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    public BitableException(ErrorCode errorCode, String message) {
        super(buildMessage(errorCode, message));
        this.errorCode = errorCode;
        this.detailMessage = message;
    }

    public BitableException(ErrorCode errorCode, String message, Throwable cause) {
        super(buildMessage(errorCode, message), cause);
        this.errorCode = errorCode;
        this.detailMessage = message;
    }

    /**
     * 错误码（无错误码时返回 0）。
     */
    public int getCode() {
        return errorCode == null ? 0 : errorCode.getCode();
    }

    /**
     * 错误等级（无错误码时返回 null）。
     */
    public String getLevel() {
        return errorCode == null ? null : errorCode.getLevel();
    }

    /**
     * 标准错误信息（无错误码时返回 null）。
     */
    public String getErrorMsg() {
        return errorCode == null ? null : errorCode.getMsg();
    }

    private static String buildMessage(ErrorCode errorCode, String message) {
        if (errorCode == null) {
            return message;
        }
        return "[" + errorCode.getCode() + "]" + message;
    }
}
