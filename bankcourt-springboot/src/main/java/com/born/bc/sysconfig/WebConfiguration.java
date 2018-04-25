package com.born.bc.sysconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.born.bc.sysconfig.filter.HeaderFilter;

@Configuration
public class WebConfiguration {

	/**
	 * 定义一个headerFilter
	 * @return
	 */
	@Bean
	public HeaderFilter headerFilter() {
		return new HeaderFilter();
	}
	
}
