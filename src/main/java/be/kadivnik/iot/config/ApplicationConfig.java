package be.kadivnik.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import be.kadivnik.iot.config.persistence.PersistenceContext;

@Configuration
@PropertySources({
	@PropertySource(value = "classpath:configuration.properties"), 
	@PropertySource(value = "classpath:hibernate.properties")
	})
@ComponentScan({"be.kadivnik.iot"})
@Import({PersistenceContext.class})
public class ApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
}
