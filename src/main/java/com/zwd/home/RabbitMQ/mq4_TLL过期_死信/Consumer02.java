package com.zwd.home.RabbitMQ.mq4_TLL过期_死信;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

public class Consumer02 {
    private static final String DEAD_EXCHANGE = "dead_exchange";    // 死信交换机名称
    private static final String DEAD_QUEUE = "dead_queue";  // 死信队列名称

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 声明死信交换机(类型都为DIRECT)
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定队列与交换机,设置其间的路由key
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        // 消费消息
        System.out.println("Consumer02等待接受死信队列的消息......");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            System.out.println("Consumer02接收到死信队列的消息:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(DEAD_QUEUE, deliverCallback, cancelCallback);
    }
}