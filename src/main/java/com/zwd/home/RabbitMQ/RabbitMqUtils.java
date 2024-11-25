package com.zwd.home.RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtils {
    // 获得RabbitMQ连接的channel
    public static Channel getChannel() throws Exception {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
//        factory.setPort(15672);
//        factory.setVirtualHost("testHost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 创建一个connection
        Connection connection = factory.newConnection();
        // 创建一个channel
        Channel channel = connection.createChannel();
        return channel;
    }
}
