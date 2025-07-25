package cn.longyu.promotion.mapper;

import cn.longyu.promotion.domain.po.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
* @author Administrator
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Mapper
* @createDate 2025-07-17 17:36:08
* @Entity cn.longyu.promotion.domain.po.Coupon
*/
public interface CouponMapper extends BaseMapper<Coupon> {
    /**
     * 改良乐观锁
     */
    @Update("update coupon set issue_num =issue_num+1 where issue_num < total_num")
    int updateRepertory();
}




