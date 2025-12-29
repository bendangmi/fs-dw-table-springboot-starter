package cn.bdmcom.core.helper;


import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.domain.FsDwAppBase;
import cn.bdmcom.core.domain.FsDwTable;
import cn.bdmcom.core.domain.FsDwTableId;
import cn.bdmcom.core.domain.FsDwTableProperty;
import cn.bdmcom.core.domain.TypeEnum;
import cn.bdmcom.core.domain.req.AddFieldReq;
import cn.bdmcom.core.domain.req.CreateTableReq;
import cn.bdmcom.core.domain.req.UpdateFieldReq;
import cn.bdmcom.core.domain.res.AddFieldRes;
import cn.bdmcom.core.domain.res.DeleteFieldRes;
import cn.bdmcom.core.domain.res.TableFieldListRes;
import cn.bdmcom.core.domain.res.UpdateFieldRes;
import cn.bdmcom.core.service.FsDwFieldService;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 飞书多维表格字段辅助类。
 */
public final class FsDwFieldHelper {

    private static volatile FsDwFieldService FIELD_SERVICE;
    private static volatile FsDwProperties PROPERTIES;
    /**
     * 工具类构造器。
     */
    private FsDwFieldHelper() {
    }

    /**
     * 注册字段服务。
     *
     * @param fieldService 字段服务
     */
    public static void registerServices(FsDwFieldService fieldService) {
        FIELD_SERVICE = fieldService;
    }

    /**
     * 注册配置属性。
     *
     * @param properties 配置属性
     */
    public static void registerProperties(FsDwProperties properties) {
        PROPERTIES = properties;
    }

