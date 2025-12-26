package cn.bdmcom.core.api;

import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.*;
import com.dtflys.forest.annotation.*;

/**
 * 飞书多维表格 API 定义。
 *
 * <p>该接口仅负责 HTTP 请求映射，不包含业务逻辑。</p>
 */
public interface FsDwRecordApi {


    /**
     * 获取 App Access Token。
     *
     * @param req 获取 token 的请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.APP_ACCESS_TOKEN_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String getToken(@Body QueryTokenReq req);


    /**
     * 新增记录。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param req      新增记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.RECORDS_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String addRecord(@Header("Authorization") String token,
                     @Var("app_token") String appToken,
                     @Var("table_id") String tableId,
                     @Body AddRecordReq req);

    /**
     * 批量新增记录。
     *
     * @param token                   授权 token
     * @param appToken                多维表格 App 的唯一标识
     * @param tableId                 数据表唯一标识
     * @param userIdType              用户 ID 类型
     * @param clientToken             幂等请求标识
     * @param ignoreConsistencyCheck  是否忽略一致性校验
     * @param req                     批量新增记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.RECORDS_BATCH_CREATE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String batchCreateRecord(@Header("Authorization") String token,
                             @Var("app_token") String appToken,
                             @Var("table_id") String tableId,
                             @Query("user_id_type") String userIdType,
                             @Query("client_token") String clientToken,
                             @Query("ignore_consistency_check") Boolean ignoreConsistencyCheck,
                             @Body BatchCreateRecordReq req);


    /**
     * 更新记录。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param recordId 记录唯一标识
     * @param req      更新记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Put(url = FsDwConstants.RECORD_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            }
    )
    String updateRecord(@Header("Authorization") String token,
                        @Var("app_token") String appToken,
                        @Var("table_id") String tableId,
                        @Var("record_id") String recordId,
                        @Body UpdateRecordReq req);

    /**
     * 批量更新记录。
     *
     * @param token                  授权 token
     * @param appToken               多维表格 App 的唯一标识
     * @param tableId                数据表唯一标识
     * @param userIdType             用户 ID 类型
     * @param ignoreConsistencyCheck 是否忽略一致性校验
     * @param req                    批量更新记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.RECORDS_BATCH_UPDATE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String batchUpdateRecord(@Header("Authorization") String token,
                             @Var("app_token") String appToken,
                             @Var("table_id") String tableId,
                             @Query("user_id_type") String userIdType,
                             @Query("ignore_consistency_check") Boolean ignoreConsistencyCheck,
                             @Body BatchUpdateRecordReq req);


    /**
     * 查询记录。
     *
     * @param token     授权 token
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param req       查询记录请求体
     * @param pageToken 分页 token
     * @param pageSize  分页大小
     * @return 接口响应 JSON 字符串
     */

    @Post(url = FsDwConstants.RECORDS_SEARCH_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            }
    )
    String queryRecord(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId,
                       @Body QueryRecordReq req,
                       @Var("page_token") String pageToken,
                       @Var("page_size") Integer pageSize
    );

    /**
     * 删除记录。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param recordId 记录唯一标识
     * @return 接口响应 JSON 字符串
     */
    @Delete(url = FsDwConstants.RECORD_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            }
    )
    String deleteRecord(@Header("Authorization") String token,
                        @Var("app_token") String appToken,
                        @Var("table_id") String tableId,
                        @Var("record_id") String recordId);

    /**
     * 批量删除记录。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param req      批量删除记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.RECORDS_BATCH_DELETE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            }
    )
    String batchDeleteRecord(@Header("Authorization") String token,
                             @Var("app_token") String appToken,
                             @Var("table_id") String tableId,
                             @Body BatchDeleteRecordReq req);

    /**
     * 批量获取记录。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param req      批量获取记录请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.RECORDS_BATCH_GET_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            }
    )
    String batchGetRecord(@Header("Authorization") String token,
                          @Var("app_token") String appToken,
                          @Var("table_id") String tableId,
                          @Body BatchGetRecordReq req);

    /**
     * 列出字段。
     *
     * @param token     授权 token
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   数据表唯一标识
     * @param pageSize  分页大小
     * @param pageToken 分页 token
     * @return 接口响应 JSON 字符串
     */
    @Get(url = FsDwConstants.FIELDS_URL)
    String listFields(@Header("Authorization") String token,
                      @Var("app_token") String appToken,
                      @Var("table_id") String tableId,
                      @Query("page_size") Integer pageSize,
                      @Query("page_token") String pageToken);
}
