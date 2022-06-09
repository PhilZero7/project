package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author Vsunks.v
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description:
 */
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    // 该成员变量序列化时，会序列化为String类型
    // @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String idNumber; //驼峰命名法 ---> 映射的字段名为 id_number
    private Integer status;  // 0禁用 1 正常

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    // 创建用户id
    private Long createUser;

    // 更新用户id
    private Long updateUser;
}

