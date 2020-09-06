package com.ustglobal.demo.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository;
import org.apache.camel.spi.IdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.sql.DataSource;

@Component
@ConfigurationProperties(prefix = "camel-demo-route")
@Data
@EqualsAndHashCode(callSuper = true)

public class CamelIdempotentRoute extends RouteBuilder {



	@Autowired
	private IdempotentRepository jdbcIdempotentRepository;
	
	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 */
	@Override
	public void configure() throws Exception {

		// @formatter:off
		
		// errorHandler(deadLetterChannel("seda:errorQueue").maximumRedeliveries(5).redeliveryDelay(1000));
		
		//SELECT name FROM person WHERE id=1;
		
		//Table creation script
		/*
		
		CREATE TABLE CAMEL_MESSAGEPROCESSED (
		processorName VARCHAR(255),
		messageId VARCHAR(100),
		createdAt TIMESTAMP,
		UNIQUE(processorName, messageId)
		);
		*/



		from("timer://dbQueryTimer?period=10s")
		.routeId("IDEMPOTENT_CONSUMER_TIMER_ROUTE")
		//setting constant JSON string {"id":1} as body
		.setBody(constant("{\"id\":1}"))
		.unmarshal().json(JsonLibrary.Jackson)
		.log("After unmarshal body:${body}")
		.log("unmarshal body type:${body.class.name}")
		//.to("sql:SELECT version()?dataSource=#dataSource")	
		.to("sql:SELECT name FROM camel.person WHERE id=:#id?dataSource=#primaryDataSource")
		.log(LoggingLevel.INFO,"******Database query executed - body:${body}******")
		.to("sql:SELECT * FROM test.customer?dataSource=#secondaryDataSource")
		.log(LoggingLevel.INFO,"******Secondary Database query executed - body:${body}******")
		.split(body())
		.log("*** STEP 100 - Split body: ${body} ***")
			.idempotentConsumer(simple("${body[name]}"), jdbcIdempotentRepository)
			.log("*** STEP 200 - Idempotent value:${body} ***")
			.end()
		.log("*** STEP 300 - Outside Idempotent, inside split: ${body} ***")
		.end()
		.log("*** STEP 400 - Outside split: ${body} ***")
		;
		
		
		// @formatter:on

	}

}
