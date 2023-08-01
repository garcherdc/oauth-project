package com.example.demo.manager.kafka;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Data
@Slf4j
@Component

public class SystemConfig {

    @Value("${http.proxy.enable:false}")
    private boolean httpProxyEnable;
    @Value("${http.proxy.host:}")
    private String httpProxyHost;
    @Value("${http.proxy.port:0}")
    private int httpProxyPort;
    @Value("${http.proxy.user:}")
    private String httpProxyUser;
    @Value("${http.proxy.password:}")
    private String httpProxyPassword;
    @Value("${http.socket.timeout}")
    private Integer httpSocketTimeout;
    @Value("${http.connet.timeout}")
    private Integer httpConnetTimeout;
    @Value("${http.max.total:400}")
    private Integer maxTotal = 400;
    @Value("${http.max.per.route:400}")
    private Integer maxPerRoute = 400;

    private List<String> roleList;
    private Map<String, String> productRoleMap;

    @Value("${base.url:http://wtTest.com:8088}")

    private String baseUrl;


    @Value("${audit.flow.apply.url:}")

    private String auditFlowApplyUrl;

    @Value("${kafka.bootstrap.servers:}")
    private String kafkaBootstrapServers;

    @Value("${kafka.audit.flow.topic:}")

    private String kafkaAuditFlowTopic;

    @Value("${script:}")

    private String script;



}
