package com.zwd.home.RabbitMQ.mode3_fanout;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 消费者1
public class Consumer01 {
    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);
        // 声明临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列与交换机
        channel.queueBind(queueName, "logs", "");
        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("获得消息:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
