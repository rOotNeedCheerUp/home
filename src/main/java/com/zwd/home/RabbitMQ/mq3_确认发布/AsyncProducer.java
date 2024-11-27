package com.zwd.home.RabbitMQ.mq3_确认发布;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class AsyncProducer {
    public static String QUEUE_NAME = "confirm";
    public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel =  RabbitMqUtils.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        // 准备一个线程安全有序的哈希表，用于存放消息的序号以及内容
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        // 消息确认成功回调函数(第一个参数表示消息标志,第二个参数表示是否为批量确认)
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            // 删除掉已经确认的消息，剩下就是未确认的消息
            if (multiple) { // 如果是批量 则批量删除
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();
            } else concurrentSkipListMap.remove(deliveryTag);   // 如果不是批量发送 则删除当前消息
            System.out.println("消息:" + deliveryTag + "已确认发布");
        };
        // 消息确认失败回调函数(第一个参数表示消息标志,第二个参数表示是否为批量确认)
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息为:" + message);
        };
        // 首先准备异步消息监听器,监听哪些消息成功了,哪些消息失败了
        channel.addConfirmListener(ackCallback, nackCallback);  // 异步通知
        // 发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 在此记录下所有要发送的消息
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条异步确认消息耗时:" + (end - begin) + "ms");
    }
}
