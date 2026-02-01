package com.desafio.pixpay.core.domain.money;

public class Money {
    private final Long PIPS_IN_MONEY = 100L;
    private Long valueInPips = 0L;

    public Long getMoneyInPips(){
        return valueInPips;
    }

    public Double getMoneyInDouble(){
        return Double.valueOf(valueInPips / PIPS_IN_MONEY);
    }

    public String getMoneyInString(){
        return Long.toString(valueInPips / PIPS_IN_MONEY);
    }

    public void setMoneyInPips(Long value){
        this.valueInPips = value;
    }

    public void setMoneyInDouble(Double value){
        this.valueInPips = convertDoubleToPips(value);
    }

    public void setMoneyInString(String value){
        this.valueInPips = convertStringToPips(value);
    }

    public void addValueInString(String value){
        this.valueInPips += convertStringToPips(value);
    }

    public void subtractValueInString(String value){
        this.valueInPips -= convertStringToPips(value);
    }

    public void multiplyValueInString(String value){
        this.valueInPips *= Long.getLong(value);
    }

    public void divideValueInString(String value){
        this.valueInPips /= Long.getLong(value);
    }

    public Long convertStringToPips(String value){
        Long pips, whole = 0L, cents = 0L;
        String[] valueSplited = value.split(".");
        if(valueSplited[1].length() != 2) throw new IllegalArgumentException("The cents must have exactly 2 decimal places.");
        whole += Long.getLong(value.split(".")[0]) * PIPS_IN_MONEY;
        cents += Long.getLong(value.split(".")[1]) * (PIPS_IN_MONEY/100);
        pips = whole + cents;
        return pips;
    }

    public Long convertDoubleToPips(Double value){
        Long pips = 0L;
        pips = (long)(PIPS_IN_MONEY * value);
        return pips;
    }
}
