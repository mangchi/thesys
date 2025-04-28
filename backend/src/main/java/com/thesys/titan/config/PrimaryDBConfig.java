package com.thesys.titan.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.thesys.titan.dao.DAOInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class PrimaryDBConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.primary.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${spring.primary.datasource.hikari.pool-name}")
    private String poolName;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;
    @Value("${spring.datasource.hikari.connection-init-sql}")
    private String connectionInitSql;
    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    private final DBConfig dbConfig;

    private final VaultConfig vaultConfig;

    @Primary
    @Bean(name = "hikariConfig")
    @ConfigurationProperties(prefix = "spring.primary.datasource.hikari")
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setPoolName(poolName);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setConnectionInitSql(connectionInitSql);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setJdbcUrl("jdbc:mysql://" + vaultConfig.getDbHostName() + ":" +
                vaultConfig.getPort() + "/"
                + vaultConfig.getDbName());
        hikariConfig.setUsername(vaultConfig.getDbName());
        hikariConfig.setPassword(vaultConfig.getUserPwd());
        hikariConfig.setAutoCommit(false);
        return hikariConfig;
    }

    @Bean(name = "dataSource")
    @Primary
    HikariDataSource dataSource(@Qualifier("hikariConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig());
    }

    @Bean("daoInterceptor")
    @Primary
    public DAOInterceptor daoInterceptor() {
        return new DAOInterceptor();
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setPlugins(daoInterceptor());
        dbConfig.setSqlSessionFactoryBean(factoryBean, dataSource, applicationContext);

        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "transactionAdvice")
    TransactionInterceptor transactionAdvice(
            @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return dbConfig.getTransactionInterceptor(transactionManager);
    }

    @Primary
    @Bean(name = "transactionAdviceAdvisor")
    Advisor transactionAdviceAdvisor(@Qualifier("transactionAdvice") TransactionInterceptor transactionAdvice) {
        return dbConfig.getAdvisor(transactionAdvice);
    }

}
