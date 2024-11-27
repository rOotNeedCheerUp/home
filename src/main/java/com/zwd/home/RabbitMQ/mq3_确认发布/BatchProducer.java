package com.zwd.home.RabbitMQ.mq3_确认发布;


import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 测试单个确认发布
public class BatchProducer {
    public static String QUEUE_NAME = "confirm";
    public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        // 确定批量大小
        int batchSize = 5;
        // 未确认消息个数
        int noConfirmMesNum = 0;
        // 开始时间
        long begin = System.currentTimeMillis();
        // 发送消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            noConfirmMesNum++;
            // 每5个一批发布确认
            if (noConfirmMesNum == batchSize) {
                boolean flag = channel.waitForConfirms();
                if (flag) System.out.println("消息" + i + "与之前的" + batchSize + "条发送成功");
                noConfirmMesNum = 0;
            }
        }
        // 为了确保还有剩余没有确认消息 再次确认
        if (noConfirmMesNum > 0)
            channel.waitForConfirms();
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息耗时:" + (end - begin) + "ms");
    }
}
