package com.chippy.feign.support.registry;

import cn.hutool.core.lang.ClassScanner;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.feign.support.processor.FeignClientProcessor;
import com.chippy.feign.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 默认Feign请求处理器实现
 *
 * @author: chippy
 * @datetime 2020/12/13 15:31
 */
@Slf4j
@Service
public class DefaultFeignClientProcessorRegistry implements ProcessorRegistry, InitializingBean {

    private ProcessorManager processorManager;
    private String scannerPackages;
    private PathMatcher pathMatcher;

    @Resource
    private List<FeignClientProcessor> feignClientProcessorList;

    public DefaultFeignClientProcessorRegistry() {
        this(ApplicationContextUtil.getDefaultScannerPackage(), new AntPathMatcher());
    }

    public DefaultFeignClientProcessorRegistry(String scannerPackages, PathMatcher pathMatcher) {
        String finalScannerPackages;
        PathMatcher finalPathMatcher;
        if (ObjectsUtil.isEmpty(scannerPackages)) {
            throw new BeanCreationException("构建模板Feign扩展扩展处理注册器异常-[未找到扫描入口]");
        } else {
            finalScannerPackages = scannerPackages;
        }

        if (ObjectsUtil.isEmpty(pathMatcher)) {
            finalPathMatcher = new AntPathMatcher();
        } else {
            finalPathMatcher = pathMatcher;
        }

        this.scannerPackages = finalScannerPackages;
        this.pathMatcher = finalPathMatcher;
        this.processorManager = ProcessorManager.getInstance();
    }

    public void register(String fullPath, FeignClientProcessor feignClientProcessor) {
        processorManager.register(fullPath, feignClientProcessor);
    }

    @Override
    public void afterPropertiesSet() {
        this.init();
    }

    private void init() {
        for (String scannerPackage : this.scannerPackages.split(",")) {
            for (Class<?> clazz : ClassScanner.scanPackage(scannerPackage)) {
                if (this.isFeignClient(clazz)) {
                    this.registerFeignClientProcessor(clazz);
                }
            }
        }
    }

    private boolean isFeignClient(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().contains("FeignClient")) {
                return true;
            }
        }
        return false;
    }

    private void registerFeignClientProcessor(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (this.isRequestAnnotation(postMapping, getMapping, requestMapping, putMapping, patchMapping,
                deleteMapping)) {
                String[] requestPaths =
                    this.resolvePath(postMapping, getMapping, requestMapping, putMapping, patchMapping, deleteMapping);
                final String fullPath = this.getFullPath(clazz, method);
                this.registerFeignClientProcessor(fullPath, requestPaths);
            }
        }
    }

    private String[] resolvePath(PostMapping postMapping, GetMapping getMapping, RequestMapping requestMapping,
        PutMapping putMapping, PatchMapping patchMapping, DeleteMapping deleteMapping) {
        if (null != postMapping) {
            return postMapping.value();
        }
        if (null != getMapping) {
            return getMapping.value();
        }
        if (null != requestMapping) {
            return requestMapping.value();
        }
        if (null != putMapping) {
            return putMapping.value();
        }
        if (null != patchMapping) {
            return patchMapping.value();
        }
        return deleteMapping.value();
    }

    private void registerFeignClientProcessor(String fullPath, String[] requestPaths) {
        for (FeignClientProcessor feignClientProcessor : feignClientProcessorList) {
            final List<String> includePathPatterns = feignClientProcessor.getIncludePathPattern();
            final List<String> excludePathPatterns = feignClientProcessor.getExcludePathPattern();
            if (CollectionUtils.isEmpty(includePathPatterns)) {
                return;
            }

            boolean isMatch = false;
            for (String includePattern : includePathPatterns) {
                for (String requestPath : requestPaths) {
                    if (pathMatcher.match(includePattern, requestPath)) {
                        isMatch = true;
                        break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(excludePathPatterns)) {
                for (String excludePattern : excludePathPatterns) {
                    for (String requestPath : requestPaths) {
                        if (pathMatcher.match(excludePattern, requestPath)) {
                            isMatch = false;
                            break;
                        }
                    }
                }
            }

            if (isMatch) {
                this.register(fullPath, feignClientProcessor);
            }
        }
    }

    private boolean isRequestAnnotation(PostMapping postMapping, GetMapping getMapping, RequestMapping requestMapping,
        PutMapping putMapping, PatchMapping patchMapping, DeleteMapping deleteMapping) {
        return null != postMapping || null != getMapping || null != requestMapping || null != putMapping
            || null != patchMapping || null != deleteMapping;
    }

    private String getFullPath(Class<?> clazz, Method method) {
        return clazz.getName() + method.getName();
    }

}
