package com.narthil.rvcs.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * MemberInfo
 */
@Data

@Document(collection = "MemberInfo")
public class MemberInfo {

    
    @ApiModelProperty(value="成员表的ID")
    @Id private String id;
    @ApiModelProperty(value="用户ID")
    private String userId;
    @ApiModelProperty(value="所属的根节点ID")
    private String rootId;
    @ApiModelProperty(value="组织ID")
    private String orgId;
    @ApiModelProperty(value="权限",notes = "分为 0.无 1.主管理员(可为他人下发二级权限)，2.全公司编辑")
    private int auth;
    @ApiModelProperty(value="权限描述")
    private String authDesc;
    // auth 分为 0.无 1.主管理员(可为他人下发二级权限)，2.全公司编辑
}