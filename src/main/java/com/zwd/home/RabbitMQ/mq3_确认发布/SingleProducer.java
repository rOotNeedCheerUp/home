package com.zwd.home.RabbitMQ.mq3_确认发布;


import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 测试单个确认发布
public class SingleProducer {
    public static String QUEUE_NAME = "confirm";
    public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel =  RabbitMqUtils.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发送10条消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 单个消息发送完毕马上确认
            boolean flag = channel.waitForConfirms();
            // 服务端返回false或超时时间内未返回，生产者可以消息重发
            if (flag) System.out.println("消息" + i + "发送成功");
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条单独确认消息耗时:" + (end - begin) + "ms");
    }
}
