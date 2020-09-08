package com.ustglobal.configuration;

import javax.sql.DataSource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository;
import org.apache.camel.spi.IdempotentRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "camel-demo-route")
@Data
//@EqualsAndHashCode(callSuper = true)
public class DatabaseConfiguration {

	private String interfaceName;

	@Primary
	@Bean(name = "primaryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	//There should be only one primary DataSource
	@Bean(name = "secondaryDataSource")
	@ConfigurationProperties(prefix = "secondary.datasource")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public IdempotentRepository idempotentRepository()
	{
		JdbcMessageIdRepository jdbcMessageIdRepository = new JdbcMessageIdRepository(primaryDataSource(),interfaceName);
//		jdbcMessageIdRepository.setInsertString();
		return jdbcMessageIdRepository;
	}

}
