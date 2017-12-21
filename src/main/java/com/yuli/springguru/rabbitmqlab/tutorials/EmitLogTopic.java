//: com.yuli.springguru.rabbitmqlab.tutorials.EmitLogTopic.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


@Slf4j
public class EmitLogTopic implements Callable<Void> {

    public static final String EXCHANGE_NAME = "topic-logs";

    private final String routingKey;

    public EmitLogTopic(String routingKey) {
        this.routingKey = routingKey;
    }

    private void sendLogs() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            Stream.generate(this::makeLog).forEach(log -> {
                try {
                    Thread.sleep(2000);
                    this.log.debug(">>>>>>> [" + this.routingKey +
                            "] Sending log ... to exit press CTRL-C");

                    channel.basicPublish(EXCHANGE_NAME, this.routingKey,
                            null, log.getBytes());
                    EmitLogTopic.log.debug(">>>>>>> Sent '" + this.routingKey
                            + " : '" + log + "'");
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }// End of try block

    }// End of sendLogs()

    private String makeLog() {
        return new StringBuilder()
                .append(this.routingKey)
                .append(" - ")
                .append(UUID.randomUUID().toString())
                .toString();
    }

    @Override
    public Void call() throws Exception {
        this.sendLogs();
        return null;
    }

}///:~