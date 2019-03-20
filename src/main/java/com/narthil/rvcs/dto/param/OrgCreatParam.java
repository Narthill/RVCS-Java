package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgCreatParam
 */
@Data
@ApiModel(value="OrgCreatParam", description = "录入组织信息时的输入数据")
public class OrgCreatParam {

    @ApiModelProperty(value="组织名字", required = true)
    private String name;
    @ApiModelProperty(value="组织描述", required = true)
    private String desc;
    @ApiModelProperty(value="父节点id", required = true)
    private String parent;
    @ApiModelProperty(value="根节点id", required = true)
    private String rootId;
    @ApiModelProperty(value="创建节点的用户id", required = true)
    private String userId;
}