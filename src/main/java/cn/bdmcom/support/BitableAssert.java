package cn.bdmcom.support;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Map;

/**
 * 飞书多维表格断言工具。
 *
 * <p>统一异常抛出方式，确保错误码与提示信息一致。</p>
 */
public final class BitableAssert {

    private BitableAssert() {
    }

    /**
     * 断言对象不为空。
     *
     * @param object    对象
     * @param errorCode 错误码
     * @param message   错误消息
     * @param params    错误消息参数
     */
    public static void notNull(Object object, ErrorCode errorCode, String message, Object... params) {
        if (object == null) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言字符串不为空。
     *
     * @param text      字符串
     * @param errorCode 错误码
     * @param message   错误消息
     * @param params    错误消息参数
     */
    public static void notBlank(String text, ErrorCode errorCode, String message, Object... params) {
        if (StrUtil.isBlank(text)) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言集合不为空。
     *
     * @param collection 集合
     * @param errorCode  错误码
     * @param message    错误消息
     * @param params     错误消息参数
     */
    public static void notEmpty(Collection<?> collection, ErrorCode errorCode, String message, Object... params) {
        if (collection == null || collection.isEmpty()) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言Map不为空。
     *
     * @param map       Map
     * @param errorCode 错误码
     * @param message   错误消息
     * @param params    错误消息参数
     */
    public static void notEmpty(Map<?, ?> map, ErrorCode errorCode, String message, Object... params) {
        if (map == null || map.isEmpty()) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言表达式为真。
     *
     * @param expression 表达式
     * @param errorCode  错误码
     * @param message    错误消息
     * @param params     错误消息参数
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, String message, Object... params) {
        if (!expression) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言表达式为假。
     *
     * @param expression 表达式
     * @param errorCode  错误码
     * @param message    错误消息
     * @param params     错误消息参数
     */
    public static void isFalse(boolean expression, ErrorCode errorCode, String message, Object... params) {
        if (expression) {
            throw buildException(errorCode, message, params);
        }
    }

    /**
     * 断言失败。
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param params    错误消息参数
     */
    public static void fail(ErrorCode errorCode, String message, Object... params) {
        throw buildException(errorCode, message, params);
    }

    /**
     * 构建异常。
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param params    错误消息参数
     * @return 异常
     */
    private static BitableException buildException(ErrorCode errorCode, String message, Object... params) {
        String formatted = params == null || params.length == 0 ? message : StrUtil.format(message, params);
        return new BitableException(errorCode, formatted);
    }
}
