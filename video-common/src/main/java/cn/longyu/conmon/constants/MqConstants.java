package cn.longyu.conmon.constants;

public interface MqConstants {
    interface Exchange{
        /*点赞记录有关的交换机*/
        String LIKE_RECORD_EXCHANGE = "like.record.topic";
    }
    interface Key{
        /*点赞的RoutingKey*/
        String LIKED_TIMES_KEY_TEMPLATE = "{}.times.changed";
        /*问答*/
        String QA_LIKED_TIMES_KEY = "QA.times.changed";
        /*笔记*/
        String NOTE_LIKED_TIMES_KEY = "NOTE.times.changed";
    }

    interface Queue{
        //点赞接受消息的队列
        String QA_LIKED_TIME_QUEUE="qa.like.times.queue";
    }

}
