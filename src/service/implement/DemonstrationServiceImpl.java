package service.implement;

import enums.OrderStatus;
import model.*;
import realization.OnlineMarketDemo;
import service.CategoryService;
import service.DemonstrationService;
import service.ProductService;

import java.util.*;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 8:01 PM
 */
public class DemonstrationServiceImpl implements DemonstrationService {

    static Scanner scanner;
    static CategoryService categoryService = new CategoryServiceImpl();
    static ProductService productService = new ProductServiceImpl();
    static OrderOperationServiceImpl orderService = new OrderOperationServiceImpl();


    @Override
    public void showCustomerMenu() {

        Long cartId = OnlineMarketDemo.currentUser.getId();
        scanner = new Scanner(System.in);
        List<Category> categories = OnlineMarketDemo.categories;

        Map<Product, Integer> productsToBuy = new HashMap<>();

        ShoppingCart mycart = new ShoppingCart(cartId);

        Double totalAmount = 0.0;
        Order myOrder = new Order(cartId, OnlineMarketDemo.currentUser, OrderStatus.NEW, totalAmount);
        OrderDetails myOrderDetails = new OrderDetails(cartId, myOrder, productsToBuy);

        if(!OnlineMarketDemo.shoppingCarts.contains(mycart)){
            OnlineMarketDemo.shoppingCarts.add(mycart);
        }


        while (OnlineMarketDemo.currentUser.getSignedIn()) {
            System.out.println("============ CUSTOMER FORM ==============");
            System.out.println("PlEASE CHOOSE CATEGORY:");
            for (Category category : categories) {
                System.out.println(category.getId() + ". " + category.getName());
            }
            boolean cancelOption = false;
            System.out.println("+ Choose All   - Cancel   v - View Shopping Cart   o - My Orders");
            System.out.println("=========================================");
                System.out.print("Choose: ");
                String choiceStr = scanner.next();
                int choice = -1; //scanner.nextInt();
                long innerChoice = -1L;
                for(Character c : choiceStr.toCharArray()){
                    if(Character.isDigit(c)){
                       choice = Integer.parseInt(choiceStr);
                    }
                }
                if(choice == -1){
                    switch (choiceStr){
                        case "+":
                            System.out.println("Index\tName\t\t\t\t\t\tQuantity");
                            for (Product prod : OnlineMarketDemo.products) {
                                System.out.println(
                                        prod.getId() + ".\t\t\t" +
                                                prod.getName() + "\t\t\t\t" +
                                                prod.getQuantity());
                            }
                            break;
                        case "-":
                            cancelOption = true;
                            break;
                        case "v":
                            showShoppingCart(mycart);
                            break;
                        case "o":
                            showOrders(myOrder);
                            //System.out.println(myOrder);
                            break;
                        default:
                            System.out.println("No such a case! Choose one of '+', '-', 'v', 'o' " +
                                    "or product id from the list.");
                    }
                }
                else {
                    Category userCategory = categories.get(choice - 1);
                    System.out.println("Index\tName\t\t\t\t\t\tQuantity");
                    for (Product prod : OnlineMarketDemo.products) {
                        if (categories.contains(userCategory)) {
                            System.out.println(
                                    prod.getId() + ".\t\t\t" +
                                            prod.getName() + "\t\t\t\t" +
                                            prod.getQuantity());
                        }
                    }
                }
                if(cancelOption){
                    break;
                }
                else {
                    System.out.print("Enter product id to add it to shopping cart: ");
                    scanner = new Scanner(System.in);

                    choiceStr = scanner.next();

                    if(choiceStr.equals("-")){
                        break;
                    }
                    else  if(choiceStr.equals("v")){
                        showShoppingCart(mycart);
                        break;
                    }
                    else if(choiceStr.equals("o")){
                        showOrders(myOrder);
                        break;
                    }
                    else {
                        innerChoice = Integer.parseInt(choiceStr);
                    }

                    Product productToBuy = productService.findById(innerChoice);
                    System.out.print("Enter the quantity to buy: ");

                    double quantity = scanner.nextDouble();
                    Double stockAvailable = productToBuy.getQuantity();
                    int counter = 3;
                    while (quantity > stockAvailable && --counter > 0){
                        System.out.println("Not enough stock! You can buy only : " + stockAvailable + " (pcs)");
                        System.out.print("Enter the quantity to buy: ");
                        quantity = scanner.nextInt();
                    }

                    mycart.addProduct(productToBuy, (int)quantity);

                    totalAmount += quantity * productToBuy.getPrice();

                    Double cartTotalAmount = mycart.getTotalAmount();
                    mycart.setTotalAmount(cartTotalAmount + totalAmount);

                    Double orderAmount = myOrder.getTotalPrice();
                    myOrder.setTotalPrice(orderAmount + totalAmount);

                    orderService.setDetails(myOrderDetails);
                    productService.updateQuantity(productToBuy, (-1) * quantity);
                    //System.out.println("Product was added to the shopping cart");
                }

            }


    }

    @Override
    public void showOrders(Order myOrder) {

    }

