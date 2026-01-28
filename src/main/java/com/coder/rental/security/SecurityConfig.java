package com.coder.rental.security;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Resource
    private CustomerUserDetailsService customerUserDetailsService;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailHandler loginFailHandler;

    @Resource
    private CustomerAnonymousEntryPoint customerAnonymousEntryPoint;

    @Resource
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Resource
    private VerifyTokenFilter verifyTokenFilter;

    @Bean
    @Order(1) // 配置执行顺序
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 指定加在哪个过滤器之前执行，明确指定，使用addFilter的话就是交给过滤器自己处理
        http.addFilterBefore(verifyTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 登录前过滤配置
        http.securityMatcher("/**")
//                .passwordManagement()
                .formLogin()                     // 1. 开始表单登录配置
                .loginProcessingUrl("/rental/user/login")  // 登录处理URL
                .successHandler(loginSuccessHandler)       // 成功处理器
                .failureHandler(loginFailHandler)          // 失败处理器
                .and()                          // 返回 HttpSecurity 对象，继续配置
                .sessionManagement()            // 2. 开始会话管理配置
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 无状态，不保存session状态，因为是前后端分离的系统，使用token，不需要session
                .and()                          // 返回 HttpSecurity 对象
                .authorizeHttpRequests()        // 3. 开始授权配置
                .requestMatchers("/rental/user/login", "/image/show/**").permitAll() // 指定特定的地址允许所有人访问（不需要认证）
                .anyRequest().authenticated()  // 其他所有请求都需要认证
                .and()                          // 返回 HttpSecurity 对象
                .exceptionHandling()            // 4. 开始异常处理配置
                .authenticationEntryPoint(customerAnonymousEntryPoint)  // 没登录访问受保护的资源的处理
                .accessDeniedHandler(customerAccessDeniedHandler)       // 登录了但是权限不够的处理
                .and()                          // 返回 HttpSecurity 对象
                .cors()                         // 5. 允许跨域
                .and()                          // 返回 HttpSecurity 对象
                .csrf().disable()               // 6. 禁用 CSRF 保护，因为需要Token，所以要关闭这个
                .userDetailsService(customerUserDetailsService);  // 7. 设置用户详情服务

        return http.build();
    }
}
