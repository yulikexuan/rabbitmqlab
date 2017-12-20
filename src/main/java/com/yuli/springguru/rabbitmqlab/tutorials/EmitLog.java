//: com.yuli.springguru.rabbitmqlab.tutorials.EmitLog.java


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
public class EmitLog implements Callable<Void> {

    public static final String EXCHANGE_NAME = "direct-logs";

    private final String name;

    public EmitLog(String name) {
        this.name = name;
    }

    private void sendLogs() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            ThreadLocalRandom random = ThreadLocalRandom.current();

            Stream.generate(this::makeLog).forEach(log -> {
                try {
                    Thread.sleep(2000);
                    this.log.debug(">>>>>>> [" + this.name +
                            "] Sending log ... to exit press CTRL-C");

                    String severity = getSeverity(random.nextInt(10));
                    String logMsg = severity + " - " + log;
                    /*
                     * The second parameter, routingKey, is "", means the queue
                     * for logs is ignored here, the log messages should be lost
                     * because no queue is bound to the exchange yet
                     * If no consumer is listening log messages yet, those
                     * messages can be discarded safely
                     */
                    channel.basicPublish(EXCHANGE_NAME, severity,
                            null, logMsg.getBytes());
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    private String makeLog() {
        return new StringBuilder()
                .append("INFO - ")
                .append(UUID.randomUUID().toString())
                .toString();
    }

    private String getSeverity(int index) {
        return Severity.values()[index % 3].getSeverity();
    }

    @Override
    public Void call() throws Exception {
        this.sendLogs();
        return null;
    }

}///:~