    @Override
    public void showShoppingCart(ShoppingCart mycart) {


        System.out.println(mycart);
        System.out.print("Complete the payment? Enter 'Y' for 'yes', or any other word for 'no': ");
        String choiceStr = scanner.next();
        if(choiceStr.equals("Y") || choiceStr.equals("y")){
            orderService.setOrder(myOrder);
            boolean orderResult = orderService.completeOrder();
            if(orderResult){
                //order complete bo`lganidan keyingina qo`shilsin!
                if(!OnlineMarketDemo.orders.contains(myOrder)){
                    OnlineMarketDemo.orders.add(myOrder);
                }
                if(!OnlineMarketDemo.orderDetails.contains(myOrderDetails)){
                    OnlineMarketDemo.orderDetails.add(myOrderDetails);
                }

                OnlineMarketDemo.shoppingCarts.remove(mycart);
                mycart = new ShoppingCart(mycart.getId() + 1);
                System.out.println("Order is complete!");

                //next Action
                System.out.print("Choose next menu:");
                System.out.println("1. Return to Orders");
                System.out.println("2. Continue shopping");
                System.out.println("3. Sign out");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        showOrders();
                        break;
                    case 2:
                        //cancelOption = true;
                        break;
                    case 3:
                        //cancelOption = true;
                        OnlineMarketDemo.currentUser.setSignedIn(false);
                        break;
                }
            }
            else
                System.out.println("Payment was not successful.");
        }
    }

    @Override
    public void showSalesmanMenu() {
        scanner = new Scanner(System.in);
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Add Category");
        System.out.println("5. Edit Category");
        System.out.println("6. Delete Category");
        System.out.println("7. View All Categories" );
        System.out.println("8. View All Products");
        System.out.println("0. Sign out");
        // add more menus for salesman

        int choice = scanner.nextInt();
        long innerChoice = 0L;
        switch (choice) {
            case 1:             // add product
                System.out.print("Name: ");
                scanner = new Scanner(System.in);
                String name = scanner.nextLine();
                Category category =  null;
                Category subCategory = null;
                System.out.println("Please, choose a category: ");

                for(Category cat: OnlineMarketDemo.categories){
                    System.out.println(cat.getId() + ". " + cat.getName());
                }
                int numCategories = OnlineMarketDemo.categories.size();
                System.out.println(( numCategories + 1) + ". New");
                scanner = new Scanner(System.in);
                innerChoice = scanner.nextInt();
                if(innerChoice > OnlineMarketDemo.categories.size()){
                    System.out.println("---NEW CATEGORY ENTRY---");
                    scanner = new Scanner(System.in);
                    System.out.print("Name: ");
                    String catName = scanner.nextLine();
                    System.out.println("Description: ");
                    String catDesc = scanner.nextLine();
                    Long catId = OnlineMarketDemo.categories.size() + 1L;
                    category = new Category(catId, catName, catDesc);
                    categoryService.addCategory(category);

                }else {
                    category = categoryService.findById(innerChoice-1);
                }


                // Subcategory selection or new subcategory entry --- buni yanada yaxshiroq o'ylab ko'rish kerak
//                if(OnlineMarketDemo.subCategories.containsKey(category)){
//                    Collection<Category> subCats = OnlineMarketDemo.subCategories.values();
//                    index = 1;
//                    for(Category subCat : subCats){
//                        System.out.println(index + ". " + subCat.getName());
//                        index++;
//                    }
//                    System.out.println(index + ". New");
//
//                }
                System.out.print("Price: ");
                Double price = scanner.nextDouble();
                System.out.print("Quantity: ");
                Double quantity = scanner.nextDouble();
                Long prodId = OnlineMarketDemo.products.size()+1L;
                Product product = new Product(
                        prodId,
                        name,
                        category,
                        null,
                        price,
                        quantity
                );

                productService.addProduct(product);
                break;
            case 2:             // update product price
            case 3:              // delete product
                // Ikkita case (holat) ni bir joyda boshqarish
                // o'xshashlik yuqori bo`lgani uchun shunday qildik!

                for (Product product1 : OnlineMarketDemo.products) {
                    System.out.println(product1.getId() + ". " + product1.getName() + " \t|\t " + product1.getQuantity());
                }
                System.out.print("Choose product index: ");
                innerChoice = scanner.nextInt();
                Product productToUpdate = productService.findById(innerChoice);
                System.out.println(productToUpdate);
                if(choice == 2) {
                    System.out.print("Enter new price: ");
                    Double newPrice = scanner.nextDouble();
                    productToUpdate.setPrice(newPrice);
                    System.out.println("Successfully updated.");
                    for (Product product1 : OnlineMarketDemo.products) {
                        System.out.println(product1.getId() + ". " + product1.getName() + " \t|\t " + product1.getQuantity());
                    }
                }
                else {
                    System.out.println("---------------------");
                    System.out.println(productService.findById(innerChoice));
                    System.out.print("Would you really like to delete this product?");
                    productService.deleteProduct(innerChoice);
                    System.out.println("Successfully deleted.");
                    for (Product product1 : OnlineMarketDemo.products) {
                        System.out.println(product1.getId() + ". " + product1.getName() + "\t | \t " + product1.getQuantity());
                    }
                }
                break;
            case 0:
                OnlineMarketDemo.currentUser.setSignedIn(false);
                System.out.println("Signed out.");
                break;
        }

    }

    @Override
    public void showManagerMenu() {

    }

    @Override
    public void showDirectorMenu() {

    }
}
