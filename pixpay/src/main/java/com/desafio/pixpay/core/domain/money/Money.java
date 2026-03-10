package com.desafio.pixpay.core.domain.money;

public class Money {
    private final Long PIPS_IN_MONEY = 100L;
    private Long valueInPips = 0L;

    public Money(){}

    public Money(Long valueInPips){
        this.valueInPips = valueInPips;
    }

    public static Money builder(){
        return new Money();
    }

    public Long getMoneyInCents(){
        return valueInPips;
    }

    public Double getMoneyInCurrency(){
        return Double.valueOf(valueInPips) / PIPS_IN_MONEY;
    }

    public Money setMoneyInCents(Long value){
        this.valueInPips = value;
        return this;
    }

    public Money setMoneyInCurrency(Double value){
        this.valueInPips = convertDoubleToCents(value);
        return this;
    }

    public Money addValueInCurrency(Money value){
        this.valueInPips += value.getMoneyInCents();
        return this;
    }

    public Money subtractValueInCurrency(Money value){
        this.valueInPips -= value.getMoneyInCents();
        return this;
    }

    public Long convertDoubleToCents(Double value){
        Long pips = 0L;
        pips = (long) Math.round(PIPS_IN_MONEY * value);
        return pips;
    }
}
