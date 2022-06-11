package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Employee;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 11:55
 * @Blog blog.sunxiaowei.net
 * @Description: 员工业务接口
 * 让该接口继承MyBatisPlus 的通用接口IService<T>
 *     泛型为实体类型
 *  结果：service层通用的增删改查不需要自己写了
 */
public interface EmployeeService extends IService<Employee> {
     // 保存用户，并在保存前检查用户名是否存在
    boolean saveWithCheckUserName(Employee employee);

}
