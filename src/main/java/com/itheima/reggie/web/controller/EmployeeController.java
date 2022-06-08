package com.itheima.reggie.web.controller;

import com.itheima.reggie.entity.dto.LoginDto;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 12:01
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 员工控制器类
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;


    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto) {
        log.info("开始登录。用户名：{}；密码：{}", loginDto.getUsername(), loginDto.getPassword());


        return null;
    }

    public static void main(String[] args) {
        String str = DigestUtils.md5DigestAsHex("12345".getBytes());
        str = DigestUtils.md5DigestAsHex(str.getBytes());
        str = DigestUtils.md5DigestAsHex(str.getBytes());
        System.out.println("str = " + str);
    }

}
