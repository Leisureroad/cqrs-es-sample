package com.example.eventsourcingdemo;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KStreamBuilderFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;

@SpringBootApplication
@EnableScheduling
public class EventsourcingDemoApplication {

    private final Repository repository;

    public EventsourcingDemoApplication(Repository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(EventsourcingDemoApplication.class, args);
    }

	@Scheduled(fixedRate = 2000)
	public void randomCards() {
        System.out.println("Sending message...");
		CreditCard card = new CreditCard(UUID.randomUUID());
		card.assignLimit(new BigDecimal(2000));
		card.withdraw(BigDecimal.TEN);
		card.repay(BigDecimal.TEN);
		repository.save(card);
	}
}


//@RestController
//class CreditCardController {
//    private final KStreamBuilderFactoryBean kStreamBuilderFactoryBean;

//    CreditCardController(KStreamBuilderFactoryBean kStreamBuilderFactoryBean) {
//        this.kStreamBuilderFactoryBean = kStreamBuilderFactoryBean;
//    }

//    @GetMapping("/cards")
//    List<CreditCard> creditCardList() {
//        List<CreditCard> cards = new ArrayList<>();
//        ReadOnlyKeyValueStore<String, CreditCard> store = kStreamBuilderFactoryBean
//                .getKafkaStreams()
//                .store(KafkaConfiguration.MYFIRST_SNAPSHOTS_FOR_CARDS, keyValueStore());
//
//        store.all().forEachRemaining(stringCreditCardKeyValue -> cards.add(stringCreditCardKeyValue.value));
//        return cards;
//    }
//}