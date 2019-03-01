package com.example.eventsourcingdemo.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "card.repaid", value = CardRepaid.class),
        @JsonSubTypes.Type(name = "card.withdrawn", value = CardWithdrawn.class),
        @JsonSubTypes.Type(name = "card.assigned", value = LimitAssigned.class)
})
public interface DomainEvent {

    Date timestamp();
    UUID aggregateUUID();
}