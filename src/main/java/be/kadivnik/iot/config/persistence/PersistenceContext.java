package be.kadivnik.iot.config.persistence;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"be.kadivnik.iot.persistence.dao"},
        entityManagerFactoryRef = "iotEntityManagerFactory",
        transactionManagerRef = "iotTransactionManager")
public class PersistenceContext {

    @Bean(name = "iotTransactionManager")
    @DependsOn("iotEntityManagerFactory")
    public JpaTransactionManager transactionManager(@Qualifier(value = "iotEntityManagerFactory")EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
    
    @Bean(name = "iotEntityManagerFactory")
    @DependsOn(value = "iotDataSource")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(value = "iotDataSource")DataSource dataSource, Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("be.kadivnik.iot.entity");
        entityManagerFactoryBean.setJpaProperties(getJpaProperties(env));
 
        return entityManagerFactoryBean;
    }

	private Properties getJpaProperties(Environment env) {
		Properties jpaProperties = new Properties();

		jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));
        jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		
        return jpaProperties;
	}
    
	@Bean(name = "iotDataSource", destroyMethod = "close")
    public DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();

        dataSourceConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("db.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("db.password"));
 
        return new HikariDataSource(dataSourceConfig);
    }

	@Bean(name = "h2WebServer", initMethod="start", destroyMethod="stop")
	public org.h2.tools.Server h2WebServer() throws SQLException {
	    return org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	}


	@Bean(initMethod="start", destroyMethod="stop")
	@DependsOn(value = "h2WebServer")
	public org.h2.tools.Server h2Server() throws SQLException {
	    return org.h2.tools.Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}
}
