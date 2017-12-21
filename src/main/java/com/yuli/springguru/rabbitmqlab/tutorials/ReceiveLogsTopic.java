//: com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogsTopic.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;


@Slf4j
public class ReceiveLogsTopic implements Callable<Void> {

    private final List<String> bindingKey;

    public ReceiveLogsTopic(List<String> bindingKey) {
        this.bindingKey = bindingKey;
    }

    private void consumeLogs() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EmitLogTopic.EXCHANGE_NAME,
                    BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();

            this.bindingKey.stream().forEach(bk -> {
                try {
                    channel.queueBind(queueName, EmitLogTopic.EXCHANGE_NAME, bk);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            log.debug(">>>>>>> [x] Waiting for logs, to exit press CTRL-C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    log.debug(">>>>>>> Received " + envelope.getRoutingKey()
                            + " : '" +  message + "'");
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