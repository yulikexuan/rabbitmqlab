//: com.yuli.springguru.rabbitmqlab.RabbitmqlabApplication;


package com.yuli.springguru.rabbitmqlab;


import com.yuli.springguru.rabbitmqlab.tutorials.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@SpringBootApplication
public class RabbitmqlabApplication {

	/*
	 * args[]:
	 * 0: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Send
     * 1: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Recv
     * 2: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLog
     * 3: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs
	 * 4: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLogTopic
	 * 5: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogsTopic
	 */
	public static void main(String[] args) throws Exception {

		SpringApplication.run(RabbitmqlabApplication.class, args);

		if (args.length > 1) {
			int arg0 = Integer.valueOf(args[0]);
			if (arg0 == 1) {
				new Recv(args[1]).call();
			} else if (arg0 == 0) {
				new Send().call();
			} else if (arg0 == 3) {
				new ReceiveLogs(args[1]).call();
			} else if (arg0 == 2) {
				new EmitLog(args[1]).call();
			} else if (arg0 == 5) {
				new ReceiveLogsTopic(Arrays.stream(args)
						.skip(1)
						.collect(Collectors.toList())).call();
			} else if (arg0 == 4) {
				new EmitLogTopic(args[1]).call();
			}
		}
	}
}
