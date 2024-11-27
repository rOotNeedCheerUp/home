package com.zwd.home.RabbitMQ.mq2_ack2_预取值;


import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

public class Producer {
    public static String QUEUE_NAME = "prefetch";

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();

        // 发送消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 10; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕" + message);
        }
    }
}
