package com.example.demo.manager.kafka;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@MapperScan(basePackages="com.example.demo.mapper")
public class KafkaAutoConfiguration {


}
