package com.thesys.titan.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.thesys.titan.dao.DAOInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class SecondaryDBConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.secondary.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${spring.secondary.datasource.hikari.pool-name}")
    private String poolName;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;
    @Value("${spring.datasource.hikari.connection-init-sql}")
    private String connectionInitSql;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    private final DBConfig dbConfig;

    private final VaultConfig vaultConfig;

    @Bean(name = "secondaryHikariConfig")
    @ConfigurationProperties(prefix = "spring.secondary.datasource.hikari")
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setPoolName(poolName);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setConnectionInitSql(connectionInitSql);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://" + vaultConfig.getSecondaryDbHostName() + ":" +
                        vaultConfig.getPort() + "/"
                        + vaultConfig.getSecondaryDbName());
        hikariConfig.setUsername(vaultConfig.getDbUser());
        hikariConfig.setPassword(vaultConfig.getUserPwd());
        hikariConfig.setAutoCommit(false);
        return hikariConfig;
    }

    @Bean(name = "secondaryDataSource")
    HikariDataSource dataSource(@Qualifier("secondaryHikariConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig());
    }

    @Bean("secondaryDaoInterceptor")
    public DAOInterceptor secondaryDaoInterceptor() {
        return new DAOInterceptor();
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setPlugins(secondaryDaoInterceptor());
        dbConfig.setSqlSessionFactoryBean(factoryBean, dataSource, applicationContext);
        return factoryBean.getObject();
    }

    @Bean(name = "secondarySqlSession")
    public SqlSessionTemplate sqlSession(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "secondaryTransactionAdvice")
    TransactionInterceptor transactionAdvice(
            @Qualifier("secondaryTransactionManager") PlatformTransactionManager transactionManager) {
        return dbConfig.getTransactionInterceptor(transactionManager);
    }

    @Bean(name = "secondarytransactionAdviceAdvisor")
    Advisor transactionAdviceAdvisor(
            @Qualifier("secondaryTransactionAdvice") TransactionInterceptor transactionAdvice) {
        return dbConfig.getAdvisor(transactionAdvice);
    }

}
