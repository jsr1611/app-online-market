package service;

import model.PaymentMethod;

public interface OrderOperationService {

    boolean completeOrder();

    boolean payByCard(PaymentMethod paymentMethod);

    PaymentMethod getNewPaymentMethod();


}
