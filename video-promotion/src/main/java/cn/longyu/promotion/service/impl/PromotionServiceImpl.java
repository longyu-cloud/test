package cn.longyu.promotion.service.impl;

import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.po.UserCoupon;
import cn.longyu.promotion.enums.UserCouponStatus;
import cn.longyu.promotion.service.CouponService;
import cn.longyu.promotion.service.ExchangeCodeService;
import cn.longyu.promotion.service.UserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.promotion.domain.po.Promotion;
import cn.longyu.promotion.service.PromotionService;
import cn.longyu.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【promotion(促销活动，形式多种多样，例如：优惠券)】的数据库操作Service实现
* @createDate 2025-07-17 17:36:37
*/
@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion>
    implements PromotionService{
    @Autowired
    private ExchangeCodeService codeService;

    @Autowired
    private UserCouponService userCouponService;


    /**
     * 保存到用户表
     */
    public void saveUserCoupon(Long userid,Long couponId,Coupon coupon){


        UserCoupon userCoupon = new UserCoupon();
        LocalDateTime termBeginTime = coupon.getTermBeginTime();
        if (termBeginTime == null) {
            termBeginTime = LocalDateTime.now();
            LocalDateTime termEndTime= termBeginTime.plusDays(coupon.getTermDays());
            userCoupon.setTermBeginTime(termBeginTime).setTermEndTime(termEndTime);
        }else {
            userCoupon.setTermBeginTime(coupon.getTermBeginTime()).setTermEndTime(coupon.getTermEndTime());
        }
        userCoupon.setUserId(userid).setStatus(UserCouponStatus.UNUSED).setCouponId(couponId);
        userCouponService.save(userCoupon);

    }


}




