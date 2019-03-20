package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgIdParam
 */

@Data
@ApiModel(value="OrgIdParam", description = "节点id")
public class OrgIdParam {

    @ApiModelProperty(value="节点id", required = true)
    private String orgId;
}