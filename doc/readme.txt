1.  For Round-Robin Dispatching (Work Queue)
    0: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Send
    1: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Recv

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-1
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-2
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 1 consumer-3
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 0 producer-1

2.  For Publish/Subscribe

    2: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLog
    3: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 log-consumer-1
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 log-consumer-2
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 2 log-producer-1

3.  For Routing

    2: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLog
    3: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 info
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 warning
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 3 error
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 2 log-producer

4.  For Topics

    4: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLogTopic
    5: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogsTopic

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 5 *.orange.*
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 5 "*.*.rabbit" "lazy.#"
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4 quick.orange.rabbit

    # Receive all logs
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4 #

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4

    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4 lazy.orange.elephant
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4 quick.orange.fox
    java -jar rabbitmqlab-0.0.1-SNAPSHOT.jar 4 lazy.brown.fox

5.  RPC

