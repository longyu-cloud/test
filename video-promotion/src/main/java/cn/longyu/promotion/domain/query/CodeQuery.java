package cn.longyu.promotion.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
//@ApiModel(description = "兑换码查询参数")
public class CodeQuery extends PageQuery {
//    @ApiModelProperty("兑换码对应的优惠券id")
    @NotNull
    private Long couponId;
//    @ApiModelProperty("兑换码状态，1：未兑换，2：已兑换")
    @NotNull
    private Integer status;
}