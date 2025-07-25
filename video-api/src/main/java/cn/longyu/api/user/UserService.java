package cn.longyu.api.user;

import cn.longyu.conmon.dto.AdminUserDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="user-service",path = "/user")
public interface UserService {
    @GetMapping("getAdminUser")
    AdminUserDto getByUsername(@RequestParam("username")String username);

}
