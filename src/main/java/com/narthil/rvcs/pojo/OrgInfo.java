package com.narthil.rvcs.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgInfo
 */

@Data
@Document(collection = "OrgInfo")
public class OrgInfo {

    
    @ApiModelProperty(value="组织ID")
    @Id private String id;
    @ApiModelProperty(value="根节点标识")
    private Boolean root;
    @ApiModelProperty(value="根节点id")
    private String rootId;
    @ApiModelProperty(value="节点名")
    private String name;
    @ApiModelProperty(value="节点描述")
    private String desc;
    @ApiModelProperty(value="父节点")
    private String parent;
    @ApiModelProperty(value="子节点")
    private List<String> children;
    // private List<MemberInfo> members;
}