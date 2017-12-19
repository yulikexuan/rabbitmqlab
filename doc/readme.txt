1.  Round-Robin Dispatching (Work Queue)
    0: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Send
    1: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Recv

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-1
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-2
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-3
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 0 producer-1

2.  Publish/Subscribe

    2: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLog
    3: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 log-consumer-1
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 log-consumer-2
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 2 log-producer-1