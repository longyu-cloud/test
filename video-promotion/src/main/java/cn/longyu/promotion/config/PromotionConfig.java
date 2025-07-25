package cn.longyu.promotion.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class PromotionConfig {

        /**
         * 使用spring的异步调用
         * 使用spring的线程池进行异步调用
         * @return
         */
        @Bean
        public Executor generateExchangeCodeExecutor(){
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            //核心线程池大小
            executor.setCorePoolSize(2);
            //最大线程池大小
            executor.setMaxPoolSize(5);
            //队列大小
            executor.setQueueCapacity(200);
            //线程名称
            executor.setThreadNamePrefix("exchange-code-handler-");
            //拒绝策略   如果线程满了 就进行同步调用
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            //初始化
            executor.initialize();
            return executor;
        }
    }

