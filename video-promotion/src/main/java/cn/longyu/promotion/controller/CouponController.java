package cn.longyu.promotion.controller;


import cn.longyu.conmon.result.Result;
import cn.longyu.promotion.domain.dto.CouponFormDTO;
import cn.longyu.promotion.domain.dto.CouponIssueFormDTO;
import cn.longyu.promotion.domain.dto.PageDTO;
import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.query.CouponQuery;
import cn.longyu.promotion.domain.vo.CouponPageVO;
import cn.longyu.promotion.domain.vo.CouponVO;
import cn.longyu.promotion.service.CouponService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")

public class CouponController {

    /**
     * 新增优惠卷
     */
    @Autowired
    private CouponService service;
    @PostMapping("add")
    public Result saveCoupon(@RequestBody  CouponFormDTO dto){

        service.saveCoupon(dto);
        return Result.ok();
    }
    /**
     * 分页查询优惠卷
     */
    @GetMapping("page")
    public Result<PageDTO<CouponPageVO>> queryCouponByPage(CouponQuery query){
       PageDTO<CouponPageVO> page=service.queryCouponByPage(query);
        return Result.ok(page);
    }
    /**
     * 发放优惠卷
     */
    @PostMapping("distribute")
    //反序列成对象
    public Result distributeCoupon(@RequestBody CouponIssueFormDTO dto){
        service.distributeCoupon(dto);
        return Result.ok();
    }
    /**
     * 查询发放中的优惠卷 前端用户展示
     * 条件：手动领取 优惠卷状态为发放中
     */
    @PutMapping("list")
    public Result<List<CouponVO>> queryIssuingCoupon(Long userid){
        List<CouponVO> list=service.queryIssuingCoupon(userid);
        return Result.ok(list);
    }
    /**
     * param 用户id 优惠卷id
     * 领取优惠卷
     * 将用户的id和优惠卷id
     */
    @PostMapping("add11")
    public Result<Integer> addIssuingCoupon(Long userid ,Long couponId){
        int result = service.addIssuingCoupon(userid, couponId);
        return Result.ok(result);
    }
    /**
     * param 兑换码
     * 领取兑换码
     */
    @PostMapping("code")
    public Result receiveRedeemCode(String code){
        service.receiveRedeemCode(code);
        return Result.ok();
    }
}
