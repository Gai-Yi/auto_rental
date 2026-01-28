package com.coder.rental.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("image")
public class LogoController {
    private static final String preFilePath = "D:\\java\\project\\auto_rental\\src\\main\\resources\\static\\image";

    @GetMapping("show/**")
    public void showHeaderImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();

        String headerImageFile = uri.replace("/image/show", "");

        // 获取图像在服务器磁盘中的全路径
        String fullPath = preFilePath + headerImageFile;

        // 获取输入输出流
        InputStream in = new FileInputStream(fullPath);
        OutputStream out = response.getOutputStream();

        // 读取文件，并将文件写入到浏览器
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }

        // 关闭输入输出流
        out.close();
        in.close();
    }
}
