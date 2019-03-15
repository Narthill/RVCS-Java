package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="UserInfoParam", description = "查找用户信息时的输入数据")
public class UserInfoParam {

    @ApiModelProperty(value="用户名", required = true)
    private String username;
}