package cn.longyu.user.service;

import cn.longyu.conmon.dto.AdminUserDto;
import cn.longyu.user.domain.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【admin_user】的数据库操作Service
* @createDate 2025-07-10 14:46:00
*/
public interface AdminUserService extends IService<AdminUser> {
    AdminUserDto getByUsername(String username);
}
