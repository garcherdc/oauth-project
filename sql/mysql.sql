ALTER TABLE `partner_user` drop COLUMN `partner`;
ALTER TABLE `partner_user` ADD COLUMN `pwd_is_temp` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为临时密码 0-非临时密码，1-临时密码';
ALTER TABLE `partner_user` ADD COLUMN `pwd_update_time` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '密码更新时间';
UPDATE partner_user SET pwd_update_time = UNIX_TIMESTAMP()*1000;

CREATE TABLE `operation_log` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `user_name` varchar(50) NOT NULL COMMENT '执行操作的用户姓名',
                                 `email` varchar(50) NULL COMMENT '执行操作的用户电子邮件',
                                 `partner` varchar(255) NOT NULL COMMENT '合作方',
                                 `operation` tinyint(1) NOT NULL COMMENT '操作类型 0-登陆,1-退出,2-导出,3-开启sso,4-关闭sso,6-更新登录增强保护,7-审核通过,8-审核拒绝,9-审核终止,10-关闭MFA,11-开启智能MFA,12-开启强制MFA',
                                 `operation_time` bigint(20) NOT NULL COMMENT '操作日期时间',
                                 `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_operation_time` (`operation_time`),
                                 KEY `idx_user_name` (`user_name`),
                                 KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7044 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表，用于记录用户的操作历史';