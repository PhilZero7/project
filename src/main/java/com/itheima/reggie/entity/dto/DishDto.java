package com.itheima.reggie.entity.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto extends Dish {
    // 菜品口味集合
    private List<DishFlavor> flavors = new ArrayList<>();
    // 菜品分类名称
    private String categoryName;
    private Integer copies;

}