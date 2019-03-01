package com.example.eventsourcingdemo.events;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CardRepaid implements DomainEvent{
    private UUID uuid;
    private Date date;
    private BigDecimal amount;
    private String type = "card.repaid";

    public CardRepaid(UUID uuid, Date date, BigDecimal amount) {
        this.uuid = uuid;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public Date timestamp() {
        return this.date;
    }

    @Override
    public UUID aggregateUUID() {
        return this.uuid;
    }
}
