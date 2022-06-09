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
import java.time.LocalDateTime;

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
     *
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

            //6. 响应页面登陆成功。
            // 登录成功，需要将员工对象响应到前台，否则前台右上角看不到当前登录的用户名。
            return R.success("登录成功", employee);
        }

        return R.fail("密码错误");
    }


    /**
     * 登出注销
     *
     * @return 操作的结果
     */
    @PostMapping("/logout")
    public R logout() {
        log.info("登出注销");

        // 清理session
        request.getSession().removeAttribute("employee");

        // 响应操作结果
        return R.success("注销成功");
    }


    /**
     * 保存员工。保存前需要补充数据：修改、创建人和时间，默认status、默认密码
     * @param employee
     * @return
     */
    @PostMapping
    public R save(@RequestBody Employee employee) {

        // 1. 对密码进行加密
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);

        // 2. 补全数据（推荐写在service层）
        employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 2.1 获取当前登录用户的id
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);


        // 3.保存员工
        boolean saveRsult = employeeService.save(employee);
        if (saveRsult) {
            return R.success("新增员工成功");
        }
        return R.fail("新增员工失败");

    }

}
