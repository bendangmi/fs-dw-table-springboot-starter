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

    public static void notNull(Object object, ErrorCode errorCode, String message, Object... params) {
        if (object == null) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void notBlank(String text, ErrorCode errorCode, String message, Object... params) {
        if (StrUtil.isBlank(text)) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode errorCode, String message, Object... params) {
        if (collection == null || collection.isEmpty()) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode errorCode, String message, Object... params) {
        if (map == null || map.isEmpty()) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode, String message, Object... params) {
        if (!expression) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void isFalse(boolean expression, ErrorCode errorCode, String message, Object... params) {
        if (expression) {
            throw buildException(errorCode, message, params);
        }
    }

    public static void fail(ErrorCode errorCode, String message, Object... params) {
        throw buildException(errorCode, message, params);
    }

    private static BitableException buildException(ErrorCode errorCode, String message, Object... params) {
        String formatted = params == null || params.length == 0 ? message : StrUtil.format(message, params);
        return new BitableException(errorCode, formatted);
    }
}
