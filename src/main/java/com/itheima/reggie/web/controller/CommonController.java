package com.itheima.reggie.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author Vsunks.v
 * @Date 2022/6/11 15:31
 * @Blog blog.sunxiaowei.net/996.mba
 * @Description: 处理文件上传下载的控制器
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 处理上传请求
     *
     */
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        System.out.println("file = " + file);
        String originalFilename = file.getOriginalFilename();

        file.transferTo(new File("D:\\"+originalFilename));
        System.out.println("file = ");

    }
}
