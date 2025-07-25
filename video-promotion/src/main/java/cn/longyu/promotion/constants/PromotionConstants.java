package cn.longyu.promotion.constants;


/**
 * 如果是常量的话，建议使用接口来定义常量，可以直接进行调用
 */
public interface PromotionConstants {
    //redis中兑换码的唯一key
    String COUPON_CODE_SERIAL_KEY="coupon:code:serial";
    String COUPON_RANGE_KEY="coupon:code:serial:redis";
    String COUPON_CODE_KEY="coupon:code:map";
}
