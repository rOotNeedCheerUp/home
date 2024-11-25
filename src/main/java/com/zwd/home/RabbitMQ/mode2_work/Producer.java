package com.zwd.home.RabbitMQ.mode2_work;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

import java.util.Scanner;

/**
 * RabbitMQ 中的工作模式默认采用轮训的方式，
 * 也就是如果有两个消费者的话，消息逐一分给每个消费者进行消费。
 * 接下来我们来用 Java 代码实现一下 Work Queues工作模式，来测试其轮训消费的功能。
 */

public class Producer {
    public static String QUEUE_NAME = "work";
    public static void main(String[] args) throws Exception {
        //创建通道
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(declareOk);
        //发送消息
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕" + message);
        }
    }
}
