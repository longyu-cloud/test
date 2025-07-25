package cn.longyu.promotion.domain.query;

import cn.longyu.promotion.domain.po.Coupon;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

@Data
//@ApiModel(description = "分页请求参数")
//lombok提供的对set方法可以进行链式调用的方法
@Accessors(chain = true)
public class PageQuery {
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUM = 1;


//  第几页
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = DEFAULT_PAGE_NUM;

//  每页数量
    @Min(value = 1, message = "每页查询数量不能小于1")
    private Integer pageSize = DEFAULT_PAGE_SIZE;


    public int from(){
        return (pageNo - 1) * pageSize;
    }
    public Page<Coupon> getPage(CouponQuery query){
        Page<Coupon> couponPage = new Page<>(query.getPageNo(),query.getPageSize());
        return couponPage;
    }

}
