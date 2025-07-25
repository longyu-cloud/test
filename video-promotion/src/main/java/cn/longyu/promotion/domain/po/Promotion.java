package cn.longyu.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 促销活动，形式多种多样，例如：优惠券
 * @TableName promotion
 */
@TableName(value ="promotion")
@Data
public class Promotion {
    /**
     * 促销活动id
     */
    @TableId
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 促销活动类型：1-优惠券，2-分销
     */
    private Integer type;

    /**
     * 是否是热门活动：true或false，默认false
     */
    private Integer hot;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;
}