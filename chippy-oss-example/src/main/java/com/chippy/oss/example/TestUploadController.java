package com.chippy.oss.example;

import com.chippy.oss.context.GenericOssRequestContextAssembler;
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

/**
 * @author: chippy
 */
@RestController
@RequestMapping("/test/upload")
public class TestUploadController {

    @Resource
    private GenericOssRequestContextAssembler genericRequestContextAssembler;

    @Resource
    private OssClientTemplate ossClientTemplate;

    @PostMapping("")
    public String testUpload(@RequestParam(value = "file") MultipartFile file) {
        final OssRequestContext ossRequestContext;
        try {
            ossRequestContext = genericRequestContextAssembler.assembler(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final UploadResult uploadResult = ossClientTemplate.upload(ossRequestContext);
        return uploadResult.getUrl();
    }

}
