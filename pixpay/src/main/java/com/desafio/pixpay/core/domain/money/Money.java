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

    public Long getMoneyInPips(){
        return valueInPips;
    }

    public Double getMoneyInCurrency(){
        return Double.valueOf(valueInPips) / PIPS_IN_MONEY;
    }

    public Money setMoneyInPips(Long value){
        this.valueInPips = value;
        return this;
    }

    public Money setMoneyInCurrency(Double value){
        this.valueInPips = convertDoubleToPips(value);
        return this;
    }

    public Money addValueInCurrency(Money value){
        this.valueInPips += value.getMoneyInPips();
        return this;
    }

    public Money subtractValueInCurrency(Money value){
        this.valueInPips -= value.getMoneyInPips();
        return this;
    }

    public Long convertDoubleToPips(Double value){
        Long pips = 0L;
        pips = (long) Math.round(PIPS_IN_MONEY * value);
        return pips;
    }
}
