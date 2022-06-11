package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @Author Vsunks.v
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description:
 * Service实现类，在实现其父接口CategoryService的同时，继承Mybatis提供的ServiceImpl类；
 * 泛型分别是对应的Mapper：CategoryMapper和实体类型Category
 * Mybatis提供的ServiceImpl中提供了大量通用的service层操作方法
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {}