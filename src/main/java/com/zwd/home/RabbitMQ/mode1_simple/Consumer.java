package com.zwd.home.RabbitMQ.mode1_simple;

import com.rabbitmq.client.*;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

public class Consumer {
    public static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 消费者消费消息
         * 1.消费的队列名称
         * 2.消费成功之后是否要自动应答(true代表自动应答,false代表手动应答)
         * 3.消费者消费消息的回调(函数式接口)
         * 4.消费者取消消费的回调(函数式接口)
         */
        // 消费消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消息成功消费!内容为:" + new String(message.getBody()));
        };
        // 取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
