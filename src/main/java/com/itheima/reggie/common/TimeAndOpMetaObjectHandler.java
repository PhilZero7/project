package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.reggie.utils.BaseContextUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Vsunks.v
 * @Date 2022/6/9 17:32
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 自动填充类，填充处理器。
 */
@Component
public class TimeAndOpMetaObjectHandler implements MetaObjectHandler {


    /**
     * 当添加一条记录时，如果对一个的实体类的成员变量上有@TableField(fill=xx)
     * 且fil属性的值为FieldFill.INSERT。就会自动调用方法，为实体类对象的成员赋值
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // setValue(name,value) 为某个成员变量赋值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        // 获取当前用户id
        Long employeeId = BaseContextUtil.getCurrentId();

        metaObject.setValue("createUser", employeeId);
        metaObject.setValue("updateUser", employeeId);


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // setValue(name,value) 为某个成员变量赋值
        metaObject.setValue("updateTime", LocalDateTime.now());

        // 获取当前用户id
        Long employeeId = BaseContextUtil.getCurrentId();

        metaObject.setValue("updateUser", employeeId);
    }
}
