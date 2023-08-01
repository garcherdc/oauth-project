package com.example.demo.entity;

import lombok.Data;


/**
 * 操作日志表，用于记录用户的操作历史
 */
@Data
public class OperationLogVO {
    /**
     * ID
     */
    private Long id;

    /**
     * 执行操作的用户名
     */
    private String username;

    /**
     * 执行操作的用户电子邮件
     */
    private String email;

    /**
     * 操作类型 0-登陆,1-退出,2-导出,3-开启sso,4-关闭sso,5-审核,6-修改密码
     */
    private String operation;
    /**
     * 操作时间
     */
    private Long operationTime;

}