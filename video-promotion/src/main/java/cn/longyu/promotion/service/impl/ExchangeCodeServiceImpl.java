package cn.longyu.promotion.service.impl;

import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.utils.CodeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.promotion.domain.po.ExchangeCode;
import cn.longyu.promotion.service.ExchangeCodeService;
import cn.longyu.promotion.mapper.ExchangeCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.longyu.promotion.constants.PromotionConstants.*;

/**
* @author longyu
* @description 针对表【exchange_code(兑换码)】的数据库操作Service实现
* @createDate
*/
@Service
public class ExchangeCodeServiceImpl extends ServiceImpl<ExchangeCodeMapper, ExchangeCode>
    implements ExchangeCodeService{
    /**
     * 使用redis自增优惠卷兑换码
     * @param coupon
     */
    private final StringRedisTemplate template;
    //创建一个绑定到 COUPON_CODE_SERIAL_KEY 的操作对象
    private final BoundValueOperations<String, String> serials;

    public ExchangeCodeServiceImpl(StringRedisTemplate template){
        this.template=template;
        this.serials = template.boundValueOps(COUPON_CODE_SERIAL_KEY);
    }
    @Override
    //异步调用线程池
    @Async("generateExchangeCodeExecutor")
    public void asyncGenerateCode(Coupon coupon) {
        //获得兑换码的数量
        Integer totalNum = coupon.getTotalNum();
        //代码健壮度判断
        if (totalNum==null){
            return;
        }
        //redis自增id
        Long increment = serials.increment(totalNum);
        int maxTotalNum = increment.intValue();
        ArrayList<ExchangeCode> exchangeCodes = new ArrayList<>(totalNum);
        for (int i= maxTotalNum-totalNum+1; i <=maxTotalNum ; i++) {
            // 2.生成兑换码
            String code = CodeUtil.generateCode(i, coupon.getId());
            ExchangeCode e = new ExchangeCode();
            e.setCode(code);
            e.setId(i);
            e.setExchangeTargetId(coupon.getId());
            e.setExpiredTime(coupon.getIssueEndTime());
            exchangeCodes.add(e);
        }
        this.saveBatch(exchangeCodes);
        // 4.写入Redis缓存，member：couponId，score：兑换码的最大序列号
        template.opsForZSet().add(COUPON_RANGE_KEY, coupon.getId().toString(), maxTotalNum);

    }

    /**
     * 修改兑换码的状态
     * @param id
     * @param mark
     */
    @Override
    public boolean updateExchangeMark(long id, boolean mark) {
        Boolean b = template.opsForValue().setBit(COUPON_CODE_KEY, id, mark);
        //&&是短路运算符，
        return b!=null&&b;
    }
}








