package cn.longyu.promotion.domain.dto;

import cn.longyu.conmon.utils.BeanUtils;
import cn.longyu.conmon.utils.CollUtils;
import cn.longyu.promotion.domain.po.Coupon;
import cn.longyu.promotion.domain.query.CouponQuery;
import cn.longyu.promotion.domain.vo.CouponPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
//支持链式调用
@Accessors(chain = true)
public class PageDTO<T> {
    private List<T> records;      // 当前页数据
    private long total;           // 总记录数
    private long size;            // 每页大小
    private long current;         // 当前页码

    // 无参构造
    public PageDTO() {
//        返回一个不可变的空列表 避免空指针异常
        this.records = Collections.emptyList();
        this.total = 0;
        this.size = 10;
        this.current = 1;
//
    }

    public PageDTO(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
    }

    // 从 MyBatis-Plus 的 IPage 转换
    public static <T> PageDTO<T> of(IPage<T> iPage) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setRecords(iPage.getRecords());
        pageDTO.setTotal(iPage.getTotal());
        pageDTO.setSize(iPage.getSize());
        pageDTO.setCurrent(iPage.getCurrent());
        return pageDTO;
    }
//    public static <S,T> PageDTO<T> of(IPage<T> iPage,List<S> list) {
//        PageDTO<T> pageDTO = new PageDTO<>();
//        pageDTO.setRecords(list);
//        pageDTO.setTotal(iPage.getTotal());
//        pageDTO.setSize(iPage.getSize());
//        pageDTO.setCurrent(iPage.getCurrent());
//        return pageDTO;
//    }
 public static <T> PageDTO<T> empty(Page<?> page) {
    return new PageDTO<>(Collections.emptyList(),page.getTotal(), page.getSize(),page.getCurrent());
}


    // 转换 IPage 中的实体为 VO
    public static <S, T> PageDTO<T> convert(IPage<S> iPage, Function<S, T> converter) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setRecords(iPage.getRecords().stream()
                .map(converter)
                .collect(Collectors.toList()));
        pageDTO.setTotal(iPage.getTotal());
        pageDTO.setSize(iPage.getSize());
        pageDTO.setCurrent(iPage.getCurrent());
        return pageDTO;
    }

    public static <T, V> PageDTO<V> convert(Page<T> page, Class<V> targetClass) {
        if (page == null) {
            return PageDTO.empty(page); // 返回空分页对象
        }
        /**
         * 使用 Hutool 将 MyBatis-Plus 的 Page 对象转换为自定义的 PageDTO
         */
        List<V> voList=null;
        List<T> records = page.getRecords();
        if (records==null){
            records = Collections.emptyList();
        }else {
             voList = BeanUtils.copyList(records, targetClass);
        }

        // 构建 PageDTO 并设置分页信息
        return new PageDTO<V>()
                .setRecords(voList)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }


}

// 服务层返回自定义 PageDTO

