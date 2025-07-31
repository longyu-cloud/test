package cn.longyu.user.mq;


import cn.longyu.api.dto.remark.LikedTimesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static cn.longyu.conmon.constants.MqConstants.Exchange.LIKE_RECORD_EXCHANGE;
import static cn.longyu.conmon.constants.MqConstants.Key.QA_LIKED_TIMES_KEY;
import static cn.longyu.conmon.constants.MqConstants.Queue.QA_LIKED_TIME_QUEUE;

/**
 * 监听测试
 */
@Slf4j
@Component
public class LikeTimesChangeListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QA_LIKED_TIME_QUEUE, durable = "true"),
            exchange = @Exchange(name = LIKE_RECORD_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = QA_LIKED_TIMES_KEY
    ))
    public void listenReplyLikedTimesChange(LikedTimesDTO dto){
        log.debug("监听到回答或评论{}的点赞数变更:{}", dto.getBizId(), dto.getLikedTimes());
    }
}
