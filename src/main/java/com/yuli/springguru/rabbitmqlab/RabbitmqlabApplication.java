//: com.yuli.springguru.rabbitmqlab.RabbitmqlabApplication;


package com.yuli.springguru.rabbitmqlab;


import com.yuli.springguru.rabbitmqlab.tutorials.EmitLog;
import com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs;
import com.yuli.springguru.rabbitmqlab.tutorials.Recv;
import com.yuli.springguru.rabbitmqlab.tutorials.Send;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class RabbitmqlabApplication {

	/*
	 * args[]:
	 * 0: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Send
     * 1: create instance of com.yuli.springguru.rabbitmqlab.tutorials.Recv
     * 2: create instance of com.yuli.springguru.rabbitmqlab.tutorials.EmitLog
     * 3: create instance of com.yuli.springguru.rabbitmqlab.tutorials.ReceiveLogs
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
			}
		}
	}
}
