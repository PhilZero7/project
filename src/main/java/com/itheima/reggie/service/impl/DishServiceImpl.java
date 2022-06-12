package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
 * Spring事务管理中，针对不同类型的事务处理不同。
 * 非编译时异常也被称为非受检异常，这类异常发生时，Spring管理的事务默认不会滚
 * 需要通过rollbackFor = Exception.class 让所有异常（包括非受检异常）发生时事务回滚
 */
@Service
@Slf4j
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;


    @Autowired
    DishMapper dishMapper;

    /**
     * 保存包含口味的菜品
     *
     * @param dishDto 包含口味的菜品
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithFlavor(DishDto dishDto)   {
        // 0. 判断菜品名称是否存在
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(Dish::getName, dishDto.getName());
        Dish dish = getOne(qw);
        if (dish == null) {
            throw new RuntimeException("菜品不可重复");
        }
        // 1. 保存菜品的基本信息
        // 使用mp新增数据，他会自动将对一个记录的id设置会实体对象
        this.save(dishDto);
        // 2. 获取菜品id
        Long dishId = dishDto.getId();
        // 3. 菜品口味设置菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        //TODO着重练习
        flavors = flavors.stream().map((flavor) -> {
            flavor.setDishId(dishId);
            return flavor;
        }).collect(Collectors.toList());
        // 事务 运行时异常（受检异常）  非运行时异常（非受检异常）
        // Spring的事务只会在受检异常发生时才会整体回滚；
        // 非受检异常发生时，其所在事务内异常前之前的数据库操作不会被回滚
		/*
        if (true) {
            throw new SQLIntegrityConstraintViolationException();
        }
        */
        // 4. 调用DishFlavorService保存口味信息
        dishFlavorService.saveBatch(flavors);
        // 5. 组织结果并返回
        return true;
    }

    /**
     * 分页查询菜品，菜品中的分类id替换为分类名称
     * @param page      当前页
     * @param pageSize  页面大小
     * @param name       查询参数
     * @return
     */
    @Override
    public Page<DishDto> pageWithCategoryName(Integer page, Integer pageSize, String name) {

        //1. 构造分页条件对象`page`
        Page<Dish> pageInfo = new Page<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(pageSize);

        //2. 构建查询及排序条件`queryWrapper`
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name).orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //3. 执行分页条件查询
        this.page(pageInfo, queryWrapper);


        // 对象拷贝，忽略recores属性
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //4. 遍历分页查询列表数据，根据分类`ID`查询分类信息，从而获取该菜品的分类名称
        List<DishDto> list = pageInfo.getRecords().stream().map((dish) -> {

            // 创建DishDto对象
            DishDto dishDto = new DishDto();

            //复制相同属性
            BeanUtils.copyProperties(dish, dishDto);

            // 获取分类id
            Long categoryId = dish.getCategoryId();

            // 查询分页名称，并设置进dishDto
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        //5. 封装数据回`Page`并返回`R<page>`
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavors(Long id) {
        // 1. 根据id查询菜品基本信息
        Dish dish = getById(id);

        // 2. 根据菜品id查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        // 3. 准备dishDto对象
        DishDto dishDto = new DishDto();

        // 4. 复制属性
        BeanUtils.copyProperties(dish, dishDto);

        // 5. 设置口味
        dishDto.setFlavors(flavors);

        // 6. 返回数据
        return dishDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithFlavors(DishDto dishDto) {
        // 1. 更新菜品基本信息
        //dishMapper.updateById(dishDto);
        boolean updateRsult = this.updateById(dishDto);
        if (!updateRsult) {
            return updateRsult;
        }

        // 2. 更新菜品口味信息
        // 2.1 删除菜品现有的口味信息
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(qw);

        // 2.2 保存新的菜品口味信息，注意新口味的DishId
        // System.out.println(dishDto.getFlavors());
        // 2.2.1 获取所有口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        // 2.2.2 遍历
        for (DishFlavor flavor : flavors) {
            // 2.2.3为所有口味设置菜品id
            flavor.setDishId();
        }
        /*flavors = flavors.stream().map((flavor) -> {
            // 2.2.3为所有口味设置菜品id
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());*/

        // 2.2.4 持久化至数据库
        boolean saveBatchResult = dishFlavorService.saveBatch(flavors);
        return saveBatchResult;
    }


}