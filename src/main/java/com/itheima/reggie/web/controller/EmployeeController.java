package com.itheima.reggie.web.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.dto.LoginDto;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.web.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R save(@RequestBody Employee employee) {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.println("**********************************");
        System.out.println("**********Controller-save: name = " + name);

        // 3.保存员工
        boolean saveRsult = employeeService.saveWithCheckUserName(employee);
        if (saveRsult) {
            return R.success("新增员工成功");
        }
        return R.fail("新增员工失败");

    }

    /**
     * 分页条件查询
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param name        查询条件
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") Integer currentPage, Integer pageSize, String name) {

        log.info("分页查询，查询第{}页，每页{}条，查询条件：{}", currentPage, pageSize, name == null ? "无" : name);

        // 0. 请求参数健壮性判断
        if (pageSize == null) {
            pageSize = 2;
        }
        if (currentPage == null) {
            currentPage = 1;
        }
        // 1. 组织分页对象
        Page<Employee> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);


        // 2. 调用service分页，会将查询的数据自动存入page对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 2.1 设置查询条件：按照名称模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);

        employeeService.page(page, queryWrapper);

        /*// 获取分页对象中所有员工，挨个请求密码，并重新封装到一个list集合中
        List<Employee> employees = page.getRecords().stream().map((employee) -> {

            // 清空密码
            employee.setPassword(null);
            return employee;
        }).collect(Collectors.toList());

        // 清空过密码的员工集合，设置会分页对象
        page.setRecords(employees);*/

        // 获取所有员工
        List<Employee> employees = page.getRecords();
        for (int i = 0; i < employees.size(); i++) {
            employees.get(i).setPassword(null);
        }

        // 3. 组织数据并响应给用户
        return R.success("查询成功", page);
    }


    /**
     * 修改员工。默认是按需修改
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R update(@RequestBody Employee employee) {
        log.info("修改员工{}", employee);

        //0. 请求参数健壮性判断
        if (employee.getId() != null) {
            // 状态修改参数合理性判断
            if (employee.getStatus() != null && (employee.getStatus() != 0 && employee.getStatus() != 1)) {
                return R.fail("参数有误");
            }
            // TODO 自己完成更多安全性的校验
            // 调用service，更新用户。employeeService.updateById是按需修改的
            boolean updateResult = employeeService.updateById(employee);

            if (updateResult) {

                return R.success("修改成功");
            }
            return R.fail("修改失败");
        }
        return R.fail("参数有误");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询，id为：{}", id);

        if (id != null) {

            Employee employee = employeeService.getById(id);

            // 清空密码
            employee.setPassword(null);

            return R.success("查询成功", employee);
        }
        return R.fail("参数有误！");
    }

}
