package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);

    Page<DishDto> pageWithCategoryName(Integer page, Integer pageSize, String name);

    DishDto getByIdWithFlavors(Long id);

    boolean updateWithFlavors(DishDto dishDto);
}