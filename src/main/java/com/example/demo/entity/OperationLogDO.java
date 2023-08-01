package com.example.demo.entity;

import lombok.Data;


/**
 * 操作日志表，用于记录用户的操作历史
 */
@Data
public class OperationLogDO {
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
     * 合作方
     */
    private String partner;

    /**
     * 操作类型 0-登陆,1-退出,2-导出,3-开启sso,4-关闭sso,6-更新登录增强保护,7-审核通过,8-审核拒绝,9-审核终止,10-关闭MFA,11-开启智能MFA,12-开启强制MFA
     */
    private Integer operation;
    /**
     * 操作时间
     */
    private Long operationTime;
}