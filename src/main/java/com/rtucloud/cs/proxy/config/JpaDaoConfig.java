package com.rtucloud.cs.proxy.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("com.rtucloud.cs.proxy.dao.repository")
@EntityScan("com.rtucloud.cs.proxy.dao.entity")
@EnableTransactionManagement
public class JpaDaoConfig {
}
