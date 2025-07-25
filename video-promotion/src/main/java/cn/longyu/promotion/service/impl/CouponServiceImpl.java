package cn.longyu.promotion.service.impl;

import cn.longyu.conmon.enums.CommonStatus;
import cn.longyu.conmon.utils.BeanUtils;
import cn.longyu.conmon.utils.CollUtils;
import cn.longyu.promotion.domain.dto.CouponIssueFormDTO;
import cn.longyu.promotion.domain.dto.PageDTO;
import cn.longyu.promotion.domain.po.CouponScope;
import cn.longyu.promotion.domain.dto.CouponFormDTO;
import cn.longyu.promotion.domain.po.ExchangeCode;
import cn.longyu.promotion.domain.po.UserCoupon;
import cn.longyu.promotion.domain.query.CouponQuery;
import cn.longyu.promotion.domain.vo.CouponPageVO;
import cn.longyu.promotion.domain.vo.CouponVO;
import cn.longyu.promotion.enums.CouponStatus;
import cn.longyu.promotion.enums.ExchangeCodeStatus;
import cn.longyu.promotion.enums.ObtainType;
import cn.longyu.promotion.enums.UserCouponStatus;
import cn.longyu.promotion.exceptions.BadRequestException;
import cn.longyu.promotion.service.CouponScopeService;
import cn.longyu.promotion.service.ExchangeCodeService;
import cn.longyu.promotion.service.UserCouponService;
import cn.longyu.promotion.utils.CodeUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.service.CouponService;
import cn.longyu.promotion.mapper.CouponMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.longyu.promotion.enums.CouponStatus.ISSUING;

/**
 * @author Administrator
 * @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service实现
 * @createDate 2025-07-17 17:36:08
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    @Autowired
    private CouponScopeService scopeService;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private ExchangeCodeService codeService;
    @Autowired
    private UserCouponService userCouponService;

    @Transactional
    @Override
    /**
     * 新增优惠卷
     */
    public void saveCoupon(CouponFormDTO dto) {

        // 复制属性到coupon中
        Coupon coupon = BeanUtils.copyBean(dto, Coupon.class);
        this.save(coupon);
        //没有范围限定 直接返回 不需要存入中间表
        if (!dto.getSpecific()) {
            return;
        }
        List<Long> scopes = dto.getScopes();
        if (CollUtils.isEmpty(scopes)) {
            throw new IllegalArgumentException("限定范围不能为空");
        }
        Long couponId = coupon.getId();
        List<CouponScope> couponScope = scopes.stream().
                map(bizId -> new CouponScope().setBizId(bizId).setCouponId(couponId))
                .collect(Collectors.toList());
        scopeService.saveBatch(couponScope);
    }

    /**
     * 分页查询优惠卷
     * @param query
     * @return
     */
    @Override
    public PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query) {
//        封装在CouponQuery里
//        Page<Coupon> couponPage = new Page<>(query.getPageNo(),query.getPageSize());
        Page<Coupon> couponPage = query.getPage(query);
        Integer status = query.getStatus();
        Integer type = query.getType();
        String name = query.getName();
        Page<Coupon> page = lambdaQuery().eq(type != null, Coupon::getDiscountType, type).
                eq(status != null, Coupon::getStatus, status).
                like(StringUtils.isNotBlank(name), Coupon::getName, name).page(couponPage);
//        得到Page<Coupon>中的records集合
        List<Coupon> records = couponPage.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }
