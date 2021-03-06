package service.implement;

import enums.CardType;
import enums.OrderStatus;
import model.*;
import realization.OnlineMarketDemo;
import service.OrderOperationService;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    Boolean paymentStatus;

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
        paymentStatus = false;
        scanner = new Scanner(System.in);
        System.out.println("=====================YOUR ORDER=====================");

        order = new Order(OnlineMarketDemo.orderDetails.size()+1L, user, OrderStatus.NEW, shoppingCart.getTotalAmount());
        details = new OrderDetails(order.getId(), order, shoppingCart.getProducts());
        System.out.println(details);
//        if(order == null){
//            order = details.getOrder();
//        }
        order.setTotalPrice(shoppingCart.getTotalAmount());
        while (true) {
            System.out.println("Payment methods\n" +
                    "1. UzCard\n" +
                    "2. Humo\n" +
                    "3. VISA/Master\n" +
                    "4. Cash");

            System.out.print("Please, choose your payment method: ");
            PaymentMethod paymentMethod = null;
            int choice = -1;
            try {
                while (choice < 0 || choice > 4) {
                    scanner = new Scanner(System.in);
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            paymentMethod = user.getPaymentMethodByCardType(CardType.UzCard);
                            if (paymentMethod == null) {
                                paymentMethod = getNewPaymentMethod();
                            }
                            break;
                        case 2:
                            paymentMethod = user.getPaymentMethodByCardType(CardType.Humo);
                            if (paymentMethod == null) {
                                paymentMethod = getNewPaymentMethod();
                            }
                            break;
                        case 3:
                            paymentMethod = user.getPaymentMethodByCardType(CardType.VISA, CardType.Master);
                            if (paymentMethod == null) {
                                paymentMethod = getNewPaymentMethod();
                            }
                            break;
                        case 4:
                            System.out.println("Your payment method: cash");
                            order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);
                            System.out.println("Payment was successful!");
                            paymentStatus = true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(choice!= 4 && paymentMethod != null){
                paymentStatus = payByCard(paymentMethod);
            }
            return paymentStatus;
        }
    }

    public boolean payByCard(PaymentMethod paymentMethod) {
        System.out.println("Your payment method: " + paymentMethod.getCardType());
        int counter = 3;
        while (counter-- > 0 && !paymentStatus) {
            if(counter == 0){
                OnlineMarketDemo.currentUser.getAccount().setActive(false);
                System.out.println("Your account was blocked due to 3 unsuccessful password entry attempts");
                System.out.println("Please, visit a nearest bank to re-activate your account!");
                return false;
            }

            System.out.print("Enter first two digits of your card password: ");
            String password = scanner.next();
            if (password.length() == 2 && user.getAccount().getPassword().toString().substring(0,2).equals(password)) {
                if (user.getAccount().getBalance() > order.getTotalPrice()) {
                    user.getAccount().addBalance(-1 * order.getTotalPrice());
                    order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);
                    System.out.println("Payment was successful!");
                    paymentStatus = true;
                } else {
                    System.out.println("Not enough money in your balance. Please, use a different payment method!");
                    order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
                }
            } else {
                System.out.println("WRONG PASSWORD!!! " +
                        "\nAfter " + counter + " more unsuccessful " +
                        "attempts your account will be blocked automatically!");
            }
        }
        return paymentStatus;
    }


    /**
     * Get new payment data from user
     * @return payment method such as card payment with all necessary information
     */
    public PaymentMethod getNewPaymentMethod(){
        PaymentMethod paymentMethod = null;
        Long cardNum;
        Integer month = 0, year = 0;
        String cardHolderName;
        CardType cardType;
        int counter = 3;
        boolean savePaymentInfo = false;
        while (paymentMethod == null && --counter > 0) {
            try {
                scanner = new Scanner(System.in);
                System.out.print("Enter your card number: ");
                cardNum = scanner.nextLong();
                //System.out.print("Enter last two digits of your card password: ");
                //password = scanner.nextInt();
                if (cardNum.toString().startsWith("8600")) {
                    cardType = CardType.UzCard;
                } else if (cardNum.toString().startsWith("4")) {
                    cardType = CardType.VISA;
                } else if (cardNum.toString().startsWith("5")) {
                    cardType = CardType.Master;
                } else {
                    cardType = CardType.Humo;
                }
                scanner = new Scanner(System.in);
                System.out.print("Enter the name on your card: ");
                cardHolderName = scanner.nextLine();
                scanner = new Scanner(System.in);
                System.out.print("Enter the expiration month: ");
                month = scanner.nextInt();
                //int tries = 3;
                //System.out.println("Please, keep in mind that after 3 unsuccessfull attempts, your bank account will be blocked! You have " + tries + " left attempts!");
                while (month < 1 || month > 12) {
                    System.out.println("Incorrect month entry!");
                    System.out.print("Enter the expiration month: ");
                    month = scanner.nextInt();

                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                int currentYear = Integer.parseInt(format.format(new Date()).split("-")[0]);
                int currentMonth = Integer.parseInt(format.format(new Date()).split("-")[1]);
                System.out.print("Enter the expiration year: ");
                year = scanner.nextInt();
                while (year < currentYear || (year == currentYear && month < currentMonth)) {
                    System.out.println("Incorrect year entry!");
                    System.out.print("Enter the expiration year: ");
                    year = scanner.nextInt();
                }
                System.out.print("Do you want to save payment info for future use? 'Y' for 'yes': ");
                scanner = new Scanner(System.in);
                String userResponse = scanner.next();
                savePaymentInfo = userResponse.contains("Y") || userResponse.contains("y");

                paymentMethod = new PaymentMethod(
                        cardHolderName,
                        cardNum,
                        month,
                        year,
                        cardType
                );
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("There was an error in input, please try again!");
            }
        }

        if(savePaymentInfo){
            OnlineMarketDemo.currentUser.addPaymentMethod(paymentMethod);
        }
        return paymentMethod;
    }
}
