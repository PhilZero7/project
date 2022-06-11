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
public interface CategoryService extends IService<Category> {
    // 保存，并在保存前检查分类名称
    boolean saveWithNameCheck(Category category);

    // 删除分类，条件：该分类未被使用时
    boolean removeByIdWithoutUsersd(Long id);
}