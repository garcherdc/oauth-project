package com.example.demo.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
@Configuration
public class MailConfig {
    //邮箱通知模板
    @Value("${email.title}")
    private String emailTitle;
    @Value("${en.operation.log.email.file.name:operation_log}")
    private String enOperationLogEmailFileName;


}
