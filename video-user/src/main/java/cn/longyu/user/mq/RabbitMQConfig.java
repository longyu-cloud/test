package cn.longyu.user.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        // 开启确认回调（需要 RabbitMQ 开启 publisher confirms ，在配置文件可设置 spring.rabbitmq.publisher-confirm-type=correlated ）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData!=null) {
                if (ack) {
                    log.info("消息发送成功,消息ID：{}",
                            correlationData.getId());
                } else {
                    log.error("消息发送失败，原因：{}，消息ID：{}",
                            cause,
                            correlationData.getId());
                }
            }
        });

        // 开启返回回调（需要设置 mandatory=true ，让无法路由的消息返回 ）
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息无法路由，消息内容：{}，回复码：{}，回复信息：{}，交换机：{}，路由键：{}",
                    new String(message.getBody()),
                    replyCode,
                    replyText,
                    exchange,
                    routingKey);
        });

        return rabbitTemplate;
    }
}