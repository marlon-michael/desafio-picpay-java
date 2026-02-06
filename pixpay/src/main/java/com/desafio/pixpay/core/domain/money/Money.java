package com.desafio.pixpay.core.domain.money;

public class Money {
    private final Long PIPS_IN_MONEY = 100L;
    private Long valueInPips = 0L;

    public Money(){}

    public Money(Long valueInPips){
        this.valueInPips = valueInPips;
    }

    public Long getMoneyInPips(){
        return valueInPips;
    }

    public Double getMoneyInCurrency(){
        return Double.valueOf(valueInPips / PIPS_IN_MONEY);
    }

    public void setMoneyInPips(Long value){
        this.valueInPips = value;
    }

    public void setMoneyInCurrency(Double value){
        this.valueInPips = convertDoubleToPips(value);
    }

    public void addValueInCurrency(Double value){
        this.valueInPips += convertDoubleToPips(value);
    }

    public void subtractValueInCurrency(Double value){
        this.valueInPips -= convertDoubleToPips(value);
    }

    public Long convertStringToPips(String value){
        Long pips;
        pips = (long) Math.floor(Double.parseDouble(value) * PIPS_IN_MONEY);
        return pips;
    }

    public Long convertDoubleToPips(Double value){
        Long pips = 0L;
        pips = (long) Math.floor(PIPS_IN_MONEY * value);
        return pips;
    }
}
