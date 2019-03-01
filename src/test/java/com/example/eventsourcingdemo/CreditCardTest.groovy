package com.example.eventsourcingdemo

import spock.lang.Specification

class CreditCardTest extends Specification {
    CreditCard creditCard = new CreditCard(UUID.randomUUID())

    def 'cannot assign limit at the second time' () {
        given:
            creditCard.assignLimit(100)
        when:
            creditCard.assignLimit(200)
        then:
            thrown(IllegalStateException)
    }

    def 'can assign limit to a card' () {
        when:
            creditCard.assignLimit(100)
        then:
            creditCard.availableLimit() == 100
    }

    def 'cannot withdraw when not enough money' () {
        given:
            creditCard.assignLimit(100)
        when:
            creditCard.withdraw(150)
        then:
            thrown(IllegalStateException)
    }

    def 'can withdraw from a card' () {
        given:
            creditCard.assignLimit(100)
        when:
            creditCard.withdraw(50)
        then:
            creditCard.availableLimit() == 50
    }

    def 'can repay a card' () {
        given:
            creditCard.assignLimit(100)
        and:
            creditCard.withdraw(50)
        when:
            creditCard.repay(50)
        then:
            creditCard.availableLimit() == 100
    }
}
