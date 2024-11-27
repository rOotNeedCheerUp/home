package com.zwd.home.RabbitMQ.mq4_TLL过期_死信;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";//普通交换机名称

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置消息TTL时间为10s
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        //发送十条消息
        for (int i = 0; i < 10; i++) {
            String message = i + "";
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes());
            System.out.println("生产者发送消息" + message);
        }

    }
}
