package com.zwd.home.RabbitMQ.mq1_work_ack;

import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

import java.util.Scanner;

public class Producer {
    public static String QUEUE_NAME="ack";

    public static void main(String[] args) throws Exception{
        //获取通道
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕" + message);
        }
    }
}
