package be.kadivnik.iot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:configuration.properties")
@ComponentScan({"be.kadivink.iot"})
public class ApplicationConfig {

}
