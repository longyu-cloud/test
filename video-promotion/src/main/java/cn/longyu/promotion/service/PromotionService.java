package cn.longyu.promotion.service;

import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.po.Promotion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【promotion(促销活动，形式多种多样，例如：优惠券)】的数据库操作Service
* @createDate 2025-07-17 17:36:37
*/
public interface PromotionService extends IService<Promotion> {
    void saveUserCoupon(Long userid, Long couponId, Coupon coupon);
}
