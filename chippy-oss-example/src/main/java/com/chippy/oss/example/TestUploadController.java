package com.chippy.oss.example;

import com.chippy.oss.context.OssClientTemplate;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: chippy
 */
@RestController
@RequestMapping("/test/upload")
public class TestUploadController {

    @Resource
    private OssClientTemplate ossClientTemplate;

    @PostMapping("")
    public String testUpload(@RequestParam(value = "file") MultipartFile file) {
        final UploadResult uploadResult = ossClientTemplate.upload(new OssRequestContext() {
            @Override
            public String getClientName() {
                return "ALI";
            }

            @Override
            public String getFileName() {
                return file.getOriginalFilename();
            }

            @Override
            public String getFileDir() {
                return null;
            }

            @Override
            public String getLevelName() {
                return "quanyu-jingqiu";
            }

            @Override
            public Long getFileSize() {
                return file.getSize();
            }

            @Override
            public String getFileType() {
                final String[] split = file.getOriginalFilename().split("\\.");
                return split[1];
            }

            @Override
            public InputStream getFileStream() {
                try {
                    return file.getInputStream();
                } catch (IOException ignore) {
                    return null;
                }
            }
        });
        return uploadResult.getUrl();
    }

}
