//: com.yuli.springguru.rabbitmqlab.tutorials.Recv.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class Recv implements Callable<Void> {

    private static AtomicInteger count = new AtomicInteger();

    private final String name;

    public Recv(String name) {
        this.name = name;
    }

    private void consume() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Send.HOST_NAME);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(Send.QUEUE_NAME, false,
                    false, false, null);

            log.debug(">>>>>>> [" + this.name +
                    "] Waiting for message. To exit press CTRL-C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    log.debug(">>>>>>> [" + name + "] Received: '" + message
                            + "'");
                    count.addAndGet(1);
                }
            };

            channel.basicConsume(Send.QUEUE_NAME, true, consumer);
            while (count.get() < 10) {
                Thread.sleep(200);
            }
        }
    }

    @Override
    public Void call() throws Exception {
        this.consume();
        return null;
    }

}///:~