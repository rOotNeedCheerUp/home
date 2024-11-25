package com.zwd.home.RabbitMQ.mode4_direct;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zwd.home.RabbitMQ.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

// 生产者
public class Producer {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 发送消息
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("info", "普通 info 信息");
        messageMap.put("warning", "警告 warning 信息");
        messageMap.put("error", "错误 error 信息");
        messageMap.put("debug", "调试 debug 信息");
        for (Map.Entry<String, String> mes : messageMap.entrySet()) {
            String routingKey = mes.getKey();
            String message = mes.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("消息发送完毕" + message);
        }
    }
}
