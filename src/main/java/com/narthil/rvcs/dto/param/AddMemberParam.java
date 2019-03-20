package com.narthil.rvcs.dto.param;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AddMemberParam
 */
@Data
@Document(collection = "AddMemberParam")
public class  AddMemberParam{

    @ApiModelProperty(value="发起添加成员请求的ID")
    private String userId;
    @ApiModelProperty(value="添加的成员ID")
    private String memberId;
    @ApiModelProperty(value="所属的根节点ID")
    private String rootId;
    @ApiModelProperty(value="组织ID")
    private String orgId;
}