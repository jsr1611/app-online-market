package service.implement;

import enums.CardType;
import enums.OrderStatus;
import model.*;
import realization.OnlineMarketDemo;
import service.OrderOperationService;

import java.util.Scanner;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:19 PM
 */
public class OrderOperationServiceImpl implements OrderOperationService {
    private Long id;
    private Order order;
    private OrderDetails details;
    private ShoppingCart shoppingCart;
    private Scanner scanner;
    private User user = OnlineMarketDemo.currentUser;

    public OrderOperationServiceImpl(){}
    public OrderOperationServiceImpl(Long id, Order order, OrderDetails details, ShoppingCart shoppingCart) {
        this.id = id;
        this.order = order;
        this.details = details;
        this.shoppingCart = shoppingCart;
        this.scanner = new Scanner(System.in);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderDetails getDetails() {
        return details;
    }

    public void setDetails(OrderDetails details) {
        this.details = details;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public boolean completeOrder() {
        boolean paymentStatus = false;
        System.out.println("=====================YOUR ORDER=====================");
        System.out.println(details);
        System.out.println("Payment methods\n" +
                "1. UzCard\n" +
                "2. Humo\n" +
                "3. VISA/Master\n" +
                "4. Cash");

        System.out.print("Please, choose your payment method: ");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                PaymentMethod uzcardPayment = user.getPaymentMethodByCardType(CardType.UzCard);
                if(uzcardPayment == null){
                    getNewPaymentMethod();
                }
                else {
                    paymentStatus = payByCard(uzcardPayment);
                }
                break;
            case 2:
                PaymentMethod humoPayment = user.getPaymentMethodByCardType(CardType.Humo);
                if(humoPayment == null){
                    getNewPaymentMethod();
                }
                else {
                    paymentStatus = payByCard(humoPayment);
                }
                break;
            case 3:
                PaymentMethod visaPayment = user.getPaymentMethodByCardType(CardType.VISA, CardType.Master);
                if(visaPayment == null){
                    getNewPaymentMethod();
                }
                else {
                    paymentStatus = payByCard(visaPayment);
                }
                break;
            case 4:
                System.out.println("Your payment method: cash");
                order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);
                System.out.println("Payment was successful!");
                paymentStatus = true;
        }
        return paymentStatus;
    }

    public boolean payByCard(PaymentMethod paymentMethod) {
        System.out.println("Your payment method: " + paymentMethod.getCardType());
        System.out.print("Enter first two digits of your card password: ");
        boolean paymentStatus = false;
        int password = scanner.nextInt();
        if(user.getAccount().getPassword().equals(password)){
            if(user.getAccount().getBalance() > order.getTotalPrice()){
                user.getAccount().addBalance(-1 * order.getTotalPrice());
                order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);
                System.out.println("Payment was successful!");
                paymentStatus = true;
            }
            else {
                System.out.println("Not enough money in your balance. Please, use a different payment method!");
                order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            }
        }
        return paymentStatus;
    }

    public void getNewPaymentMethod(){
        Long cardNum;
        Integer month, year;
        String cardHolderName;
        CardType cardType;

        System.out.print("Enter your card number");
        cardNum = scanner.nextLong();
        //System.out.print("Enter last two digits of your card password: ");
        //password = scanner.nextInt();
        if(cardNum.toString().startsWith("8600")){
            cardType = CardType.UzCard;
        }
        else if(cardNum.toString().startsWith("4")){
            cardType = CardType.VISA;
        }
        else if(cardNum.toString().startsWith("5")){
            cardType = CardType.Master;
        }
        else {
            cardType = CardType.Humo;
        }
        scanner = new Scanner(System.in);
        System.out.print("Enter the name on your card: ");
        cardHolderName = scanner.nextLine();
        scanner = new Scanner(System.in);

        System.out.print("Enter the expiration month: ");
        month = scanner.nextInt();
        System.out.print("Enter the expiration year: ");
        year = scanner.nextInt();
        System.out.print("Do you want to save payment info for future use? 'Y' for 'yes': ");
        scanner = new Scanner(System.in);
        boolean savePaymentInfo = scanner.next().contains("Y");
        if(savePaymentInfo){
            OnlineMarketDemo.currentUser.addPaymentMethod(
                    new PaymentMethod(
                            cardHolderName,
                            cardNum,
                            month,
                            year,
                            cardType
                    ));
        }
    }
}
