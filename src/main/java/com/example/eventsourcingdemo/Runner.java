//package com.example.eventsourcingdemo;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
////import java.math.BigDecimal;
////import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class Runner implements CommandLineRunner {
//
//    private final RabbitTemplate rabbitTemplate;
//    private final Receiver receiver;
////    private final Repository repository;
//
//    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate, Repository repository) {
//        this.receiver = receiver;
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
////        System.out.println("Sending message...");
////        rabbitTemplate.convertAndSend(RabbitMqConfiguration.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
////        CreditCard card = new CreditCard(UUID.randomUUID());
////        card.assignLimit(new BigDecimal(2000));
////        card.withdraw(BigDecimal.TEN);
////        card.repay(BigDecimal.TEN);
////        repository.save(card);
//        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
//    }
//
//}
