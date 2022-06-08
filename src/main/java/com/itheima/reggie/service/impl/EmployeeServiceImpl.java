package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 11:56
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 员工业务层实现类
 * 自定义实现类要
 *  1. 实现业务层接口EmployeeService
 *  2. 继承自Mybatis提供的通用实现类ServiceImpl。
 *      该类需要制定两个泛型，分别是对应的Mapper和mode
 * 注意：
 *    要先继承，后实现
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
