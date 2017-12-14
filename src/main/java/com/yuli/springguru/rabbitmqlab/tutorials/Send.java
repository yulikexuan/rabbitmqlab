//: com.yuli.springguru.rabbitmqlab.tutorials.Send.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


@Slf4j
public class Send implements Callable<Void> {

    public static final String QUEUE_NAME = "hello";
    public static final String HOST_NAME = "localhost";

    private void send() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            /*
             * To send, we must declare a queue for us to send to; then we can
             * publish a message to the queue
             *
             * A queue will only be created if it doesn't exist already. The
             * message content is a byte array, so you can encode whatever you
             * like there
             */
            channel.queueDeclare(QUEUE_NAME, false, false,
                    false, null);

            String message = null;

            int count = 10;
            for (int i = 0; i < count; i++) {
                message = i + ": Hello World!";
                channel.basicPublish("", QUEUE_NAME, null,
                        message.getBytes("UTF-8"));
                log.debug(">>>>>>> [x] Sent '" + message + "'");
                Thread.sleep(200);
            }
        }

    }

    @Override
    public Void call() throws Exception {
        this.send();
        return null;
    }

}///:~