package com.narthil.rvcs.dto.param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="UserLoginParam", description = "用户登录时需要的数据")
public class UserLoginParam {

    @ApiModelProperty(value="用户名", required = true)
    private String username;
    @ApiModelProperty(value="用户密码", required = true)
    private String password;
}