//: com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Callable;


@Slf4j
public class ReceiveLogs implements Callable<Void> {

    private final String name;

    public ReceiveLogs(String name) {
        this.name = name;
    }

    private void consumeLogs() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EmitLog.EXCHANGE_NAME,
                    BuiltinExchangeType.FANOUT);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");

            log.debug(">>>>>>> [" + this.name +
                    "] Waiting for logs, to exit press CTRL-C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    log.debug(">>>>>>> [" + ReceiveLogs.this.name +
                            "] Received Log: '" + message + "'");
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