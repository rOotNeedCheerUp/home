package com.zwd.home.RabbitMQ.mode2_work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 消费者1
public class Consumer01 {
    public static String QUEUE_NAME = "work";

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消息成功消费!内容为:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
