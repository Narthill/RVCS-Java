package com.narthil.rvcs.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ComCreatParam
 */
@Data
@ApiModel(value="ComCreatParam", description = "录入公司信息时的输入数据")
public class ComCreatParam {
    @ApiModelProperty(value="公司名字", required = true)
    private String name;
    @ApiModelProperty(value="公司描述", required = true)
    private String desc;
    @ApiModelProperty(value="创建公司的用户id", required = true)
    private String userId;
}