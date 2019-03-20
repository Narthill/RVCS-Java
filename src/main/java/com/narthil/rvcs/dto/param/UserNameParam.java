package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="UserNameParam", description = "用户名输入数据")
public class UserNameParam{

    @ApiModelProperty(value="用户名", required = true)
    private String username;
}