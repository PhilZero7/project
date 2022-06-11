package com.itheima.reggie.web.controller;

import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

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
     */
    @PostMapping
    public R add(@RequestBody Category category) {

        log.info("新增分类，信息：{}", category);

        boolean saveRsult = categoryService.save(category);

        if (saveRsult) {
            return R.success("新增成功");
        }
        return R.fail("新增失败");

    }


}
