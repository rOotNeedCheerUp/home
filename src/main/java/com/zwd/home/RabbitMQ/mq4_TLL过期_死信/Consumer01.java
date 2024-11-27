package com.zwd.home.RabbitMQ.mq4_TLL过期_死信;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    private static final String NORMAL_EXCHANGE = "normal_exchange";    // 普通交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";    // 死信交换机名称
    private static final String NORMAL_QUEUE = "normal_queue";  // 普通队列名称
    private static final String DEAD_QUEUE = "dead_queue";  // 死信队列名称

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);//普通交换机
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);//死信交换机
        // 声明普通和死信队列(普通队列需要传递参数设置死信交换机及其对应的路由key)
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE); // 设置死信交互机
        arguments.put("x-dead-letter-routing-key", "lisi"); // 设置与死信交换机间的routing-key
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定队列与交换机,设置其间的路由key
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        // 消费消息
        System.out.println("Consumer01等待接收普通队列到消息......");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String mgs = new String(message.getBody());
            if(mgs.equals("2")){
                System.out.println("Consumer01接收到普通队列的消息" + mgs + "并拒收该消息");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01接收到普通队列的消息:" + new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(NORMAL_QUEUE, deliverCallback, cancelCallback);

    }
}
