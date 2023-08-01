package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 7977162948385098188L;
    /**
     * 登录id
     */
    private String loginId;
    /**
    * 用户名
     */
    private String userName;
    /**
    * 邮箱
     */
    private String email;
    /**
     * 合作方编码
     */
    private String selectPartner;
    /**
    * 用户角色列表
     */
    private List<String> roleList = new ArrayList<>();

    /**
     * 合作方列表
     */
    private List<String> accessPartners  = new ArrayList<>();
    /**
     * 客户用户类型：1-超级管理员，2-管理员，3-普通用户
     */
    private Integer type;
    /**
     * 状态 0-冻结 1-启用
     */
    private Integer  status;
    /**
     * 登录来源
     * 参照LoginSourceEnum
     */
    private Integer loginSource;
    /**
     * 密码更新时间 （0-临时密码）
     */
    private Long pwdUpdateTime;
    /**
     * 注册时间
     */
    private Long registerTime;
    /**
     * 选择时区
     */
    private String timeZone;
}
