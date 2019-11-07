package team.educoin.transaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.educoin.transaction.interceptor.JWTAuthenticationInterceptor;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-06-08
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/","/error","/csrf")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/integration/**")
                //放行 swagger2
                . excludePathPatterns("/**.html","/v2/api-docs","/swagger-resources/**","/webjars/**");
    }


    public JWTAuthenticationInterceptor jwtAuthenticationInterceptor(){
        return new JWTAuthenticationInterceptor();
    }
}
