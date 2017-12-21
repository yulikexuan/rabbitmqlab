//: com.yuli.springguru.rabbitmqlab.tutorials.RPCServer.java


package com.yuli.springguru.rabbitmqlab.tutorials;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class RPCServer implements Callable<Void> {

    public static final String RPC_QUEUE_NAME = "rpc-queue";

    private void calculate() throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Send.HOST_NAME);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(RPC_QUEUE_NAME, false, false,
                    false, null);

            channel.basicQos(1);

            log.debug(">>>>>>> Awaiting RPC requests ... ... ");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    String response = "";

                    String message = new String(body,"UTF-8");
                    int n = Integer.parseInt(message);
                    response = fibonacci(n).toString();

                    channel.basicPublish( "",
                            properties.getReplyTo(), replyProps,
                            response.getBytes("UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(),
                            false);
                    // RabbitMq consumer worker thread notifies the RPC
                    // server owner thread
                    synchronized(this) {
                        this.notify();
                    }
                }
            };

        }// End of try block

    }// End of calculate()

    private List<Integer> fibonacci(int i) {
        return Stream.iterate(new int[] {0, 1},
                t -> new int[] {t[1], t[0] + t[1]})
                        .limit(i)
                        .map(t -> t[0])
                        .collect(Collectors.toList());
    }

    @Override
    public Void call() throws Exception {
        this.calculate();
        return null;
    }

}///:~