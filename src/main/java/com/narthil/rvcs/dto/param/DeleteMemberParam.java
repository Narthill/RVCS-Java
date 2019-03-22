package com.narthil.rvcs.dto.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DeleteMemberParam
 */
@Data
@ApiModel(value = "DeleteMemberParam",description = "删除成员的输入参数")
public class  DeleteMemberParam{

    @ApiModelProperty(value="删除的成员ID")
    private String memberId;
    @ApiModelProperty(value="所属的根节点ID")
    private String rootId;
    @ApiModelProperty(value="组织ID")
    private String orgId;
}