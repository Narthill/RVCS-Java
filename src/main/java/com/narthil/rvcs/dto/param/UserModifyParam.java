package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UserModifyParam
 */
@Data
@ApiModel(value="UserModifyParam", description = "修改用户信息时的输入的数据")
public class UserModifyParam {
    @ApiModelProperty(value="用户邮箱",required = false)
    private String email;
    @ApiModelProperty(value="用户名",required = false)
    private String username;
    @ApiModelProperty(value="用户密码",required = false)
    private String password;
}