//        List<CouponPageVO> couponPageVO = BeanUtils.copyList(records, CouponPageVO.class);
        PageDTO<CouponPageVO> result = PageDTO.convert(page, CouponPageVO.class);
        return result;
    }

    /**
     * 立即发放优惠卷
     * @param dto
     */
    @Transactional
    @Override
    public void distributeCoupon(CouponIssueFormDTO dto) {
        Coupon coupon = this.getById(dto.getId());
        if (coupon==null){
            throw new IllegalArgumentException("优惠卷异常");
        }
        CouponStatus status = coupon.getStatus();
        int value = status.getValue();
        if (value!=1&&value!=5){
            throw new IllegalArgumentException("优惠券状态错误！");
        }

        LocalDateTime now = LocalDateTime.now();
        //        判断时间   立即发放
        Coupon c = BeanUtils.copyBean(dto, Coupon.class);
        if (dto.getIssueEndTime()!=null||now.isAfter(dto.getIssueEndTime())){
            c.setStatus(ISSUING);
            c.setIssueBeginTime(now);
            this.saveOrUpdate(c);
        }else {
            //设置状态为待发放
            c.setStatus(CouponStatus.UN_ISSUE);
        }
        this.updateById(c);
        //todo 生成优惠卷兑换码
        coupon.setTermEndTime(dto.getTermEndTime());
        codeService.asyncGenerateCode(coupon);
    }

    @Override
    //定时发放优惠卷 cron表达式
    //每天0点执行一次
    @Scheduled(cron = "0 0 0 * * *")
    public void regularDistributeCoupons() {
        List<Coupon> unactivatedCoupons = this.lambdaQuery().eq(Coupon::getStatus, CouponStatus.UN_ISSUE.getValue()).list();
        //筛选出发布时间大于当前时间的元素
        List<Coupon> couponList = unactivatedCoupons.stream().filter(coupon -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime issueBeginTime = coupon.getIssueBeginTime();
           return issueBeginTime!=null&& !now.isAfter(issueBeginTime);
        }).collect(Collectors.toList());
        couponList.forEach(coupon -> {
            coupon.setStatus(ISSUING);
            codeService.asyncGenerateCode(coupon);
        });
        this.saveBatch(couponList);
        //todo 生成优惠卷兑换码
    }

    /**
     * 查询发放中的优惠卷
     * @return CouPonVo
     */

    @Override
    public List<CouponVO> queryIssuingCoupon(Long userid) {
        //查询发放中且为手动领取的优惠卷
        List<Coupon> coupons = this.lambdaQuery().eq(Coupon::getStatus, ISSUING).eq(Coupon::getType, ObtainType.PUBLIC).list();
        //代码健壮度判断
        if (CollectionUtils.isEmpty(coupons)){
            return Collections.emptyList();
        }
        //获取优惠卷id列表
        List<Long> ids = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        //获取当前用户用户卷的集合
        List<UserCoupon> userCoupons = userCouponService.lambdaQuery().eq(UserCoupon::getUserId, userid).in(UserCoupon::getCouponId, ids).list();
        //统计当前用户对优惠券的已经领取数量
        Map<Long, Long> issuedMap= userCoupons.stream().collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));
        //统计当前用户对领取优惠卷但却未使用的数量
        Map<Long, Long> unusedMap = userCoupons.stream().filter(c -> c.getStatus() == UserCouponStatus.UNUSED
        ).collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));
        //封装vo对象
        ArrayList<CouponVO> couponVOS = new ArrayList<>(coupons.size());
        coupons.forEach(coupon ->{
            CouponVO couponVO = BeanUtils.copyBean(coupon, CouponVO.class);
            //判断是否可以领取 发布数<总数，每个人领每种优惠卷的数量<每个人最多可以领取的数量
            couponVO.setAvailable( coupon.getIssueNum()<coupon.getTotalNum()&&issuedMap.getOrDefault(coupon.getId(),0L)<coupon.getUserLimit());
           //判断是否可以使用 用户每种优惠卷未使用的是不是>0
            couponVO.setReceived(unusedMap.getOrDefault(coupon.getId(),0L)>0);
            couponVOS.add(couponVO);
        } );
        return couponVOS;
    }

    /**
     * 手动领取优惠
     * @param userid
     * @param couponId
     */
    @Override
    @Transactional
    public int addIssuingCoupon(Long userid, Long couponId) {

        /*
         * - 向中间表中插入数据
         * - 校验优惠券是否存在，不存在无法领取
         * - 校验优惠券的发放时间，是不是正在发放中
         * - 校验优惠券剩余库存是否充足
         * - 校验优惠券的每人限领数量
         * - 已经领取+1
         */
        //获取优惠卷
        Coupon coupon = this.getById(couponId);
        //代码健壮度判断
        if (coupon==null){
            throw new IllegalArgumentException("优惠卷不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issueBeginTime = coupon.getIssueBeginTime();
        LocalDateTime issueEndTime = coupon.getIssueEndTime();
        //判断是否过期或者活动尚未开始
        if (now.isAfter(issueEndTime)||now.isBefore(issueBeginTime)){
            throw new IllegalArgumentException("活动还未开始");
        }
        if (coupon.getStatus()!= ISSUING){
            throw new IllegalArgumentException("优惠卷不是在发放中");
        }
        Integer totalNum = coupon.getTotalNum();
        Integer issueNum = coupon.getIssueNum();
        if (issueNum>=totalNum){
            throw new IllegalArgumentException("优惠卷已经卖完");
        }

        Long count = userCouponService.lambdaQuery().
                eq(UserCoupon::getUserId, userid).
                eq(UserCoupon::getCouponId,couponId).count();
        if (count!=null&&count>=coupon.getUserLimit()){
            throw new BadRequestException("超过领取数量");
        }
        //更新数据
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
        //领取之后优惠卷库存数量+1
        int result = couponMapper.updateRepertory();
        return result;
    }

    /**
     * 兑换码领取优惠卷
     * @param code
     */
    @Override
    @Transactional
    public void receiveRedeemCode(String code) {
        //判断兑换码是否合法
        //判断兑换码是否过期
        //判断库存是否充足?优惠卷发了多少 兑换码就有多少 库存一定充足 不用
        //判断兑换码是否被兑换过
        //新增用户卷信息
        //优惠卷已经卖出数量加
        if (StringUtils.isBlank(code)){
            throw new IllegalArgumentException("兑换码为空");
        }
        Long id = CodeUtil.parseCode(code);
        //判断兑换码是否被兑换过 bitmap判断
        synchronized (id.toString().intern()) {

            boolean consequence = codeService.updateExchangeMark(id, true);
            if (consequence) {
                throw new BadRequestException("兑换码已经被兑换过");
            }
            ExchangeCode exchangeCode = null;
            Long couponId = null;
            Coupon coupon = null;
            long userid = 0; //模拟用户id
            try {
                exchangeCode = codeService.getById(id);
                //获得优惠卷id
                couponId = exchangeCode.getExchangeTargetId();
                coupon = this.getById(couponId);
                Integer userLimit = coupon.getUserLimit();
                //判断是否可以领
                userid = 1;
                Long count = userCouponService.lambdaQuery().
                        eq(UserCoupon::getUserId, userid).
                        eq(UserCoupon::getCouponId, couponId).count();

                if (count >= userLimit) {
                    throw new BadRequestException("领取数量上限");
                }
                LocalDateTime issueBeginTime = coupon.getIssueBeginTime();
                LocalDateTime issueEndTime = coupon.getIssueEndTime();
                LocalDateTime now = LocalDateTime.now();
                //判断是否过期或者活动尚未开始
                if (now.isAfter(issueEndTime) || now.isBefore(issueBeginTime)) {
                    throw new IllegalArgumentException("活动还未开始");
                }
            } catch (RuntimeException e) {
                codeService.updateExchangeMark(id, false);
            }
            //更新库存信息
            couponMapper.updateRepertory();
            //保存数据
            //更新数据到用户表
            userCouponService.saveUserCoupon(userid, couponId);
            //更新兑换码状态
            exchangeCode.setStatus(ExchangeCodeStatus.USED);
            codeService.saveOrUpdate(exchangeCode);
        }

    }

}




