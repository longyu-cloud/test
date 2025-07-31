package cn.longyu.promotion.service;

import cn.longyu.promotion.domain.dto.CouponIssueFormDTO;
import cn.longyu.promotion.domain.dto.PageDTO;
import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.dto.CouponFormDTO;
import cn.longyu.promotion.domain.po.ExchangeCode;
import cn.longyu.promotion.domain.query.CouponQuery;
import cn.longyu.promotion.domain.vo.CouponPageVO;
import cn.longyu.promotion.domain.vo.CouponVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service
* @createDate  2025-07-17 17:36:08
*/
public interface CouponService extends IService<Coupon> {
    /**
     * 新增优惠卷
     */
    void saveCoupon(CouponFormDTO dto);
    /**
     * 分页查询优惠卷
     * @param query
     * @return
     */
    PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query);
    /**
     * 立即发放优惠卷
     * @param dto
     */
    void distributeCoupon(CouponIssueFormDTO dto);
    /**
     * 定时发放优惠卷 cron表达式
     * 每天0点执行一次
     */
    void regularDistributeCoupons();
    /**
     * 查询发放中的优惠卷
     * @return CouPonVo
     */
    List<CouponVO> queryIssuingCoupon(Long userid);
    /**
     * 手动领取优惠
     *
     * @param userid
     * @param couponId
     */
    int addIssuingCoupon(Long userid, Long couponId);
    /**
     * 兑换码领取优惠卷
     *
     * @param code
     */
    void receiveRedeemCode(String code);
    /**
     * 为了事务要在锁里面 抽取保存数据库的方法
     */
    void saveSuccessCoupon(Long couponId, Long userid, ExchangeCode exchangeCode);
}
