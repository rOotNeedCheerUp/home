package com.zwd.home.RabbitMQ.mode4_direct;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 消费者2
public class Consumer02 {
    private static final String QUEUE_NAME = "disk";
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明临时队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列与交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("获得消息:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
