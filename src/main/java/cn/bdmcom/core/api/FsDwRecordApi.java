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
     */
    @Post(url = FsDwConstants.APP_ACCESS_TOKEN_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String getToken(@Body QueryTokenReq req);


    /**
     * 新增记录。
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
     */
    @Get(url = FsDwConstants.FIELDS_URL)
    String listFields(@Header("Authorization") String token,
                      @Var("app_token") String appToken,
                      @Var("table_id") String tableId,
                      @Query("page_size") Integer pageSize,
                      @Query("page_token") String pageToken);
}
