package cn.bdmcom.core.api;

import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.BatchCreateTableReq;
import cn.bdmcom.core.domain.req.BatchDeleteTableReq;
import cn.bdmcom.core.domain.req.CreateTableReq;
import cn.bdmcom.core.domain.req.UpdateTableReq;
import com.dtflys.forest.annotation.*;


/**
 * 飞书多维表格数据表 API 定义。
 */
public interface FsDwTableApi {

    /**
     * 新增数据表。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param req      新增数据表请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.TABLES_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String createTable(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Body CreateTableReq req);

    /**
     * 批量新增数据表。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param req      批量新增数据表请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.TABLES_BATCH_CREATE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String batchCreateTable(@Header("Authorization") String token,
                            @Var("app_token") String appToken,
                            @Body BatchCreateTableReq req);

    /**
     * 更新数据表名称。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @param req      更新数据表请求体
     * @return 接口响应 JSON 字符串
     */
    @Patch(url = FsDwConstants.TABLE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String updateTable(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId,
                       @Body UpdateTableReq req);

    /**
     * 删除数据表。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param tableId  数据表唯一标识
     * @return 接口响应 JSON 字符串
     */
    @Delete(url = FsDwConstants.TABLE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String deleteTable(@Header("Authorization") String token,
                       @Var("app_token") String appToken,
                       @Var("table_id") String tableId);

    /**
     * 批量删除数据表。
     *
     * @param token    授权 token
     * @param appToken 多维表格 App 的唯一标识
     * @param req      批量删除数据表请求体
     * @return 接口响应 JSON 字符串
     */
    @Post(url = FsDwConstants.TABLES_BATCH_DELETE_URL,
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            })
    String batchDeleteTable(@Header("Authorization") String token,
                            @Var("app_token") String appToken,
                            @Body BatchDeleteTableReq req);

    /**
     * 列出数据表。
     *
     * @param token     授权 token
     * @param appToken  多维表格 App 的唯一标识
     * @param pageSize  分页大小
     * @param pageToken 分页 token
     * @return 接口响应 JSON 字符串
     */
    @Get(url = FsDwConstants.TABLES_URL)
    String listTables(@Header("Authorization") String token,
                      @Var("app_token") String appToken,
                      @Query("page_size") Integer pageSize,
                      @Query("page_token") String pageToken);
}
