package com.itheima.reggie.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 12:18
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 登录的Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
