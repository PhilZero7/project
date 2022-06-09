package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.utils.BaseContextUtil;
import com.itheima.reggie.web.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 11:56
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 员工业务层实现类
 * 自定义实现类要
 * 1. 实现业务层接口EmployeeService
 * 2. 继承自Mybatis提供的通用实现类ServiceImpl。
 * 该类需要制定两个泛型，分别是对应的Mapper和mode
 * 注意：
 * 要先继承，后实现
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


    /**
     * 保存用户，并在保存前检查用户名是否存在
     * 不存在就补充数据：修改、创建人和时间，默认status、默认密码，后保存
     *
     * @param employee
     * @return
     */
    @Override
    public boolean saveWithCheckUserName(Employee employee) {

        // 验证是否在同一个线程中
        /*Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.println("**********************************");
        System.out.println("**********Service-savexx: name = " + name);*/


        /* 完整思路步骤如下：
        	1.判断用户名是否存在
        		1.1 根据用户名查询
        		1.2 判断用户是否存在，存在就抛出业务异常并给出提示信息
        	2. 如果用户不存在，就开始补全数据
            	2.1 设置默认密码123456并加密
            	2.2 补全数据时间相关数据
            	2.3 获取当前登录用户的id，并填充进employee相应属性
            3. 保存用户
        */

        // 1.判断用户名是否存在
        // 1.1 根据用户名查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        String username = employee.getUsername();
        queryWrapper.eq(StringUtils.isNotBlank(username), Employee::getUsername,
                username);
        Employee em = getOne(queryWrapper);
        // 1.2判断用户是否存在，存在就抛出业务异常并给出提示信息
        if (em != null) {
            throw new BusinessException("用户名" + username + "已经存在");

            // 这里不再需要return结束
            // 因为我们没有try catch 抛出异常之后的代码不会继续执行
        }

        // 2. 如果用户不存在，就开始补全数据
        // 2.1 设置默认密码123456并加密
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);

        // 2.2. 补全数据时间相关数据
        employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 2.3 获取当前登录用户的id，并填充进employee相应属性
        //Long employeeId = (Long) request.getSession().getAttribute("employee");

        // 通过线程容器对象，获取绑定在该线程上的用户id
        Long employeeId = BaseContextUtil.getCurrentId();
        System.out.println("service employeeId = " + employeeId);
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);

        // 3. 保存用户
        this.save(employee);

        return true;
    }
}
