package model;

import enums.CardType;

public class PaymentMethod {
    private String cardHolderName;
    private Long cardNum;
    private Integer expireMonth;
    private Integer expireYear;
    private CardType cardType;

    public PaymentMethod(String cardHolderName,
                         Long cardNum,
                         Integer expireMonth,
                         Integer expireYear,
                         CardType cardType) {
        this.cardHolderName = cardHolderName;
        this.cardNum = cardNum;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cardType = cardType;
    }

       public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Long getCardNum() {
        return cardNum;
    }

    public void setCardNum(Long cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(Integer expireMonth) {
        this.expireMonth = expireMonth;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }
    public CardType getCardType(){
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "PaymentMethod" +
                "\nCard holder's name: " + cardHolderName +
                "\nCard number: " + cardNum +
                "\nExpire date: " + expireMonth +" / "+ expireYear +
                "\nCard type: " + cardType;
    }
}
