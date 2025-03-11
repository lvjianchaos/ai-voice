package com.example.controller.user;

import com.example.dto.LoginDto;
import com.example.entity.User;
import com.example.result.Result;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.vo.UserLoginVo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/front/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    /*
    * 注册
    * */
    @PostMapping("/register")
    public Result register(@RequestBody LoginDto  loginDto){
        log.info("开始注册");
       if(loginDto.getName()==null||loginDto.getPassword()==null){
           return Result.error("用户名或密码不能为空");
       }
        User u = userService.findUserByName(loginDto.getName());
        if(u!=null){
            return Result.error("用户名已被注册");
        }
        userService.register(loginDto.getName(), loginDto.getPassword());
        return Result.success();

    }
    /*
    * 登录接口
    * */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto){
        log.info("用户登录");
        User user = userService.findUserByName(loginDto.getName());
        if(user==null) return Result.error("用户名不存在");
        if(!user.getPassword().equals(loginDto.getPassword())) return Result.error("密码错误");
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.createJwt(claims);
        UserLoginVo userLoginVo = UserLoginVo.builder()
                .userName(user.getUsername())
                .token(token)
                .build();

       return Result.success(userLoginVo);

    }
    //用户退出
    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }


    @GetMapping("/hello2")
    public Result hello(){

        return Result.success("mmmm");
    }

}

