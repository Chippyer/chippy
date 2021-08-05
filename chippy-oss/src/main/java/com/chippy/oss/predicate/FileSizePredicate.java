package com.chippy.oss.predicate;

import com.chippy.oss.context.OssRequestContext;
import org.springframework.stereotype.Service;

/**
 * 文件大小限制
 *
 * @author: chippy
 **/
@Service
public class FileSizePredicate implements OssPredicate {

    @Override
    public boolean test(OssRequestContext ossRequestContext) {
        return false;
    }

}