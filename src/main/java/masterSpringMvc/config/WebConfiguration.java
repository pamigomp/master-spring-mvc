package masterSpringMvc.config;

import java.time.LocalDate;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import masterSpringMvc.USLocalDateFormatter.USLocalDateFormatter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldType(LocalDate.class, new USLocalDateFormatter());
	}
}
