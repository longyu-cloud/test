package cn.longyu.promotion.service;

import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.po.ExchangeCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【exchange_code(兑换码)】的数据库操作Service
* @createDate 2025-07-17 17:36:30
*/
public interface ExchangeCodeService extends IService<ExchangeCode> {

    void asyncGenerateCode(Coupon coupon);

    boolean updateExchangeMark(long id, boolean mark);
}
