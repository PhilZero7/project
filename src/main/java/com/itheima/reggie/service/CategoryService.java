package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @Author Vsunks.v
 * @Blog blog.sunxiaowei.net
 * @Description:
 * Service类，继承Mybatis提供的IService类，泛型为实体类型。
 * 继承该接口，可以简化Service开发
 */
public interface CategoryService extends IService<Category> {}