package com.acuity.rdso.sbe_user;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.driver-class-name}")
  private String jdbcDriverClassName;

  @Value("${spring.datasource.url}")
  private String jdbcUrl;

  @Value("${spring.datasource.username}")
  private String jdbcUserName;

  @Value("${spring.datasource.password}")
  private String jdbcPassword;

  @Bean
  public BasicDataSource basicDataSource() {
    final BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(jdbcDriverClassName);
    ds.setUrl(jdbcUrl);
    ds.setUsername(jdbcUserName);
    ds.setPassword(jdbcPassword);
    return ds;
  }
}
