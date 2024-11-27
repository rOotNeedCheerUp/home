package com.zwd.home.RabbitMQ.mq1_work_ack;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

// 测试手动应答
public class Consumer02 {
    public static String QUEUE_NAME = "ack";

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("consumer2收到消息时间较长");
        // 消费消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 模拟接受消息的延迟 10s
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消息成功消费!内容为:" + new String(message.getBody()));
            // 手动应答:第一个参数表示消息标记tag、第二个参数false表示不进行批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        // 取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        // 消费消息（第2个参数修改为false表示手动应答）
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
