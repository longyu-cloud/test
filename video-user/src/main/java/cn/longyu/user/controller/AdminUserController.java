package cn.longyu.user.controller;

import cn.longyu.conmon.dto.AdminUserDto;
import cn.longyu.conmon.result.Result;
import cn.longyu.user.domain.AdminUser;
import cn.longyu.user.service.AdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("regist")
    public Result regist(@RequestBody AdminUser user){
        String username = user.getUsername();
        LambdaQueryWrapper<AdminUser>  wrapper = new LambdaQueryWrapper<>();
        AdminUser adminUser = adminUserService.lambdaQuery().eq(AdminUser::getUsername, username).one();
        if (adminUser!=null){
            adminUserService.saveOrUpdate(user);
            return Result.ok();
        }else return  Result.fail();
    }
    @GetMapping("getAdminUser")
    public AdminUserDto getByUsername(@RequestParam String username){
        return adminUserService.getByUsername(username);
    }

}
