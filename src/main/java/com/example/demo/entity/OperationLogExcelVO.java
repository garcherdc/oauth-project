package com.example.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
    * 客户订阅记录表
    */
@Data
public class OperationLogExcelVO {

    /**
     * 操作时间
     */
    @ExcelProperty(value = "Operation Time",order = 1)
    private String operationTime;

    /**
     * 执行操作的用户名
     */
    @ExcelProperty(value = "User Name",order = 2)
    private String username;

    /**
     * 执行操作的用户电子邮件
     */
    @ExcelProperty(value = "Email",order = 3)
    private String email;

    /**
     * 操作类型 0-登陆,1-退出,2-导出,3-开启sso,4-关闭sso,5-修改密码,6-更新登录增强保护,7-审核通过,8-审核拒绝,9-审核终止
     */
    @ExcelProperty(value = "Operation",order = 4)
    private String operation;


}