package cn.longyu.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 优惠券作用范围信息  优惠卷和上用户的中间表
 * @TableName coupon_scope
 */
@TableName(value ="coupon_scope")
@Data
public class CouponScope {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    public CouponScope setCouponId(Long couponId) {
        this.couponId = couponId;
        return this;
    }

    public CouponScope setBizId(Long bizId) {
        this.bizId = bizId;
        return this;
    }

    /**
     * 范围限定类型：1-分类，2-课程，等等
     */
    private Integer type;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券作用范围的业务id，例如分类id、课程id
     */
    private Long bizId;
}