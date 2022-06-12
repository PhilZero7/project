package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("保存菜品，信息如下：{}", dishDto.toString());
        boolean saveResult = dishService.saveWithFlavor(dishDto);
        if (saveResult) {
            return R.success("新增菜品成功");
        }
        return R.fail("新增菜品失败");
    }

    /**
     * 分页查询
     *
     * @param page     当前页
     * @param pageSize 页面大小
     * @param name     查询条件
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name) {
        log.info("菜品分页查询，查询第{}页，每页显示{}行，查询条件：{}", page, pageSize, name);
        Page<DishDto> dishDtoPage = dishService.pageWithCategoryName(page, pageSize, name);

        return R.success("分页查询成功", dishDtoPage);
    }

    /**
     * 根据菜品id，得到DishDto后返回
     *
     * @param id 菜品id
     * @return dishDto 菜品DTO对象，包含菜品基本信息 + 菜品口味信息
     */
    @GetMapping("/{id}")
    public R<Dish> getByIdWithFlavor(@PathVariable Long id) {
        log.info("根据菜品id，查询菜品信息（基本信息、菜品口味）。菜品ID：{}", id);

        // id非空判断
        if (id != null) {
            DishDto dishDto = dishService.getByIdWithFlavors(id);
            if (dishDto != null) {
                return R.success("查询成功", dishDto);
            }

            return R.fail("查询失败");
        }
        return R.fail("参数有误");
    }

    /**
     * 修改菜品信息，包含口味、菜品分类
     * @param dishDto 包含口味、菜品分类的菜品信息
     * @return
     */
    @PutMapping
    public R updateWithFlavors(@RequestBody DishDto dishDto) {
        log.info("修改菜品，包含口味和菜品分类。参数：{}", dishDto.toString());
        boolean updateResult = dishService.updateWithFlavors(dishDto);
        if (updateResult) {
            System.out.println("aaaa");
            return R.success("修改菜品成功");
        }
        return R.fail("修改菜品失败");




    }

    @PostMapping
    public R xiugai(@RequestBody DishDto dishDto){
        log.info("修改菜品，包含口味{}",dishDto);
        boolean updataResult = dishService.updateWithFlavors(dishDto);

        if (updataResult) {
            R.success("cheng",dishDto);
        }
        return R.fail("cuowu");


    }

}    