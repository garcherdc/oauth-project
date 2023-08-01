package com.example.demo.web;


import com.example.demo.entity.*;
import com.example.demo.req.OperationLogQueryReq;
import com.example.demo.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.CommonUtil;
import util.Constants;
import util.RespUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/portal/operationLog")
@Slf4j
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 操作类型列表
     *
     * @return
     */
//    @GetMapping("/types")
//    public RespVO<List<OperationTypeVO>> types() {
//        List<OperationTypeVO> result = new ArrayList<>();
//        for (OperationEnum operationEnum : OperationEnum.values()) {
//            OperationTypeVO operationTypeVO = new OperationTypeVO();
//            operationTypeVO.setCode(operationEnum.getCode());
//            operationTypeVO.setDisplayName(LocaleUtil.get(operationEnum.getDisplayCode()));
//            result.add(operationTypeVO);
//        }
//        return RespUtil.result(result);
//    }

    /**
     * 查询操作日志
     *
     * @param operationLogQueryReq
     * @return
     */
    @PostMapping("/list")
    public RespVO<PaginationVO<OperationLogVO>> list(HttpServletRequest req, @RequestBody OperationLogQueryReq operationLogQueryReq) {
        UserInfo userInfo = (UserInfo) req.getSession().getAttribute(Constants.SESSION_USER_INFO);
        operationLogQueryReq.setPartner(userInfo.getSelectPartner());
        return RespUtil.result(operationLogService.list(operationLogQueryReq));
    }

    /**
     * 导出操作日志
     *
     * @param operationLogQueryReq
     * @return
     */
    @PostMapping("export")
//    @LogRecord(success = "导出", type = "2", bizNo = "")
    public RespVO export( @RequestBody OperationLogQueryReq operationLogQueryReq, HttpServletRequest request) {
        String timeZone = CommonUtil.getRequestTimeZone(request);
        operationLogService.export(operationLogQueryReq,timeZone);
        return RespUtil.getSUCCESS();
    }
    @GetMapping("/test")
//    @LogRecord(success = "导出", type = "2", bizNo = "")
    public RespVO test() {
        OperationLogDTO operationLogDTO = new OperationLogDTO();
        operationLogDTO.setUsername("1");
        operationLogDTO.setPartner("test");
        operationLogDTO.setOperation(0);
        operationLogDTO.setOperationTime(System.currentTimeMillis());
        operationLogDTO.setPartner("test");
        operationLogService.addOperationLog(operationLogDTO);
        return RespUtil.getSUCCESS();
    }

}
