package com.itheima.reggie.common;

import com.itheima.reggie.web.R;
import com.itheima.reggie.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Vsunks.v
 * @Date 2022/6/9 9:51
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 全局异常处理类
 */
@Slf4j
// @Component // 装配进Spring容器
// 只处理标有下述注解集合中任一一个的类抛出的异常
// 某个类如果标注的是@RestController或者是@Controller注解，抛出了异常，我才会处理，否则不处理
// @RestControllerAdvice = @ControllerAdvice + @ResponeBody
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    // 定义方法，处理具体的异常
    /**
     * 处理BusinessException
     *
     * @param e 对应异常类型的参数；异常发生时，会将异常对象赋值给该参数
     * @return
     */
    @ExceptionHandler(BusinessException.class) // 通过该注解，指定该方法处理哪些异常
    public R handleBusinessException(BusinessException e) {
        // 记录日志
        log.warn(e.getMessage());

        // 提示用户
        return R.fail(e.getMessage());
    }
}
