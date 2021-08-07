package com.chippy.oss.example;

import com.chippy.oss.context.GenericOssClientTemplate;
import com.chippy.oss.context.UploadResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author: chippy
 */
@RestController
@RequestMapping("/test/upload")
public class TestUploadController {

    @Resource
    private GenericOssClientTemplate ossClientTemplate;

    @PostMapping("")
    public String testUpload(@RequestParam(value = "file") MultipartFile file) {
        final UploadResult uploadResult;
        try {
            uploadResult = ossClientTemplate.upload(file, "ALI", "quanyu-jingqiu");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadResult.getUrl();
    }

}
