package cn.longyu.conmon.dto;


import lombok.Data;

/**
 * 
 * @TableName admin_user
 */

@Data
public class AdminUserDto {

    private Long id;

    private String username;

    private String password;
}