package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UserRegisterParam
 */
@Data
@ApiModel(value="UserRegisterParam", description = "用户注册时的输入的数据")
public class UserRegisterParam {
    // @ApiModelProperty(value="用户真名")
    // private String name;
    @ApiModelProperty(value="用户邮箱")
    private String email;
    @ApiModelProperty(value="用户名")
    private String username;
    @ApiModelProperty(value="用户密码")
    private String password;
}