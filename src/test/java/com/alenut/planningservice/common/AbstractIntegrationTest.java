package com.alenut.planningservice.common;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import com.alenut.planningservice.config.ObjectMapperConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = {"classpath:/sql/clean-data.sql"})
@Import(ObjectMapperConfiguration.class)
public abstract class AbstractIntegrationTest {

}
