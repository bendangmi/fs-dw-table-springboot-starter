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

    /**
     * 创建查询构建器（无实体类型）。
     */
    public DwLambdaQueryWrapper() {
        this(null);
    }

    /**
     * 创建查询构建器。
     *
     * @param entityClass 实体类型
     */
    public DwLambdaQueryWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.currentGroup = new ConditionGroup();
        this.groups.add(this.currentGroup);
    }

    /**
     * 设置视图 ID。
     *
     * @param viewId 视图 ID
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> viewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    /**
     * 设置分页大小。
     *
     * @param pageSize 分页大小
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 设置页码。
     *
     * @param pageNo 页码
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> pageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    /**
     * 设置分页 token。
     *
     * @param pageToken 分页 token
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> pageToken(String pageToken) {
        this.pageToken = pageToken;
        return this;
    }

    /**
     * 设置是否自动返回系统字段。
     *
     * @param automaticFields 是否自动返回系统字段
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> automaticFields(Boolean automaticFields) {
        this.automaticFields = automaticFields;
        return this;
    }

    /**
     * 指定查询字段（字段名）。
     *
     * @param fields 字段名数组
     * @return 当前构建器
     */
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

    /**
     * 指定查询字段（Lambda 方式）。
     *
     * @param columns 字段 Lambda
     * @return 当前构建器
     */
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

    /**
     * 新建 OR 条件组。
     *
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> or() {
        if (this.currentGroup != null && this.currentGroup.hasConditions()) {
            this.currentGroup = new ConditionGroup();
            this.groups.add(this.currentGroup);
        }
        return this;
    }

    /**
     * 保持 AND 连接（与默认一致）。
     *
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> and() {
        return this;
    }

    /**
     * 等于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object value) {
        return eq(true, column, value);
    }

    /**
     * 等于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> eq(String fieldName, Object value) {
        return eq(true, fieldName, value);
    }

    /**
     * 等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object value) {
        return eq(condition, resolveFieldName(column), value);
    }

    /**
     * 等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> eq(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "is", value);
    }

    /**
     * 不等于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ne(SFunction<T, ?> column, Object value) {
        return ne(true, column, value);
    }

    /**
     * 不等于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ne(String fieldName, Object value) {
        return ne(true, fieldName, value);
    }

    /**
     * 不等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ne(boolean condition, SFunction<T, ?> column, Object value) {
        return ne(condition, resolveFieldName(column), value);
    }

    /**
     * 不等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ne(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isNot", value);
    }

    /**
     * 包含条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> like(SFunction<T, ?> column, Object value) {
        return like(true, column, value);
    }

    /**
     * 包含条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> like(String fieldName, Object value) {
        return like(true, fieldName, value);
    }

    /**
     * 包含条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> like(boolean condition, SFunction<T, ?> column, Object value) {
        return like(condition, resolveFieldName(column), value);
    }

    /**
     * 包含条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> like(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "contains", value);
    }

    /**
     * 不包含条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> notLike(SFunction<T, ?> column, Object value) {
        return notLike(true, column, value);
    }

    /**
     * 不包含条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> notLike(String fieldName, Object value) {
        return notLike(true, fieldName, value);
    }

    /**
     * 不包含条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> notLike(boolean condition, SFunction<T, ?> column, Object value) {
        return notLike(condition, resolveFieldName(column), value);
    }

    /**
     * 不包含条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> notLike(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "doesNotContain", value);
    }

    /**
     * 大于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> gt(SFunction<T, ?> column, Object value) {
        return gt(true, column, value);
    }

    /**
     * 大于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> gt(String fieldName, Object value) {
        return gt(true, fieldName, value);
    }

    /**
     * 大于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object value) {
        return gt(condition, resolveFieldName(column), value);
    }

    /**
     * 大于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> gt(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isGreater", value);
    }

    /**
     * 大于等于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ge(SFunction<T, ?> column, Object value) {
        return ge(true, column, value);
    }

    /**
     * 大于等于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ge(String fieldName, Object value) {
        return ge(true, fieldName, value);
    }

    /**
     * 大于等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object value) {
        return ge(condition, resolveFieldName(column), value);
    }

    /**
     * 大于等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> ge(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isGreaterEqual", value);
    }

    /**
     * 小于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> lt(SFunction<T, ?> column, Object value) {
        return lt(true, column, value);
    }

    /**
     * 小于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> lt(String fieldName, Object value) {
        return lt(true, fieldName, value);
    }

    /**
     * 小于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object value) {
        return lt(condition, resolveFieldName(column), value);
    }

    /**
     * 小于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> lt(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isLess", value);
    }

    /**
     * 小于等于条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param value  值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> le(SFunction<T, ?> column, Object value) {
        return le(true, column, value);
    }

    /**
     * 小于等于条件（字段名）。
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> le(String fieldName, Object value) {
        return le(true, fieldName, value);
    }

    /**
     * 小于等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> le(boolean condition, SFunction<T, ?> column, Object value) {
        return le(condition, resolveFieldName(column), value);
    }

    /**
     * 小于等于条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param value     值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> le(boolean condition, String fieldName, Object value) {
        return addCondition(condition, fieldName, "isLessEqual", value);
    }

    /**
     * IN 条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param values 值列表
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> in(SFunction<T, ?> column, Object... values) {
        return in(true, column, values);
    }

    /**
     * IN 条件（字段名）。
     *
     * @param fieldName 字段名
     * @param values    值列表
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> in(String fieldName, Object... values) {
        return in(true, fieldName, values);
    }

    /**
     * IN 条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param values    值列表
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> in(boolean condition, SFunction<T, ?> column, Object... values) {
        return in(condition, resolveFieldName(column), values);
    }

    /**
     * IN 条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param values    值列表
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> in(boolean condition, String fieldName, Object... values) {
        return addCondition(condition, fieldName, "in", values == null ? null : Arrays.asList(values));
    }

    /**
     * 为空条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNull(SFunction<T, ?> column) {
        return isNull(true, column);
    }

    /**
     * 为空条件（字段名）。
     *
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNull(String fieldName) {
        return isNull(true, fieldName);
    }

    /**
     * 为空条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNull(boolean condition, SFunction<T, ?> column) {
        return isNull(condition, resolveFieldName(column));
    }

    /**
     * 为空条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNull(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, "isEmpty", Collections.emptyList());
    }

    /**
     * 非空条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNotNull(SFunction<T, ?> column) {
        return isNotNull(true, column);
    }

    /**
     * 非空条件（字段名）。
     *
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNotNull(String fieldName) {
        return isNotNull(true, fieldName);
    }

    /**
     * 非空条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNotNull(boolean condition, SFunction<T, ?> column) {
        return isNotNull(condition, resolveFieldName(column));
    }

    /**
     * 非空条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> isNotNull(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, "isNotEmpty", Collections.emptyList());
    }

    /**
     * 区间条件（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @param start  起始值
     * @param end    结束值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> between(SFunction<T, ?> column, Object start, Object end) {
        return between(true, column, start, end);
    }

    /**
     * 区间条件（字段名）。
     *
     * @param fieldName 字段名
     * @param start     起始值
     * @param end       结束值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> between(String fieldName, Object start, Object end) {
        return between(true, fieldName, start, end);
    }

    /**
     * 区间条件（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @param start     起始值
     * @param end       结束值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> between(boolean condition, SFunction<T, ?> column, Object start, Object end) {
        return between(condition, resolveFieldName(column), start, end);
    }

    /**
     * 区间条件（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param start     起始值
     * @param end       结束值
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> between(boolean condition, String fieldName, Object start, Object end) {
        if (!condition) {
            return this;
        }
        ge(fieldName, start);
        le(fieldName, end);
        return this;
    }

    /**
     * 升序排序（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        return orderByAsc(true, column);
    }

    /**
     * 升序排序（字段名）。
     *
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByAsc(String fieldName) {
        return orderByAsc(true, fieldName);
    }

    /**
     * 升序排序（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByAsc(boolean condition, SFunction<T, ?> column) {
        return orderByAsc(condition, resolveFieldName(column));
    }

    /**
     * 升序排序（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByAsc(boolean condition, String fieldName) {
        return addSort(condition, fieldName, false);
    }

    /**
     * 降序排序（Lambda 字段）。
     *
     * @param column 字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        return orderByDesc(true, column);
    }

    /**
     * 降序排序（字段名）。
     *
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByDesc(String fieldName) {
        return orderByDesc(true, fieldName);
    }

    /**
     * 降序排序（带开关）。
     *
     * @param condition 是否生效
     * @param column    字段 Lambda
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByDesc(boolean condition, SFunction<T, ?> column) {
        return orderByDesc(condition, resolveFieldName(column));
    }

    /**
     * 降序排序（带开关）。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @return 当前构建器
     */
    public DwLambdaQueryWrapper<T> orderByDesc(boolean condition, String fieldName) {
        return addSort(condition, fieldName, true);
    }

    /**
     * 构建查询请求体。
     *
     * @return 查询请求体
     */
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

    /**
     * 组装过滤条件。
     *
     * @return 过滤条件
     */
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

    /**
     * 添加过滤条件。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param operator  操作符
     * @param value     值
     * @return 当前构建器
     */
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

    /**
     * 添加排序条件。
     *
     * @param condition 是否生效
     * @param fieldName 字段名
     * @param desc      是否降序
     * @return 当前构建器
     */
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

    /**
     * 规范化条件值。
     *
     * @param operator 操作符
     * @param value    值
     * @return 规范化后的值
     */
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

    /**
     * 解析字段名（根据实体注解映射）。
     *
     * @param fieldName 字段名
     * @return 映射后的字段名
     */
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

    /**
     * 解析 Lambda 字段名。
     *
     * @param column 字段 Lambda
     * @return 字段名
     */
    private String resolveFieldName(SFunction<T, ?> column) {
        if (column == null) {
            return null;
        }
        String property = resolveLambdaProperty(column);
        return resolveFieldName(property);
    }

    /**
     * 获取字段名映射表。
     *
     * @return 字段名映射表
     */
    private Map<String, String> getFieldNameMap() {
        if (fieldNameMap == null) {
            fieldNameMap = buildFieldNameMap(entityClass);
        }
        return fieldNameMap;
    }

    /**
     * 构建字段名映射表。
     *
     * @param clazz 实体类型
     * @return 字段名映射表
     */
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
     * 解析字段名称。
     *
     * @param field    字段
     * @param property 字段注解
     * @return 字段名
     */
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

    /**
     * 从 Lambda 中解析属性名。
     *
     * @param lambda Lambda 表达式
     * @return 属性名
     */
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

    /**
     * 解析 Lambda 序列化信息。
     *
     * @param lambda Lambda 表达式
     * @return 序列化对象
     */
    private static SerializedLambda resolveSerializedLambda(Serializable lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(lambda);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to resolve lambda", e);
        }
    }

    /**
     * 首字母小写。
     *
     * @param value 字符串
     * @return 首字母小写后的字符串
     */
    private static String decapitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        char[] chars = value.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 条件组。
     */
    private static final class ConditionGroup {
        private final List<QueryRecordReq.Filter.Condition> conditions = new ArrayList<>();

        /**
         * 添加条件。
         *
         * @param condition 条件
         */
        private void add(QueryRecordReq.Filter.Condition condition) {
            if (condition != null) {
                conditions.add(condition);
            }
        }

        /**
         * 是否包含条件。
         *
         * @return 是否包含条件
         */
        private boolean hasConditions() {
            return !conditions.isEmpty();
        }

        /**
         * 获取条件列表。
         *
         * @return 条件列表
         */
        private List<QueryRecordReq.Filter.Condition> toConditions() {
            return new ArrayList<>(conditions);
        }
    }

    @FunctionalInterface
    public interface SFunction<T, R> extends Serializable {
        /**
         * 应用函数。
         *
         * @param source 源对象
         * @return 返回值
         */
        R apply(T source);
    }
}
