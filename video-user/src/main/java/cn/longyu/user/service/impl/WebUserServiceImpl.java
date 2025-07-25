package cn.longyu.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.user.domain.WebUser;
import cn.longyu.user.service.WebUserService;
import cn.longyu.user.mapper.WebUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【web_user】的数据库操作Service实现
* @createDate 2025-07-10 14:58:00
*/
@Service
public class WebUserServiceImpl extends ServiceImpl<WebUserMapper, WebUser>
    implements WebUserService{

}




