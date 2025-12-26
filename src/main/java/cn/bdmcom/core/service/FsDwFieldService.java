package cn.bdmcom.core.service;

import cn.bdmcom.core.api.FsDwFieldApi;
import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.AddFieldReq;
import cn.bdmcom.core.domain.req.UpdateFieldReq;
import cn.bdmcom.core.domain.res.*;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞书多维表格字段服务。
 *
 * <p>提供字段级别 CRUD 与列表查询封装。</p>
 */
@Slf4j
public class FsDwFieldService {

    /**
     * 飞书接口统一成功码。
     */
    private static final Integer SUCCESS_CODE = 0;

    @Autowired
    private FsDwTokenService fsDwTokenService;

    @Autowired
    private FsDwFieldApi fsDwFieldApi;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 新增字段。
     *
     * @param appId       应用ID
     * @param appSecret   应用密钥
     * @param appToken    多维表格 App 的唯一标识
     * @param tableId     数据表唯一标识
     * @param clientToken 幂等请求标识
     * @param req         新增字段请求体
     * @return 新增字段结果
     */
    public AddFieldRes createField(String appId, String appSecret, String appToken, String tableId, String clientToken, AddFieldReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]新增字段请求不能为空");
        validateTableInfo(appToken, tableId);
        String res = fsDwFieldApi.createField(buildAuthorization(appId, appSecret), appToken, tableId, clientToken, req);
        AddFieldRes result = parseResponse("新增字段", res, AddFieldRes.class);
        log.info("[飞书多维表格]新增字段成功, code={}", result.getCode());
        return result;
    }

    /**
     * 更新字段。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param fieldId   字段唯一标识
     * @param req       更新字段请求体
     * @return 更新字段结果
     */
    public UpdateFieldRes updateField(String appId, String appSecret, String appToken, String tableId, String fieldId, UpdateFieldReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]更新字段请求不能为空");
        validateTableInfo(appToken, tableId);
        validateFieldId(fieldId);
        String res = fsDwFieldApi.updateField(buildAuthorization(appId, appSecret), appToken, tableId, fieldId, req);
        UpdateFieldRes result = parseResponse("更新字段", res, UpdateFieldRes.class);
        log.info("[飞书多维表格]更新字段成功, code={}", result.getCode());
        return result;
    }

    /**
     * 删除字段。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param fieldId   字段唯一标识
     * @return 删除字段结果
     */
    public DeleteFieldRes deleteField(String appId, String appSecret, String appToken, String tableId, String fieldId) {
        validateTableInfo(appToken, tableId);
        validateFieldId(fieldId);
        String res = fsDwFieldApi.deleteField(buildAuthorization(appId, appSecret), appToken, tableId, fieldId);
        DeleteFieldRes result = parseResponse("删除字段", res, DeleteFieldRes.class);
        log.info("[飞书多维表格]删除字段成功, code={}", result.getCode());
        return result;
    }

    /**
     * 查询指定表格的所有字段（自动分页）。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @return 字段列表
     */
    public List<TableFieldListRes.TableField> listFields(String appId, String appSecret, String appToken, String tableId) {
        return listFields(appId, appSecret, appToken, tableId, null);
    }

    /**
     * 查询指定表格的所有字段（自动分页）。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param viewId    视图 ID
     * @return 字段列表
     */
    public List<TableFieldListRes.TableField> listFields(String appId, String appSecret, String appToken, String tableId, String viewId) {
        validateTableInfo(appToken, tableId);

        List<TableFieldListRes.TableField> result = new ArrayList<>();
        String pageToken = null;
        boolean hasMore;

        do {
            String res = fsDwFieldApi.listFields(buildAuthorization(appId, appSecret), appToken, tableId, viewId, 100, pageToken);
            TableFieldListRes listRes = parseResponse("列出字段", res, TableFieldListRes.class);
            TableFieldListRes.FieldListData data = listRes.getData();
            if (data != null && data.getItems() != null) {
                result.addAll(data.getItems());
            }
            hasMore = data != null && Boolean.TRUE.equals(data.getHasMore());
            pageToken = data == null ? null : data.getPageToken();
        } while (hasMore && pageToken != null);

        return result;
    }

    /**
     * 构建请求头中的授权信息。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @return Authorization header 值
     */
    private String buildAuthorization(String appId, String appSecret) {
        String token = fsDwTokenService.getToken(appId, appSecret);
        BitableAssert.notBlank(token, BitableErrorCode.TOKEN_ACQUIRE_FAILED, "[飞书多维表格]token获取失败");
        return FsDwConstants.AUTHORIZATION_PREFIX + token;
    }

    /**
     * 解析飞书接口返回的 JSON 响应。
     *
     * @param action 操作名称
     * @param res    接口响应 JSON 字符串
     * @param clazz  响应类型
     * @param <T>    响应类型
     * @return 解析后的响应对象
     */
    private <T extends AbstractRes<?>> T parseResponse(String action, String res, Class<T> clazz) {
        BitableAssert.notBlank(res, BitableErrorCode.FEISHU_RESPONSE_EMPTY, "[飞书多维表格][{}]响应为空", action);
        try {
            T result = objectMapper.readValue(res, clazz);
            BitableAssert.notNull(result, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书多维表格][{}]解析结果为空", action);
            BitableAssert.isTrue(SUCCESS_CODE.equals(result.getCode()), BitableErrorCode.FEISHU_API_ERROR,
                    "[飞书多维表格][{}]失败, code={}, msg={}, error={}", action, result.getCode(), result.getMsg(), result.getError());
            return result;
        } catch (BitableException e) {
            throw e;
        } catch (Exception e) {
            String message = StrUtil.format("[飞书多维表格][{}]解析响应失败: {}", action, res);
            throw new BitableException(BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, message, e);
        }
    }

    /**
     * 校验表格信息。
     *
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     */
    private void validateTableInfo(String appToken, String tableId) {
        BitableAssert.notBlank(appToken, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]appToken不能为空");
        BitableAssert.notBlank(tableId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId不能为空");
    }

    /**
     * 校验字段ID。
     *
     * @param fieldId 字段唯一标识
     */
    private void validateFieldId(String fieldId) {
        BitableAssert.notBlank(fieldId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]fieldId不能为空");
    }
}
