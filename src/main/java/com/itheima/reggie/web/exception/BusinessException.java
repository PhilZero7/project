package com.itheima.reggie.web.exception;

/**
 * @Author Vsunks.v
 * @Date 2022/6/9 9:38
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description:
 * 统一业务异常类，重写两个构造方法，不需要手动编写实现。
 */
public class BusinessException  extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
