package com.itheima.reggie.web.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.dto.LoginDto;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Vsunks.v
 * @Date 2022/6/8 12:01
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 员工控制器类
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    @Autowired
    HttpServletRequest request;

/**
 * 登录，验证用户名、密码、用户是否禁用。
 * @param loginDto 包含用户名和密码的实体
 * @return 是否登录成功，及对应的提示
 */
@PostMapping("/login")
public R login(@RequestBody LoginDto loginDto) {
    String password = loginDto.getPassword();
    log.info("开始登录。用户名：{}；密码：{}", loginDto.getUsername(), password);

    // StringUtils导commons的包，而非SpringFramwork的
    if (StringUtils.isNotBlank(password)) {
        //1. 加密提交的`password`，得到密文
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. 判断员工是否存在
        // 2.1 根据用户名查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(loginDto.getUsername() != null, Employee::getUsername,
                loginDto.getUsername());
        Employee employee = employeeService.getOne(queryWrapper);

        // 2.2 判断查询到的员工是否为空
        if (employee == null) {
            return R.fail("用户不存在");
        }

        //3. 判断密码是否正确
        if (!pwd.equals(employee.getPassword())) {
            return R.fail("密码错误");
        }

        //4. 判断员工是否被禁用
        if (0 == employee.getStatus()) {
            return R.fail("都离职了，还来干嘛！");
        }

        //5. 登陆成功，把员工`ID`存入`session`
        request.getSession().setAttribute("employee", employee.getId());

        //6. 响应页面登陆成功
        return R.success("登录成功",employee);
    }

    return R.fail("密码错误");
}


}
