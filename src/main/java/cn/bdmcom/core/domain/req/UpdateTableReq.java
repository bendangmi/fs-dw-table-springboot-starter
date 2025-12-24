package cn.bdmcom.core.domain.req;

import lombok.Data;

/**
 * 更新数据表请求体。
 */
@Data
public class UpdateTableReq {

    /**
     * 数据表新名称。
     */
    private String name;
}
