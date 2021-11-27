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
        Order myOrder = new Order(cartId, OnlineMarketDemo.currentUser, OrderStatus.NEW, 0.0);
        OrderDetails myOrderDetails = new OrderDetails(cartId, myOrder, productsToBuy);

        if(!OnlineMarketDemo.shoppingCarts.contains(mycart)){
            OnlineMarketDemo.shoppingCarts.add(mycart);
        }
        if(!OnlineMarketDemo.orders.contains(myOrder)){
            OnlineMarketDemo.orders.add(myOrder);
        }
        if(!OnlineMarketDemo.orderDetails.contains(myOrderDetails)){
            OnlineMarketDemo.orderDetails.add(myOrderDetails);
        }

        while (true) {
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
                            System.out.println("Index\tName\t\t\t\tQuantity");
                            for (Map.Entry<Product, Integer> prod : OnlineMarketDemo.products.entrySet()) {
                                System.out.println(
                                        prod.getKey().getId() + ".\t\t" +
                                        prod.getKey().getName() + "\t\t\t\t" +
                                        prod.getValue());
                            }
                            break;
                        case "-":
                            cancelOption = true;
                            break;
                        case "v":
                            System.out.println(mycart);
                            System.out.print("Complete the payment? Enter 'Y' for 'yes', or any other word for 'no': ");
                            choiceStr = scanner.next();
                            if(choiceStr.equals("Y")){
                                orderService.setOrder(myOrder);
                                boolean orderResult = orderService.completeOrder();
                                if(orderResult)
                                    System.out.println("Order is complete!");
                            }
                            break;
                        case "o":
                            System.out.println(myOrder);
                            break;
                        default:
                            System.out.println("No such a case! Choose one of '+', '-', 'v', 'o' " +
                                    "or product id from the list.");
                    }
                }
                else {
                    Category userCategory = categories.get(choice - 1);
                    System.out.println("Index\tName\t\t\t\tQuantity");
                    for (Map.Entry<Product, Integer> prod : OnlineMarketDemo.products.entrySet()) {
                        if (categories.contains(userCategory)) {
                            System.out.println(
                                    prod.getKey().getId() + ".\t\t" +
                                            prod.getKey().getName() + "\t\t\t\t" +
                                            prod.getValue());
                        }
                    }
                }
                if(cancelOption){
                    break;
                }
                else {
                    System.out.print("Enter product id to add it to shopping cart: ");
                    innerChoice = scanner.nextInt();
                    Product productToBuy = productService.findById(innerChoice);
                    System.out.print("Enter the quantity to buy: ");
                    int quantity = scanner.nextInt();
                    mycart.addProduct(productToBuy, quantity);
                    System.out.println("Product was added to the shopping cart");
                }

            }



    }

    @Override
    public void showSalesmanMenu() {
        scanner = new Scanner(System.in);
        System.out.println("1. Add Product");
        System.out.println("2. Update Price");
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
                Long prodId = OnlineMarketDemo.products.size()+1L;
                Product product = new Product(
                        prodId,
                        name,
                        category,
                        null,
                        price
                );

                productService.addProduct(product);
                break;
            case 2:             // update product price
            case 3:              // delete product
                // Ikkita case (holat) ni bir joyda boshqarish
                // o'xshashlik yuqori bo`lgani uchun shunday qildik!

                OnlineMarketDemo.products.forEach((key, val) -> {
                    System.out.println(key + ". " + val);
                });
                System.out.print("Choose product index: ");
                innerChoice = scanner.nextInt();
                Product productToUpdate = productService.findById(innerChoice);
                System.out.println(productToUpdate);
                if(choice == 2) {
                    System.out.print("Enter new price: ");
                    Double newPrice = scanner.nextDouble();
                    productToUpdate.setPrice(newPrice);
                    System.out.println("Successfully updated.");
                    OnlineMarketDemo.products.forEach((key, val) -> {
                        System.out.println(key + ". " + val);
                    });
                }
                else {
                    System.out.println("---------------------");
                    System.out.println(productService.findById(innerChoice));
                    System.out.print("Would you really like to delete this product?");
                    productService.deleteProduct(innerChoice);
                    System.out.println("Successfully deleted.");
                    OnlineMarketDemo.products.forEach((key, val) -> {
                        System.out.println(key + ". " + val);
                    });
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