    /**
     * 新增字段（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @param req         新增字段请求体
     * @return 新增字段结果
     */
    public static AddFieldRes createField(Class<?> entityClass, AddFieldReq req) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return createField(requireAppId(), requireAppSecret(), appToken, tableId, null, req);
    }

    /**
     * 新增字段（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @param clientToken 幂等请求标识
     * @param req         新增字段请求体
     * @return 新增字段结果
     */
    public static AddFieldRes createField(Class<?> entityClass, String clientToken, AddFieldReq req) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return createField(requireAppId(), requireAppSecret(), appToken, tableId, clientToken, req);
    }

    /**
     * 新增字段（基于 appId/appSecret/appToken）。
     *
     * @param appId       应用ID
     * @param appSecret   应用密钥
     * @param appToken    多维表格 App 的唯一标识
     * @param tableId     数据表唯一标识
     * @param clientToken 幂等请求标识
     * @param req         新增字段请求体
     * @return 新增字段结果
     */
    public static AddFieldRes createField(String appId, String appSecret, String appToken, String tableId, String clientToken, AddFieldReq req) {
        return requireService().createField(appId, appSecret, appToken, tableId, clientToken, req);
    }

    /**
     * 更新字段（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @param fieldId     字段唯一标识
     * @param req         更新字段请求体
     * @return 更新字段结果
     */
    public static UpdateFieldRes updateField(Class<?> entityClass, String fieldId, UpdateFieldReq req) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return updateField(requireAppId(), requireAppSecret(), appToken, tableId, fieldId, req);
    }

    /**
     * 更新字段（基于 appId/appSecret/appToken）。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param fieldId   字段唯一标识
     * @param req       更新字段请求体
     * @return 更新字段结果
     */
    public static UpdateFieldRes updateField(String appId, String appSecret, String appToken, String tableId, String fieldId, UpdateFieldReq req) {
        return requireService().updateField(appId, appSecret, appToken, tableId, fieldId, req);
    }

    /**
     * 删除字段（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @param fieldId     字段唯一标识
     * @return 删除字段结果
     */
    public static DeleteFieldRes deleteField(Class<?> entityClass, String fieldId) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return deleteField(requireAppId(), requireAppSecret(), appToken, tableId, fieldId);
    }

    /**
     * 删除字段（基于 appId/appSecret/appToken）。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param fieldId   字段唯一标识
     * @return 删除字段结果
     */
    public static DeleteFieldRes deleteField(String appId, String appSecret, String appToken, String tableId, String fieldId) {
        return requireService().deleteField(appId, appSecret, appToken, tableId, fieldId);
    }

    /**
     * 查询数据表字段列表（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @return 字段列表
     */
    public static List<TableFieldListRes.TableField> listFields(Class<?> entityClass) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return listFields(requireAppId(), requireAppSecret(), appToken, tableId, null);
    }

    /**
     * 查询数据表字段列表（基于实体注解解析表ID）。
     *
     * @param entityClass 实体类型
     * @param viewId      视图 ID
     * @return 字段列表
     */
    public static List<TableFieldListRes.TableField> listFields(Class<?> entityClass, String viewId) {
        String tableId = resolveTableId(entityClass);
        String appToken = resolveAppToken(entityClass);
        return listFields(requireAppId(), requireAppSecret(), appToken, tableId, viewId);
    }

    /**
     * 查询数据表字段列表（基于 appId/appSecret/appToken）。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param viewId    视图 ID
     * @return 字段列表
     */
    public static List<TableFieldListRes.TableField> listFields(String appId, String appSecret, String appToken, String tableId, String viewId) {
        return requireService().listFields(appId, appSecret, appToken, tableId, viewId);
    }

    /**
     * 构建新增数据表字段定义（基于实体注解）。
     *
     * @param entityClass 实体类型
     * @return 字段定义列表
     */
    public static List<CreateTableReq.Field> buildCreateTableFields(Class<?> entityClass) {
        BitableAssert.notNull(entityClass, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        List<FieldMeta> metas = resolveFieldMetas(entityClass);
        if (metas.isEmpty()) {
            return Collections.emptyList();
        }
        List<CreateTableReq.Field> fields = new ArrayList<>();
        for (FieldMeta meta : metas) {
            CreateTableReq.Field field = new CreateTableReq.Field();
            field.setFieldName(resolveFieldName(meta.getField(), meta.getProperty()));
            TypeEnum type = meta.getProperty() == null ? TypeEnum.TEXT : meta.getProperty().type();
            field.setType(type == null ? null : type.getType());
            Map<String, Object> property = buildFieldProperty(type, meta.getProperty());
            if (property != null && !property.isEmpty()) {
                field.setProperty(property);
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * 获取字段服务。
     *
     * @return 字段服务
     */
    private static FsDwFieldService requireService() {
        BitableAssert.notNull(FIELD_SERVICE, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]字段服务未注册");
        return FIELD_SERVICE;
    }

    /**
     * 获取配置属性。
     *
     * @return 配置属性
     */
    private static FsDwProperties requireProperties() {
        BitableAssert.notNull(PROPERTIES, BitableErrorCode.TABLE_CONFIG_MISSING, "[飞书多维表格]配置未加载");
        return PROPERTIES;
    }

    /**
     * 获取应用ID。
     *
     * @return 应用ID
     */
    private static String requireAppId() {
        String appId = requireProperties().getAppId();
        BitableAssert.notBlank(appId, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appId未配置");
        return appId;
    }

    /**
     * 获取应用密钥。
     *
     * @return 应用密钥
     */
    private static String requireAppSecret() {
        String appSecret = requireProperties().getAppSecret();
        BitableAssert.notBlank(appSecret, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appSecret未配置");
        return appSecret;
    }

    /**
     * 解析应用令牌（优先使用实体父类上的 @FsDwAppBase）。
     *
     * @param entityClass 实体类型
     * @return 应用令牌
     */
    private static String resolveAppToken(Class<?> entityClass) {
        BitableAssert.notNull(entityClass, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        FsDwAppBase appBase = entityClass.getAnnotation(FsDwAppBase.class);
        String appToken = appBase == null ? null : appBase.appToken();
        BitableAssert.notBlank(appToken, BitableErrorCode.APP_TOKEN_MISSING,
                "[飞书多维表格]appToken未配置，请在父类添加@FsDwAppBase或显式传入appToken");
        return appToken.trim();
    }

    /**
     * 解析数据表ID。
     *
     * @param entityClass 实体类型
     * @return 数据表ID
     */
    private static String resolveTableId(Class<?> entityClass) {
        BitableAssert.notNull(entityClass, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        FsDwTable table = entityClass.getAnnotation(FsDwTable.class);
        BitableAssert.notNull(table, BitableErrorCode.TABLE_META_MISSING, "[飞书多维表格]实体未标注@FsDwTable");
        BitableAssert.notBlank(table.tableId(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId未配置");
        return table.tableId();
    }

    /**
     * 解析字段元信息列表。
     *
     * @param entityClass 实体类型
     * @return 字段元信息列表
     */
    private static List<FieldMeta> resolveFieldMetas(Class<?> entityClass) {
        List<Field> fields = getAllFields(entityClass);
        if (fields.isEmpty()) {
            return Collections.emptyList();
        }
        boolean hasTableProperty = fields.stream().anyMatch(field -> field.getAnnotation(FsDwTableProperty.class) != null);
        List<FieldMeta> metas = new ArrayList<>();
        int index = 0;
        for (Field field : fields) {
            FsDwTableProperty property = field.getAnnotation(FsDwTableProperty.class);
            if (isTableIdField(field)) {
                index++;
                continue;
            }
            if (hasTableProperty && property == null) {
                index++;
                continue;
            }
            int order = property == null ? Integer.MAX_VALUE : property.order();
            metas.add(new FieldMeta(field, property, order, index));
            index++;
        }
        metas.sort(Comparator.comparingInt(FieldMeta::getOrder).thenComparingInt(FieldMeta::getIndex));
        return metas;
    }

    /**
     * 获取实体所有非静态字段。
     *
     * @param type 实体类型
     * @return 字段列表
     */
    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            Field[] declaredFields = current.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * 判断字段是否为表ID字段。
     *
     * @param field 字段
     * @return 是否为表ID字段
     */
    private static boolean isTableIdField(Field field) {
        return field.getAnnotation(FsDwTableId.class) != null;
    }

    /**
     * 解析字段名称。
     *
     * @param field    字段
     * @param property 字段注解
     * @return 字段名称
     */
    private static String resolveFieldName(Field field, FsDwTableProperty property) {
        if (property != null) {
            if (!isBlank(property.field())) {
                return property.field().trim();
            }
            String[] values = property.value();
            if (values != null && values.length > 0 && !isBlank(values[0])) {
                return values[0].trim();
            }
        }
        return field.getName();
    }

    /**
     * 构建字段属性配置。
     *
     * @param type     字段类型
     * @param property 字段注解
     * @return 字段属性配置
     */
    private static Map<String, Object> buildFieldProperty(TypeEnum type, FsDwTableProperty property) {
        if (type == null || property == null) {
            return new HashMap<>();
        }
        if (type != TypeEnum.SINGLE_SELECT && type != TypeEnum.MULTI_SELECT) {
            return new HashMap<>();
        }
        String[] options = property.options();
        if (options == null || options.length == 0) {
            return new HashMap<>();
        }
        List<Map<String, Object>> optionList = new ArrayList<>();
        for (String option : options) {
            if (isBlank(option)) {
                continue;
            }
            Map<String, Object> optionMap = new LinkedHashMap<>();
            optionMap.put("name", option.trim());
            optionList.add(optionMap);
        }
        if (optionList.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Object> propertyMap = new LinkedHashMap<>();
        propertyMap.put("options", optionList);
        return propertyMap;
    }

    /**
     * 判断字符串是否为空。
     *
     * @param value 字符串
     * @return 是否为空
     */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 通过字段名更新字段
     *
     * @param tableClass     测试表实体类
     * @param oldField       旧字段名
     * @param updateFieldReq 更新字段请求
     * @return 更新字段结果
     */
    public static UpdateFieldRes updateFieldByName(Class<?> tableClass, String oldField, UpdateFieldReq updateFieldReq) {
        // 列出所有字段
        List<TableFieldListRes.TableField> fields = FsDwFieldHelper.listFields(tableClass);
        for (TableFieldListRes.TableField field : fields) {
            if (field.getFieldName().equals(oldField)) {
                return FsDwFieldHelper.updateField(tableClass, field.getFieldId(), updateFieldReq);
            }
        }
        throw new RuntimeException("字段不存在");
    }

    /**
     * 通过字段名删除字段
     *
     * @param tableClass  测试表实体类
     * @param deleteField 旧字段名
     * @return 删除字段结果
     */
    public static DeleteFieldRes deleteFieldByName(Class<?> tableClass, String deleteField) {
        // 列出所有字段
        List<TableFieldListRes.TableField> fields = FsDwFieldHelper.listFields(tableClass);
        for (TableFieldListRes.TableField field : fields) {
            if (field.getFieldName().equals(deleteField)) {
                return FsDwFieldHelper.deleteField(tableClass, field.getFieldId());
            }
        }
        throw new RuntimeException("字段不存在");
    }

    /**
     * 字段元信息。
     */
    private static final class FieldMeta {
        private final Field field;
        private final FsDwTableProperty property;
        private final int order;
        private final int index;

        /**
         * 构造字段元信息。
         *
         * @param field    字段
         * @param property 字段注解
         * @param order    排序值
         * @param index    字段索引
         */
        private FieldMeta(Field field, FsDwTableProperty property, int order, int index) {
            this.field = field;
            this.property = property;
            this.order = order;
            this.index = index;
        }

        /**
         * 获取字段。
         *
         * @return 字段
         */
        private Field getField() {
            return field;
        }

        /**
         * 获取字段注解。
         *
         * @return 字段注解
         */
        private FsDwTableProperty getProperty() {
            return property;
        }

        /**
         * 获取排序值。
         *
         * @return 排序值
         */
        private int getOrder() {
            return order;
        }

        /**
         * 获取字段索引。
         *
         * @return 字段索引
         */
        private int getIndex() {
            return index;
        }
    }
}
