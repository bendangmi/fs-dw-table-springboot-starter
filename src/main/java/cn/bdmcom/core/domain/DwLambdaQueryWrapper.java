package cn.bdmcom.core.domain;

import cn.bdmcom.core.domain.req.QueryRecordReq;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Bitable Lambda 查询构建器（MyBatis Plus 风格）。
 *
 * <p>构建 QueryRecordReq 的筛选/排序/页面参数.</p>
 */
public final class DwLambdaQueryWrapper<T> {

    private static final String CONJUNCTION_AND = "and";
    private static final String CONJUNCTION_OR = "or";

    private final Class<T> entityClass;
    private final List<ConditionGroup> groups = new ArrayList<>();
    private final List<QueryRecordReq.Sort> sorts = new ArrayList<>();
    private final List<String> selectFields = new ArrayList<>();
    private String viewId;
    private Integer pageNo;
    private Integer pageSize;
    private String pageToken;
    private Boolean automaticFields;
    private ConditionGroup currentGroup;
    private Map<String, String> fieldNameMap;

    public DwLambdaQueryWrapper() {
        this(null);
    }

    public DwLambdaQueryWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.currentGroup = new ConditionGroup();
        this.groups.add(this.currentGroup);
    }

    public DwLambdaQueryWrapper<T> viewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public DwLambdaQueryWrapper<T> pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public DwLambdaQueryWrapper<T> pageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public DwLambdaQueryWrapper<T> pageToken(String pageToken) {
        this.pageToken = pageToken;
        return this;
    }

    public DwLambdaQueryWrapper<T> automaticFields(Boolean automaticFields) {
        this.automaticFields = automaticFields;
        return this;
    }

    public DwLambdaQueryWrapper<T> select(String... fields) {
        if (fields == null) {
            return this;
        }
        for (String field : fields) {
            if (StrUtil.isBlank(field)) {
                continue;
            }
            this.selectFields.add(resolveFieldName(field.trim()));
        }
        return this;
    }

    @SafeVarargs
    public final DwLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (columns == null) {
            return this;
        }
        for (SFunction<T, ?> column : columns) {
            String fieldName = resolveFieldName(column);
            if (StrUtil.isNotBlank(fieldName)) {
                this.selectFields.add(fieldName);
            }
        }
        return this;
    }

    public DwLambdaQueryWrapper<T> or() {
        if (this.currentGroup != null && this.currentGroup.hasConditions()) {
            this.currentGroup = new ConditionGroup();
            this.groups.add(this.currentGroup);
        }
        return this;
    }

    public DwLambdaQueryWrapper<T> and() {
        return this;
    }

    public DwLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object value) {
        return eq(true, column, value);
    }

    public DwLambdaQueryWrapper<T> eq(String fieldName, Object value) {
        return eq(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object value) {
        return eq(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> eq(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "is", value);
    }

    public DwLambdaQueryWrapper<T> ne(SFunction<T, ?> column, Object value) {
        return ne(true, column, value);
    }

    public DwLambdaQueryWrapper<T> ne(String fieldName, Object value) {
        return ne(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> ne(boolean condition, SFunction<T, ?> column, Object value) {
        return ne(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> ne(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isNot", value);
    }

    public DwLambdaQueryWrapper<T> like(SFunction<T, ?> column, Object value) {
        return like(true, column, value);
    }

    public DwLambdaQueryWrapper<T> like(String fieldName, Object value) {
        return like(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> like(boolean condition, SFunction<T, ?> column, Object value) {
        return like(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> like(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "contains", value);
    }

    public DwLambdaQueryWrapper<T> notLike(SFunction<T, ?> column, Object value) {
        return notLike(true, column, value);
    }

    public DwLambdaQueryWrapper<T> notLike(String fieldName, Object value) {
        return notLike(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> notLike(boolean condition, SFunction<T, ?> column, Object value) {
        return notLike(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> notLike(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "doesNotContain", value);
    }

    public DwLambdaQueryWrapper<T> gt(SFunction<T, ?> column, Object value) {
        return gt(true, column, value);
    }

    public DwLambdaQueryWrapper<T> gt(String fieldName, Object value) {
        return gt(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object value) {
        return gt(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> gt(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isGreater", value);
    }

    public DwLambdaQueryWrapper<T> ge(SFunction<T, ?> column, Object value) {
        return ge(true, column, value);
    }

    public DwLambdaQueryWrapper<T> ge(String fieldName, Object value) {
        return ge(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object value) {
        return ge(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> ge(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isGreaterEqual", value);
    }

    public DwLambdaQueryWrapper<T> lt(SFunction<T, ?> column, Object value) {
        return lt(true, column, value);
    }

    public DwLambdaQueryWrapper<T> lt(String fieldName, Object value) {
        return lt(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object value) {
        return lt(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> lt(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isLess", value);
    }

    public DwLambdaQueryWrapper<T> le(SFunction<T, ?> column, Object value) {
        return le(true, column, value);
    }

    public DwLambdaQueryWrapper<T> le(String fieldName, Object value) {
        return le(true, fieldName, value);
    }

    public DwLambdaQueryWrapper<T> le(boolean condition, SFunction<T, ?> column, Object value) {
        return le(condition, resolveFieldName(column), value);
    }

    public DwLambdaQueryWrapper<T> le(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isLessEqual", value);
    }

    public DwLambdaQueryWrapper<T> in(SFunction<T, ?> column, Object... values) {
        return in(true, column, values);
    }

    public DwLambdaQueryWrapper<T> in(String fieldName, Object... values) {
        return in(true, fieldName, values);
    }

    public DwLambdaQueryWrapper<T> in(boolean condition, SFunction<T, ?> column, Object... values) {
        return in(condition, resolveFieldName(column), values);
    }

    public DwLambdaQueryWrapper<T> in(boolean condition, String fieldName, Object... values) {
        return addCondition(condition, fieldName, "in", values == null ? null : Arrays.asList(values));
    }

    public DwLambdaQueryWrapper<T> isNull(SFunction<T, ?> column) {
        return isNull(true, column);
    }

    public DwLambdaQueryWrapper<T> isNull(String fieldName) {
        return isNull(true, fieldName);
    }

    public DwLambdaQueryWrapper<T> isNull(boolean condition, SFunction<T, ?> column) {
        return isNull(condition, resolveFieldName(column));
    }

    public DwLambdaQueryWrapper<T> isNull(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, "isEmpty", Collections.emptyList());
    }

    public DwLambdaQueryWrapper<T> isNotNull(SFunction<T, ?> column) {
        return isNotNull(true, column);
    }

    public DwLambdaQueryWrapper<T> isNotNull(String fieldName) {
        return isNotNull(true, fieldName);
    }

    public DwLambdaQueryWrapper<T> isNotNull(boolean condition, SFunction<T, ?> column) {
        return isNotNull(condition, resolveFieldName(column));
    }

    public DwLambdaQueryWrapper<T> isNotNull(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, "isNotEmpty", Collections.emptyList());
    }

    public DwLambdaQueryWrapper<T> between(SFunction<T, ?> column, Object start, Object end) {
        return between(true, column, start, end);
    }

    public DwLambdaQueryWrapper<T> between(String fieldName, Object start, Object end) {
        return between(true, fieldName, start, end);
    }

    public DwLambdaQueryWrapper<T> between(boolean condition, SFunction<T, ?> column, Object start, Object end) {
        return between(condition, resolveFieldName(column), start, end);
    }

    public DwLambdaQueryWrapper<T> between(boolean condition, String fieldName, Object start, Object end) {
        if (!condition) {
            return this;
        }
        ge(fieldName, start);
        le(fieldName, end);
        return this;
    }

    public DwLambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        return orderByAsc(true, column);
    }

    public DwLambdaQueryWrapper<T> orderByAsc(String fieldName) {
        return orderByAsc(true, fieldName);
    }

    public DwLambdaQueryWrapper<T> orderByAsc(boolean condition, SFunction<T, ?> column) {
        return orderByAsc(condition, resolveFieldName(column));
    }

    public DwLambdaQueryWrapper<T> orderByAsc(boolean condition, String fieldName) {
        return addSort(condition, fieldName, false);
    }

    public DwLambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        return orderByDesc(true, column);
    }

    public DwLambdaQueryWrapper<T> orderByDesc(String fieldName) {
        return orderByDesc(true, fieldName);
    }

    public DwLambdaQueryWrapper<T> orderByDesc(boolean condition, SFunction<T, ?> column) {
        return orderByDesc(condition, resolveFieldName(column));
    }

    public DwLambdaQueryWrapper<T> orderByDesc(boolean condition, String fieldName) {
        return addSort(condition, fieldName, true);
    }

    public QueryRecordReq toQueryRecordReq() {
        QueryRecordReq req = new QueryRecordReq();
        if (StrUtil.isNotBlank(viewId)) {
            req.setViewId(viewId);
        }
        if (!selectFields.isEmpty()) {
            req.setFieldNames(new ArrayList<>(selectFields));
        }
        if (!sorts.isEmpty()) {
            req.setSort(new ArrayList<>(sorts));
        }
        if (pageNo != null) {
            req.setPageNo(pageNo);
        }
        if (pageSize != null) {
            req.setPageSize(pageSize);
        }
        if (StrUtil.isNotBlank(pageToken)) {
            req.setPageToken(pageToken);
        }
        if (automaticFields != null) {
            req.setAutomaticFields(automaticFields);
        }
        QueryRecordReq.Filter filter = buildFilter();
        if (filter != null) {
            req.setFilter(filter);
        }
        return req;
    }

    private QueryRecordReq.Filter buildFilter() {
        List<ConditionGroup> validGroups = new ArrayList<>();
        for (ConditionGroup group : groups) {
            if (group != null && group.hasConditions()) {
                validGroups.add(group);
            }
        }
        if (validGroups.isEmpty()) {
            return null;
        }
        if (validGroups.size() == 1) {
            QueryRecordReq.Filter filter = new QueryRecordReq.Filter();
            filter.setConjunction(CONJUNCTION_AND);
            filter.setConditions(validGroups.get(0).toConditions());
            return filter;
        }
        QueryRecordReq.Filter root = new QueryRecordReq.Filter();
        root.setConjunction(CONJUNCTION_OR);
        List<QueryRecordReq.Filter> children = new ArrayList<>();
        for (ConditionGroup group : validGroups) {
            QueryRecordReq.Filter child = new QueryRecordReq.Filter();
            child.setConjunction(CONJUNCTION_AND);
            child.setConditions(group.toConditions());
            children.add(child);
        }
        root.setChildren(children);
        return root;
    }

    private DwLambdaQueryWrapper<T> addCondition(boolean condition, String fieldName, String operator, Object value) {
        if (!condition || StrUtil.isBlank(fieldName)) {
            return this;
        }
        QueryRecordReq.Filter.Condition conditionItem = new QueryRecordReq.Filter.Condition();
        conditionItem.setFieldName(resolveFieldName(fieldName));
        conditionItem.setOperator(operator);
        conditionItem.setValue(normalizeValue(operator, value));
        this.currentGroup.add(conditionItem);
        return this;
    }

    private DwLambdaQueryWrapper<T> addSort(boolean condition, String fieldName, boolean desc) {
        if (!condition || StrUtil.isBlank(fieldName)) {
            return this;
        }
        QueryRecordReq.Sort sort = new QueryRecordReq.Sort();
        sort.setFieldName(resolveFieldName(fieldName));
        sort.setDesc(desc);
        this.sorts.add(sort);
        return this;
    }

    private Object normalizeValue(String operator, Object value) {
        if ("isEmpty".equals(operator) || "isNotEmpty".equals(operator)) {
            return Collections.emptyList();
        }
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof Collection) {
            return new ArrayList<>((Collection<?>) value);
        }
        Class<?> valueClass = value.getClass();
        if (valueClass.isArray()) {
            int length = Array.getLength(value);
            List<Object> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(value, i));
            }
            return list;
        }
        return Collections.singletonList(value);
    }

    private String resolveFieldName(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return fieldName;
        }
        if (entityClass == null) {
            return fieldName;
        }
        Map<String, String> map = getFieldNameMap();
        String mapped = map.get(fieldName);
        return mapped == null ? fieldName : mapped;
    }

    private String resolveFieldName(SFunction<T, ?> column) {
        if (column == null) {
            return null;
        }
        String property = resolveLambdaProperty(column);
        return resolveFieldName(property);
    }

    private Map<String, String> getFieldNameMap() {
        if (fieldNameMap == null) {
            fieldNameMap = buildFieldNameMap(entityClass);
        }
        return fieldNameMap;
    }

    private static Map<String, String> buildFieldNameMap(Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<>();
        if (clazz == null) {
            return map;
        }
        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            if (field.getAnnotation(FsDwTableId.class) != null) {
                map.put(field.getName(), "record_id");
                continue;
            }
            FsDwTableProperty property = field.getAnnotation(FsDwTableProperty.class);
            String resolvedName = resolveFieldName(field, property);
            map.put(field.getName(), resolvedName);
        }
        map.put("recordId", "record_id");
        map.put("record_id", "record_id");
        map.put("createdTime", "created_time");
        map.put("created_time", "created_time");
        map.put("lastModifiedTime", "last_modified_time");
        map.put("last_modified_time", "last_modified_time");
        return map;
    }

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

    private static String resolveFieldName(Field field, FsDwTableProperty property) {
        if (property != null) {
            if (StrUtil.isNotBlank(property.field())) {
                return property.field();
            }
            String[] values = property.value();
            if (values != null && values.length > 0 && StrUtil.isNotBlank(values[0])) {
                return values[0];
            }
        }
        return field.getName();
    }

    private static String resolveLambdaProperty(Serializable lambda) {
        SerializedLambda serializedLambda = resolveSerializedLambda(lambda);
        String methodName = serializedLambda.getImplMethodName();
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is") && methodName.length() > 2) {
            return decapitalize(methodName.substring(2));
        }
        return methodName;
    }

    private static SerializedLambda resolveSerializedLambda(Serializable lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(lambda);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to resolve lambda", e);
        }
    }

    private static String decapitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        char[] chars = value.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private static final class ConditionGroup {
        private final List<QueryRecordReq.Filter.Condition> conditions = new ArrayList<>();

        private void add(QueryRecordReq.Filter.Condition condition) {
            if (condition != null) {
                conditions.add(condition);
            }
        }

        private boolean hasConditions() {
            return !conditions.isEmpty();
        }

        private List<QueryRecordReq.Filter.Condition> toConditions() {
            return new ArrayList<>(conditions);
        }
    }

    @FunctionalInterface
    public interface SFunction<T, R> extends Serializable {
        R apply(T source);
    }
}
