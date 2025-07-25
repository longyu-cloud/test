//package cn.longyu.promotion.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//// 全局配置类
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**") // 对所有 /api 路径生效
//            .allowedOrigins("http://localhost:5173/") // 允许的前端域名
//            .allowedMethods("GET", "POST", "PUT", "DELETE")
//            .allowedHeaders("*")
//            .allowCredentials(true) // 允许 Cookie
//            .maxAge(3600); // 预检请求缓存时间（秒）
//  }
//}