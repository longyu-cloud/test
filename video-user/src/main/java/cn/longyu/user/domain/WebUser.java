package cn.longyu.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName web_user
 */
@TableName(value ="web_user")
@Data
public class WebUser {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String nickName;

    /**
     * 
     */
    private Integer gender;

    /**
     * 
     */
    private String avatarUrl;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String accountName;

    /**
     * 
     */
    private String accountPwd;
}