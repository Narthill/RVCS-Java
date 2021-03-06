package com.narthil.rvcs.pojo;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UserInfo
 */


@Data
@Document(collection = "UserInfo")
@ApiModel(value="UserInfo", description = "用户所有信息json描述")
public class UserInfo {
    
    @ApiModelProperty(value="用户ID")
    @Id private String id;
    @ApiModelProperty(value="用户真名")
    private String name;
    @ApiModelProperty(value="用户邮箱")
    private String email;
    @ApiModelProperty(value="用户名")
    private String username;
    @ApiModelProperty(value="用户密码")
    private String password;
    @ApiModelProperty(value="用户系统权限")
    private List<String> roles;
    
}