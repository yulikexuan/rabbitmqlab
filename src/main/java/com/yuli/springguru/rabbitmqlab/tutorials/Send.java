//: com.yuli.springguru.rabbitmqlab.tutorials.Send.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
public class Send implements Callable<Void> {

    public static final String QUEUE_NAME = "yli-task";
    public static final String HOST_NAME = "localhost";
    public static final String CHAR_SET = "UTF-8";
    public static final boolean QUEUE_DURABLE = true;

    public static final int TASK_LOAD = 6;

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
            channel.queueDeclare(QUEUE_NAME, QUEUE_DURABLE, false,
                    false, null);

            ThreadLocalRandom random = ThreadLocalRandom.current();
            String task = null;
            for (int i = 0; i < TASK_LOAD; i++) {
                task = this.makeTask(random, i);
                log.debug(">>>>>>> Sending: '" + task + "'");
                channel.basicPublish("", QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        task.getBytes(CHAR_SET));
                Thread.sleep(2000);
            }
        }
    }

    private String makeTask(ThreadLocalRandom random, int index) {
        StringBuilder taskBuilder = new StringBuilder("Task ");
        int weight = (index % 2 == 0) ? random.nextInt(1, 3) :
                9;

        for (int i = 0; i < weight; i++) {
            taskBuilder.append(".");
        }

        return taskBuilder.toString();
    }

    @Override
    public Void call() throws Exception {
        this.send();
        return null;
    }

}///:~