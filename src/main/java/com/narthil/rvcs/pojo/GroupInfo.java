package com.narthil.rvcs.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GroupInfo
 */
@Data
@Document(collection = "ContactInfo")
@ApiModel(value="GroupInfo", description = "群组信息")
public class GroupInfo {
    
    @ApiModelProperty(value="群ID")
    @Id private String id;
    @ApiModelProperty(value="群组名")
    private String name;
    @ApiModelProperty(value="成员ID")
    private List<String> members;
}