package com.example.config;

import com.example.interceptor.JwtTokenUserInterceptor;
import com.example.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@Slf4j
public class WebMvcConfiguration  implements WebMvcConfigurer {
    @Autowired
    JwtTokenUserInterceptor jwtTokenUserInterceptor;

  /*public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login","/user/register");
    }*/
    @Override
 public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
      log.info("扩展消息转换器");
      //创建一个消息转换器
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      //需要为消息转换器设置一个对象转换器
      converter.setObjectMapper(new JacksonObjectMapper());
      converters.add(0,converter);
  }

}
