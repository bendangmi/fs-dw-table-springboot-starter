package cn.bdmcom.support;

/**
 * 错误码<br>
 * 一共分为三种： 1）系统错误、2）用户级别错误、3）未预期到的错误
 */
public interface ErrorCode {

    /**
     * 系统等级
     */
    String LEVEL_SYSTEM = "system";

    /**
     * 用户等级
     */
    String LEVEL_USER = "user";

    /**
     * 未预期到的等级
     */
    String LEVEL_UNEXPECTED = "unexpected";

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误消息。
     *
     * @return 错误消息
     */
    String getMsg();

    /**
     * 获取错误等级。
     *
     * @return 错误等级
     */
    String getLevel();


}
