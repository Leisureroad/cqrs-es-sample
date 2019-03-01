package com.example.eventsourcingdemo;

import com.example.eventsourcingdemo.events.CardRepaid;
import com.example.eventsourcingdemo.events.CardWithdrawn;
import com.example.eventsourcingdemo.events.DomainEvent;
import com.example.eventsourcingdemo.events.LimitAssigned;
import javaslang.API;
import javaslang.Predicates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;

import static javaslang.API.Case;
import static javaslang.collection.List.ofAll;

@Getter
@ToString
@NoArgsConstructor
public class CreditCard {
    private UUID uuid;
    private BigDecimal limit;
    private BigDecimal used = BigDecimal.ZERO;

    public List<DomainEvent> getDirtyEvents() {
        return Collections.unmodifiableList(dirtyEvents);
    }

    private List<DomainEvent> dirtyEvents = new ArrayList<>();

    public CreditCard(UUID uuid) {
        this.uuid = uuid;
    }

    void assignLimit(BigDecimal amount) {
        if(limitAlreadyAssigned()) {
            throw new IllegalStateException();
        }
        limitAssigned(new LimitAssigned(uuid, new Date(), amount));
    }

    private CreditCard limitAssigned(LimitAssigned limitAssigned) {
        this.limit = limitAssigned.getLimit();
        dirtyEvents.add(limitAssigned);
        return this;
    }

    private boolean limitAlreadyAssigned() {
        return limit != null;
    }

    void withdraw(BigDecimal amount) {
        if(notEnoughMoneyToWithdraw(amount)) {
            throw new IllegalStateException();
        }
        cardWithdrawn(new CardWithdrawn(uuid, new Date(), amount));

    }

    private CreditCard cardWithdrawn(CardWithdrawn cardWithdrawn) {
        this.used = used.add(cardWithdrawn.getAmount());
        dirtyEvents.add(cardWithdrawn);
        return this;
    }

    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo(amount) < 0 ;
    }

    void repay(BigDecimal amount) {
        cardRepaid(new CardRepaid(uuid, new Date(), amount));
    }

    private CreditCard cardRepaid(CardRepaid cardRepaid) {
        used = used.subtract(cardRepaid.getAmount());
        dirtyEvents.add(cardRepaid);
        return this;
    }

    public BigDecimal availableLimit() {
        return limit.subtract(used);
    }

    public void eventsFlushed() {
        dirtyEvents.clear();
    }

    public static CreditCard recreate(UUID uuid, List<DomainEvent> events) {
        return ofAll(events).foldLeft(new CreditCard(uuid), CreditCard::handle);
    }

    CreditCard handle(DomainEvent domainEvents) {
        return API.Match(domainEvents).of(
                Case(Predicates.instanceOf(LimitAssigned.class), this::limitAssigned),
                Case(Predicates.instanceOf(CardWithdrawn.class), this::cardWithdrawn),
                Case(Predicates.instanceOf(CardRepaid.class), this::cardRepaid)
        );
    }
}
