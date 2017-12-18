//: com.yuli.springguru.rabbitmqlab.bootstrap.RabbitmqBootstrap.java


package com.yuli.springguru.rabbitmqlab.bootstrap;


import com.yuli.springguru.rabbitmqlab.tutorials.Recv;
import com.yuli.springguru.rabbitmqlab.tutorials.Send;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


//@Component
public class RabbitmqBootstrap implements
        ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(
            ContextRefreshedEvent contextRefreshedEvent) {

        ExecutorService service = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        service.submit(new Recv("I"));
        service.submit(new Recv("II"));
//        service.submit(new Recv("III"));
        service.submit(new Send());

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();

        try {
            if (!service.awaitTermination(1000,
                    TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }

}///:~