package com.sourcecode.malls.admin.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@EnableTransactionManagement
@EntityScan(basePackages = "com.sourcecode.malls.admin.domain")
@EnableJpaRepositories(basePackages = "com.sourcecode.malls.admin.repository.jpa.impl")
public class DataSourceConfig {
	@Bean
	@ConfigurationProperties("spring.datasource.druid")
	public DataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}
}
