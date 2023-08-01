package com.example.demo.mapping;


import com.example.demo.enums.ResultCodeEnum;
import com.example.demo.exception.BizException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapping通用转换
 */
@Component
@Named("TypeConversionWorker")
public class TypeConversionWorker {

    /**
     * 按,分割
     *
     * @param value
     * @return
     */
    @Named("doSplit")
    public List<String> doSplit(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        List<String> list = Arrays.asList(value.split(","));
        return list;

    }

    /**
     * String2Long
     *
     * @param value
     * @return
     */
    @Named("toLong")
    public Long toLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            Date date = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm");
            return date.getTime();
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.CODE_202);
        }
    }



}