package com.chippy.oss.predicate;

import com.chippy.oss.context.OssRequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件类型限制
 *
 * @author: chippy
 **/
@Service
public class FileTypePredicate extends AbstractPredicate implements InitializingBean {

    private List<String> supportTypeList;

    public List<String> getSupportTypeList() {
        return supportTypeList;
    }

    public void setSupportTypeList(List<String> supportTypeList) {
        this.supportTypeList = supportTypeList;
    }

    @Override
    public boolean test(OssRequestContext ossRequestContext) {
        return this.getSupportTypeList().contains(ossRequestContext.getFileType().trim().toLowerCase());
    }

    @Override
    public String getErrorMsg() {
        return String.format("目前支持文件类型-[%s]", supportTypeList);
    }

    @Override
    public void afterPropertiesSet() {
        this.loadSupportTypeList();
    }

    private void loadSupportTypeList() {
        final List<String> supportTypeList = ossProperties.getTypePredicatePropertiesList().stream()
            .map(typePredicateProperties -> typePredicateProperties.getType().trim().toLowerCase())
            .collect(Collectors.toList());
        this.setSupportTypeList(supportTypeList);
    }

}