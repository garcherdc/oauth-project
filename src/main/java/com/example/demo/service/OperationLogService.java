package com.example.demo.service;


import com.alibaba.excel.EasyExcel;
import com.example.demo.entity.*;
import com.example.demo.enums.ResultCodeEnum;
import com.example.demo.exception.BizException;
import com.example.demo.mapper.OperationLogMapper;
import com.example.demo.mapping.OperationLogMapping;
import com.example.demo.req.OperationLogQueryReq;
import com.example.demo.threadpool.ThreadPoolService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.demo.config.MailConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import util.SessionContext;
import com.example.demo.entity.OperationLogDO;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;
    @Resource
    private OperationLogMapping operationLogMapping;
    @Resource
    private ThreadPoolService threadPoolService;
    @Autowired
    private MailConfig mailConfig;

    public void addOperationLog(OperationLogDTO operationLogDTO) {
        try {
            OperationLogDO operationLogDO = operationLogMapping.toDO(operationLogDTO);
            operationLogMapper.insert(operationLogDO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public PaginationVO<OperationLogVO> list(OperationLogQueryReq operationLogQueryReq) {
        PageHelper.startPage(operationLogQueryReq.getPageNum(), operationLogQueryReq.getPageSize());
        List<OperationLogDO> operationLogDOList = operationLogMapper.queryByCondition(operationLogQueryReq);
        PageInfo<OperationLogDO> operationLogDOPageInfo = new PageInfo<>(operationLogDOList);
        List<OperationLogVO> operationLogVOList = operationLogMapping.toVOList(operationLogDOList);
        PaginationVO<OperationLogVO> resultPageInfo = new PaginationVO<>(operationLogVOList, operationLogDOPageInfo.getTotal(), operationLogDOPageInfo.getPageSize(), operationLogDOPageInfo.getPageNum());
        return resultPageInfo;
    }

    public void export(OperationLogQueryReq operationLogQueryReq, String timeZone) {
        UserInfo currentUser = SessionContext.getCurrentUser();
        currentUser.setTimeZone(timeZone);
        Locale currentLocale = LocaleContextHolder.getLocale();


        if (currentUser == null || StringUtils.isBlank(currentUser.getEmail())) {
            throw new BizException(ResultCodeEnum.CODE_601009);
        }
        threadPoolService.submitTask(new Runnable() {
            @Override
            public void run() {
                try {
                    SessionContext.save(currentUser);
                    LocaleContextHolder.setLocale(currentLocale);
                    List<OperationLogDO> operationLogDOList = operationLogMapper.queryByCondition(operationLogQueryReq);
                    log.info("export operation log size {}", operationLogDOList.size());
                    List<OperationLogExcelVO> operationLogExcelVOS = operationLogMapping.toExcelVOList(operationLogDOList);
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        EasyExcel.write(out, OperationLogExcelVO.class).sheet("Operation Logs").doWrite(operationLogExcelVOS);
                        byte[] bookByteAry = out.toByteArray();
                        try (InputStream fileStream = new ByteArrayInputStream(bookByteAry)) {
                            if (fileStream.available() != 0) {
                                Map<String, InputStream> fileMap = new HashMap<>();
                                String operationLogEmailFileName = mailConfig.getEnOperationLogEmailFileName();
                                StringBuilder fileNameBuilder = new StringBuilder()
                                        .append(operationLogEmailFileName)
                                        .append("_")
                                        .append(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()))
                                        .append(".xlsx");
                                fileMap.put(fileNameBuilder.toString(), fileStream);


                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    SessionContext.clear();
                    LocaleContextHolder.resetLocaleContext();
                }
            }
        });
    }
}
