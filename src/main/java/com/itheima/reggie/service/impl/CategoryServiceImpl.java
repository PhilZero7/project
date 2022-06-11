package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.web.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Vsunks.v
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: Service实现类，在实现其父接口CategoryService的同时，继承Mybatis提供的ServiceImpl类；
 * 泛型分别是对应的Mapper：CategoryMapper和实体类型Category
 * Mybatis提供的ServiceImpl中提供了大量通用的service层操作方法
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    // 注入菜品service
    @Autowired
    DishService dishService;

    // 注入套餐service
    @Autowired
    SetmealService setmealService;

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
            throw new BusinessException("分类：" + ca.getName() + "已经存在");
        }

        // 保存
        this.save(category);

        return true;
    }

    /**
     * 删除分类，条件：该分类未被使用时
     *
     * @param id 被删除的id
     * @return
     */
    @Override
    public boolean removeByIdWithoutUsersd(Long id) {

        //TODO 注意：这里不区分分类类型，性能更好。
        // 因为这样做，最少查一次数据库；如果要区分分类类型，需要两次数据库查询

        // 检查该分类是否在被菜品使用
        LambdaQueryWrapper<Dish> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(id != null, Dish::getCategoryId, id);
        List<Dish> dishes = dishService.list(qw1);

        // 如果该菜品分类在使用，就抛业务异常
        if (dishes != null && dishes.size() > 0) {
            throw new BusinessException("该菜品分类在使用，禁止删除！");
            // 这里手动抛出了异常，后面代码就不会执行了
        }


        // 检查该分类是否在被套餐使用
        LambdaQueryWrapper<Setmeal> qw2 = new LambdaQueryWrapper<>();
        qw2.eq(id != null, Setmeal::getCategoryId, id);
        List<Setmeal> setmeals = setmealService.list(qw2);

        // 如果该菜品分类在使用，就抛业务异常
        if (setmeals != null && setmeals.size() > 0) {
            throw new BusinessException("该套餐分类在使用，禁止删除！");
            // 这里手动抛出了异常，后面代码就不会执行了
        }

        // 都没有使用，才删除
        this.removeById(id);

        return true;
    }
}