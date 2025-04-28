package com.thesys.titan.config;

import javax.sql.DataSource;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import java.io.IOException;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.List;

@Component
public class DBConfig {
	private static final String AOP_TRANSACTION_METHOD_NAME = "*";
	private static final String AOP_TRANSACTION_EXPRESSION = "execution(* *..*ServiceImpl.*(..))";

	public void setSqlSessionFactoryBean(SqlSessionFactoryBean factoryBean, DataSource dataSource,
			ApplicationContext applicationContext) throws IOException {
		factoryBean.setDataSource(dataSource);
		factoryBean.setTypeAliasesPackage(
				"com.thesys.titan.**.vo com.thesys.titan.**.dto");
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		factoryBean.setConfiguration(configuration);
	}

	public TransactionInterceptor getTransactionInterceptor(PlatformTransactionManager transactionManager) {
		List<RollbackRuleAttribute> rollbackRules = Collections
				.singletonList(new RollbackRuleAttribute(Exception.class));

		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionManager(transactionManager);
		interceptor.setTransactionAttributeSource(attributeSource);

		return interceptor;
	}

	public Advisor getAdvisor(TransactionInterceptor transactionInterceptor) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_TRANSACTION_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, transactionInterceptor);
	}

}
