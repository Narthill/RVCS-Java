package com.narthil.rvcs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ResultInfo
 */
@Data
@ApiModel(value="ResultInfo", description = "返回数据")
public class ResultInfo<T> {

    @ApiModelProperty(value="状态")
    private StatusInfo status;
    @ApiModelProperty(value="数据",example="{'name':'chenhao','username':'narthil'}")
    private T data ;
}