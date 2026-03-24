package com.desafio.pixpay.core.domain.money;

public class Money {
    private final Long REAL_IN_CENTS = 100L;
    private Long valueInCents = 0L;

    public Money(){}

    public Money(Long valueInCents){
        this.valueInCents = valueInCents;
    }

    public static Money builder(){
        return new Money();
    }

    public Long getMoneyInCents(){
        return valueInCents;
    }

    public Double getMoneyInReal(){
        return Double.valueOf(valueInCents) / REAL_IN_CENTS;
    }

    public Money setMoneyInCents(Long value){
        this.valueInCents = value;
        return this;
    }

    public Money setMoneyInReal(Double value){
        this.valueInCents = convertDoubleToCents(value);
        return this;
    }

    public Money addValueInReal(Money value){
        this.valueInCents += value.getMoneyInCents();
        return this;
    }

    public Money subtractValueInReal(Money value){
        this.valueInCents -= value.getMoneyInCents();
        return this;
    }

    public Long convertDoubleToCents(Double value){
        Long cents = 0L;
        cents = (long) Math.round(REAL_IN_CENTS * value);
        return cents;
    }
}
