package com.example.eventsourcingdemo;

import com.example.eventsourcingdemo.events.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class Repository {

    public static final String MYFIRST_EVENT_SOURCING_MSG = "myfirst-event-sourcing-msg";
//    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    private final RabbitTemplate rabbitTemplate;

    private Map<UUID, List<DomainEvent>> eventStreams = new ConcurrentHashMap<>();

//    public Repository(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    public Repository(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    void save(CreditCard creditCard) {
        List<DomainEvent> currentEvents = eventStreams.getOrDefault(creditCard.getUuid(), new ArrayList<>());
        List<DomainEvent> newEvents = creditCard.getDirtyEvents();
        currentEvents.addAll(newEvents);
        eventStreams.put(creditCard.getUuid(), currentEvents);
//        newEvents.forEach(
//                domainEvent -> kafkaTemplate.send(MYFIRST_EVENT_SOURCING_MSG, domainEvent)
//        );

        newEvents.forEach(

                domainEvent -> {
                    rabbitTemplate.convertAndSend(RabbitMqConfiguration.topicExchangeName, "foo.bar.baz", domainEvent.toString());
                }
        );
        creditCard.eventsFlushed();
    }

    CreditCard load(UUID uuid) {
        return CreditCard.recreate(uuid, eventStreams.getOrDefault(uuid, new ArrayList<>()));
    }
}
