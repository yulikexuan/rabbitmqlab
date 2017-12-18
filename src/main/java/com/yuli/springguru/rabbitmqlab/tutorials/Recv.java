//: com.yuli.springguru.rabbitmqlab.tutorials.Recv.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class Recv implements Callable<Void> {

    private final String name;

    public Recv(String name) {
        this.name = name;
    }

    private void consume() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Send.HOST_NAME);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(Send.QUEUE_NAME, Send.QUEUE_DURABLE,
                    false, false, null);

            log.debug(">>>>>>> [" + this.name +
                    "] Waiting for message. To exit press CTRL-C");

            /*
             * RabbitMQ will not to give more than one message to this worker
             * at a time
             *
             * Don't dispatch a new message to this worker until it has
             * processed and acknowledged the previous one
             */
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String task = new String(body, Send.CHAR_SET);
                    log.debug(">>>>>>> [" + name + "] Processing: '" + task
                            + "'");
                    int weight = 0;
                    try {
                        for (char ch: task.toCharArray()) {
                            if (ch == '.') {
                                Thread.sleep(1000);
                                weight++;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        log.debug(">>>>>>> [" + name + "] Done: '" + weight
                                + "'");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            boolean autoAck = false;
            channel.basicConsume(Send.QUEUE_NAME, autoAck, consumer);

            while (true) {
                Thread.sleep(1000);
            }
        }
    }

    @Override
    public Void call() throws Exception {
        this.consume();
        return null;
    }

}///:~