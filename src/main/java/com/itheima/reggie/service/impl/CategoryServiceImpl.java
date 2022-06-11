package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.web.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * @Author Vsunks.v
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: Service实现类，在实现其父接口CategoryService的同时，继承Mybatis提供的ServiceImpl类；
 * 泛型分别是对应的Mapper：CategoryMapper和实体类型Category
 * Mybatis提供的ServiceImpl中提供了大量通用的service层操作方法
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    /**
     * 保存，并在保存前检查分类名称
     *
     * @param category
     * @return
     */
    @Override
    public boolean saveWithNameCheck(Category category) {

        // 构建查询条件对象
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();

        // 设置查询条件
        qw.eq(Category::getName, category.getName());

        // 查询，得到对象
        Category ca = this.getOne(qw);

        // 判断对象是否为null，如果存在抛出业务异常
        if (ca != null) {
            throw new BusinessException("分类："+ca.getName() + "已经存在");
        }

        // 保存
        this.save(category);

        return true;
    }
}