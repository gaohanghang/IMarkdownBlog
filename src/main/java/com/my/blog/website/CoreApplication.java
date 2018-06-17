package com.my.blog.website;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @SpringBootApplication 是 @SpringBootConfiguration，@EnableAutoConfiguration以及@ComponentScan
 * @SpringBootConfiguration 这个注解实际上和@Configuration有相同的作用，配备了该注解的类就能够以JavaConfig的方式完成一些配置，可以不再使用XML配置。
 * @ComponentScan ，这个注解完成的是自动扫描的功能
 * @EnableAutoConfiguration 这个注解是让Spring Boot的配置能够如此简化的关键性注解。
 * 使用@MapperScan可以指定要扫描的Mapper类的包的路径
 *
 */
@MapperScan("com.my.blog.website.dao")
@SpringBootApplication
@EnableTransactionManagement // 注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class CoreApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}
