package com.BjpTracking.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.BjpTracking")
@PropertySources({
@PropertySource("classpath:AppConfig.properties"),
@PropertySource("classpath:Queries.properties")
})
public class AppConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired 
	Environment enviromnet; 
	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");		
		dataSource.setUrl(enviromnet.getProperty("db"));
		dataSource.setUsername(enviromnet.getProperty("dbUser"));
		dataSource.setPassword(enviromnet.getProperty("password"));
		
		
//		dataSource.setUrl("jdbc:mysql://127.2.194.2:3306/bjp_tracking");
//		dataSource.setUsername("adminruSVaeC");
//		dataSource.setPassword("AFhYFncSIjBK");
//		
		
		return dataSource;
	}
	
	
	@Bean
    public MultipartResolver multipartResolver() {		
        return new CommonsMultipartResolver();        
    }
	
	
	
}