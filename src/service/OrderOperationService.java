package service;

import model.PaymentMethod;

public interface OrderOperationService {

    boolean completeOrder();

    boolean payByCard(PaymentMethod paymentMethod);

    void getNewPaymentMethod();


}
