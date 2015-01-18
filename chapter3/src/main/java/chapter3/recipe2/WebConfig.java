package chapter3.recipe2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages=WebConfig.BASE_PACKAGE_NAME)
public class WebConfig extends WebMvcConfigurerAdapter {
	static final String BASE_PACKAGE_NAME = "chapter3.recipe2";
}
