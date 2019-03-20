package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="UserIdParam", description = "用户id输入数据")
public class UserIdParam{

    @ApiModelProperty(value="用户id", required = true)
    private String userId;
}