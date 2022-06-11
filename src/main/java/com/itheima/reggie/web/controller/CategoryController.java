package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

/**
 * @Author Vsunks.v
 * @Date 2022/6/11 9:44
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 分类控制器，包含了菜品分类和套餐分类
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 保存分类
     * @param category 分类信息
     * @return
     *//*
    @PostMapping
    public R add(@RequestBody Category category) {

        log.info("新增分类，信息：{}", category);

        boolean saveRsult = categoryService.save(category);

        if (saveRsult) {
            return R.success("新增成功");
        }
        return R.fail("新增失败");

    }*/

    /**
     * 保存分类，并在保存前检查分类名称
     *
     * @param category 分类信息
     * @return
     */
    @PostMapping
    public R add(@RequestBody Category category) {

        log.info("新增分类，信息：{}", category);

        boolean saveRsult = categoryService.saveWithNameCheck(category);

        if (saveRsult) {
            return R.success("新增成功");
        }
        return R.fail("新增失败");
    }


    /**
     * 分类查询
     * 排序条件：主要条件是sort，次要条件是updateTime
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") Integer currentPage, Integer pageSize) {
        log.info("所有分类分页查询，第{}页，每页{}条", currentPage, pageSize);

        // TODO 下面的逻辑代码，应该写在service层

        // 0. 请求数据合理化设置
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 5;
        }

        // 1. 创建分页查询的page对象
        Page<Category> page = new Page<>(currentPage, pageSize);

        // 2. 创建条件对象，并设置隐含条件
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        // 3. 查询，数据自动封装进Page对象
        categoryService.page(page, qw);

        return R.success("查询成功", page);
    }


    /**
     * 按照id删除
     *
     * @param id
     * @return
     */
    /*@DeleteMapping
    public R deleteById(Long id) {
        log.info("按照id删除，id为：{}", id);

        // 删除
        boolean deleteResut = categoryService.removeById(id);

        if (deleteResut) {

            return R.success("删除成功");
        }
        return R.fail("删除失败");

    }*/


    /**
     * 按照id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R deleteById(Long id) {
        log.info("按照id删除，id为：{}", id);

        // 删除
        boolean deleteResut = categoryService.removeByIdWithoutUsersd(id);

        if (deleteResut) {

            return R.success("删除成功");
        }
        return R.fail("删除失败");

    }
}
