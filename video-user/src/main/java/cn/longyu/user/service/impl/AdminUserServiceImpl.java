package cn.longyu.user.service.impl;

import cn.longyu.conmon.dto.AdminUserDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.user.domain.AdminUser;
import cn.longyu.user.service.AdminUserService;
import cn.longyu.user.mapper.AdminUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【admin_user】的数据库操作Service实现
* @createDate 2025-07-10 14:46:00
*/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService{

    @Override
    public AdminUserDto getByUsername(String username) {

        AdminUserDto adminUserDto = new AdminUserDto();
        AdminUser userDto = this.lambdaQuery().eq(AdminUser::getUsername, username).one();
        BeanUtils.copyProperties(userDto,adminUserDto);
        return adminUserDto;
    }
}




