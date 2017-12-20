//: com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;


@Slf4j
public class ReceiveLogs implements Callable<Void> {

    private final String logType;

    public ReceiveLogs(String logType) {
        this.logType = logType;
    }

    private void consumeLogs() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EmitLog.EXCHANGE_NAME,
                    BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();

            if (Severity.INFO.getSeverity().equals(this.logType)) {
                Arrays.stream(Severity.values()).forEach(s -> {
                    try {
                        channel.queueBind(queueName, EmitLog.EXCHANGE_NAME,
                                s.getSeverity());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (Severity.WARNING.getSeverity().equals(this.logType)) {
                channel.queueBind(queueName, EmitLog.EXCHANGE_NAME,
                        Severity.WARNING.getSeverity());
                channel.queueBind(queueName, EmitLog.EXCHANGE_NAME,
                        Severity.ERROR.getSeverity());
            } else {
                channel.queueBind(queueName, EmitLog.EXCHANGE_NAME,
                        Severity.ERROR.getSeverity());
            }

            log.debug(">>>>>>> [" + this.logType +
                    "] Waiting for logs, to exit press CTRL-C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    log.debug(">>>>>>> " +  message);
                }
            };

            boolean autoAck = true;
            channel.basicConsume(queueName, autoAck, consumer);

            while (true) {
                Thread.sleep(1000);
            }

        }// End of try block
    }

    @Override
    public Void call() throws Exception {
        this.consumeLogs();
        return null;
    }

}///:~