package cn.longyu.promotion.service.impl;

import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.enums.ExchangeCodeStatus;
import cn.longyu.promotion.enums.UserCouponStatus;
import cn.longyu.promotion.service.CouponService;
import cn.longyu.promotion.service.ExchangeCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.promotion.domain.po.UserCoupon;
import cn.longyu.promotion.service.UserCouponService;
import cn.longyu.promotion.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【user_coupon(用户领取优惠券的记录，是真正使用的优惠券信息)】的数据库操作Service实现
* @createDate 2025-07-23 15:14:02
*/
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon>
    implements UserCouponService{


}




