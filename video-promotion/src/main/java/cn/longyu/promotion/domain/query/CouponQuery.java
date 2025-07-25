package cn.longyu.promotion.domain.query;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 优惠卷分页查询 前端传来的条件参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@ApiModel(description = "优惠券查询参数")
@Accessors(chain = true)
public class CouponQuery extends PageQuery {

//    @ApiModelProperty("优惠券折扣类型：1：每满减，2：折扣，3：无门槛，4：满减")
    private Integer type;

//    @ApiModelProperty("优惠券状态，1：待发放，2：发放中，3：已结束, 4：取消/终止")
    private Integer status;

//    @ApiModelProperty("优惠券名称")
    private String name;

}