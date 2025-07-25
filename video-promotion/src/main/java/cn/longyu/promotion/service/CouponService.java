package cn.longyu.promotion.service;

import cn.longyu.promotion.domain.dto.CouponIssueFormDTO;
import cn.longyu.promotion.domain.dto.PageDTO;
import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.dto.CouponFormDTO;
import cn.longyu.promotion.domain.query.CouponQuery;
import cn.longyu.promotion.domain.vo.CouponPageVO;
import cn.longyu.promotion.domain.vo.CouponVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service
* @createDate 2025-07-17 17:36:08
*/
public interface CouponService extends IService<Coupon> {

    void saveCoupon(CouponFormDTO dto);

    PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query);

    void distributeCoupon(CouponIssueFormDTO dto);
    void regularDistributeCoupons();

    List<CouponVO> queryIssuingCoupon(Long userid);


    int addIssuingCoupon(Long userid, Long couponId);

    void receiveRedeemCode(String code);
}
