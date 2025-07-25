package cn.longyu.promotion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.promotion.domain.po.Promotion;
import cn.longyu.promotion.service.PromotionService;
import cn.longyu.promotion.mapper.PromotionMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【promotion(促销活动，形式多种多样，例如：优惠券)】的数据库操作Service实现
* @createDate 2025-07-17 17:36:37
*/
@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion>
    implements PromotionService{

}




