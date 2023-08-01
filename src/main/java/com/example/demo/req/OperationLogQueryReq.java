package com.example.demo.req;

import lombok.Data;


import java.util.List;

/**
 * 操作日志表，用于记录用户的操作历史
 */
@Data
public class OperationLogQueryReq {

    /**
     * 操作时间
     */
    private Long startTime;
    /**
     * 操作时间
     */
    private Long endTime;
    /**
     * 用户名
     */
    private List<String> userNames;
    /**
     * 邮箱
     */
    private List<String> emails;
    /**
     * 合作方
     */
    private String partner;
    /**
     * 操作类型
     */
    private List<Integer> operations;
    /**
     * 页数
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 20;
}