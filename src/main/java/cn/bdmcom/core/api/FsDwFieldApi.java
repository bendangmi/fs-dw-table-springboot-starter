package cn.bdmcom.core.api;

import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.AddFieldReq;
import cn.bdmcom.core.domain.req.UpdateFieldReq;
import com.dtflys.forest.annotation.*;


/**
 * 飞书多维表格字段 API 定义。
 */
public interface FsDwFieldApi {

    /**
     * 新增字段。
     *
     * @param token       授权 token
     * @param appToken    多维表格 App 的唯一标识
     * @param tableId     数据表唯一标识
     * @param clientToken 幂等请求标识
     * @param req         新增字段请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.FIELDS_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String createField(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId,
                       @Query("client_token") String clientToken,
                       @Body AddFieldReq req);

    /**
     * 更新字段。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param fieldId  字段唯一标识
     * @param req      更新字段请求体
     * @return 接口响应 JSON 字符串
     */
    @Put(url = FsDwConstants.FIELD_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String updateField(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId,
                       @Var("field_id") String fieldId,
                       @Body UpdateFieldReq req);

    /**
     * 删除字段。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param fieldId  字段唯一标识
     * @return 接口响应 JSON 字符串
     */
    @Delete(url = FsDwConstants.FIELD_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String deleteField(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId,
                       @Var("field_id") String fieldId);

    /**
     * 列出字段。
     *
     * @param token     授权 token
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param viewId    视图 ID
     * @param pageSize  分页大小
     * @param pageToken 分页 token
     * @return 接口响应 JSON 字符串
     */
    @Get(url = FsDwConstants.FIELDS_URL)
    String listFields(@Header("Authorization") String token,
                      @Var("app_token") String appToken,
                      @Var("table_id") String tableId,
                      @Query("view_id") String viewId,
                      @Query("page_size") Integer pageSize,
                      @Query("page_token") String pageToken);
}
