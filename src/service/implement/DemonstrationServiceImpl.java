package service.implement;

import enums.OrderStatus;
import enums.Role;
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
    Order myOrder;
    OrderDetails myOrderDetails;
    ShoppingCart myCart;
    List<Category> categories; // = OnlineMarketDemo.categories;
    Map<Product, Double> availableProducts; // = OnlineMarketDemo.products;


    @Override
    public void showCustomerMenu() {

        if(categories == null){
            categories = OnlineMarketDemo.categories;
        }
        if(availableProducts == null){
            availableProducts = OnlineMarketDemo.products;
        }

        Long customerId = OnlineMarketDemo.currentUser.getId();
        Map<Product, Double> productsToBuy = null;


        Long cartId = null;
        Double totalAmount = null;
        boolean userHasCart = false;
        for (ShoppingCart shoppingCart : OnlineMarketDemo.shoppingCarts) {
            if (shoppingCart.getCustomerId().equals(customerId)) {
                userHasCart = true;
                cartId = shoppingCart.getId();
                myCart = shoppingCart;
                productsToBuy = shoppingCart.getProducts();
                totalAmount = shoppingCart.getTotalAmount();
            }
        }

        if (!userHasCart) {
            cartId = OnlineMarketDemo.shoppingCarts.size() + 1L;
            myCart = new ShoppingCart(cartId, customerId);
            productsToBuy = new HashMap<>();
            totalAmount = 0.0;
            OnlineMarketDemo.shoppingCarts.add(myCart);
        }
        Long orderId = OnlineMarketDemo.orders.size() + 1L;
        scanner = new Scanner(System.in);



        myOrder = new Order(orderId, OnlineMarketDemo.currentUser, OrderStatus.NEW, totalAmount);
        myOrderDetails = new OrderDetails(orderId, myOrder, productsToBuy);

        while (OnlineMarketDemo.currentUser.getSignedIn()) {
            System.out.println("============ CUSTOMER FORM ==============");
            printCategoryInfo();
            boolean cancelOption = false;
            System.out.println("+ Choose All   - Cancel   v - View Shopping Cart   o - My Orders");
            System.out.println("=========================================");
            System.out.print("Choose: ");
            String choiceStr = scanner.next();
            int choice = -1; //scanner.nextInt();
            long innerChoice = -1L;
            for (Character c : choiceStr.toCharArray()) {
                if (Character.isDigit(c)) {
                    choice = Integer.parseInt(choiceStr);
                }
            }
            if (choice == -1) {
                switch (choiceStr) {
                    case "+":
                        availableProducts = printProductInfo();
                        break;
                    case "-":
                    case "v":
                    case "o":
                        if(choiceStr.equals("v"))
                            showShoppingCart(myCart, Role.CUSTOMER);
                        else if(choiceStr.equals("o"))
                            showOrders(myOrder, Role.CUSTOMER);
                        cancelOption = true;
                        break;
                    default:
                        System.out.println("No such a case! Choose one of '+', '-', 'v', 'o' " +
                                "or product id from the list.");
                }
            } else {
                Category userCategory = categories.get(choice - 1);
                availableProducts = printProductInfo(userCategory);
            }
            if (cancelOption) {
                break;
            } else {
                if(!availableProducts.isEmpty()) {
                    System.out.print("Enter product id to add it to shopping cart or anything else to return to previous menu: ");
                    scanner = new Scanner(System.in);
                    choiceStr = scanner.next();
                    try {
                        innerChoice = Integer.parseInt(choiceStr);
                        Map.Entry<Product, Double> productToBuy = productService.findById(innerChoice);
                        if (productToBuy != null) {
                            System.out.print("Enter the quantity to buy: ");

                            double quantity = scanner.nextDouble();
                            Double stockAvailable = productToBuy.getValue();
                            int counter = 3;
                            while (quantity > stockAvailable && --counter > 0) {
                                System.out.println("Not enough stock! You can buy only : " + stockAvailable + " (pcs)");
                                System.out.print("Enter the quantity to buy: ");
                                quantity = scanner.nextInt();
                            }

                            myCart.addProduct(productToBuy.getKey(), quantity);
                            totalAmount += quantity * productToBuy.getKey().getPrice();
                            Double cartTotalAmount = myCart.getTotalAmount();
                            myCart.setTotalAmount(cartTotalAmount + totalAmount);

                            Double orderAmount = myOrder.getTotalPrice();
                            myOrder.setTotalPrice(orderAmount + totalAmount);

                            orderService.setDetails(myOrderDetails);
                            productService.updateQuantity(productToBuy.getKey(), (-1) * quantity);
                        }
                        //System.out.println("Product was added to the shopping cart");
                    } catch (Exception e) {
                        // return to previous menu
                    }
                }
                else {
                    System.out.println("No products available in this category.");
                }
            }
        }


    }


    @Override
    public void showOrders(Order myOrder, Role role) {
        scanner = new Scanner(System.in);
        if (role.equals(Role.CUSTOMER)) {
            for (OrderDetails orderDetail : OnlineMarketDemo.orderDetails) {
                if (orderDetail.getOrder().getCustomer().equals(OnlineMarketDemo.currentUser)) {
                    System.out.println(orderDetail);
                }
            }
        } else if (role.equals(Role.DIRECTOR)) {
            for (Order order : OnlineMarketDemo.orders) {
                System.out.println(order);
            }
        } else if (role.equals(Role.SALESMAN)) {
            for (OrderDetails orderDetail : OnlineMarketDemo.orderDetails) {
                for (Map.Entry<Product, Double> productDoubleEntry : orderDetail.getProducts().entrySet()) {
                    if (productDoubleEntry.getKey().getSeller().equals(OnlineMarketDemo.currentUser)) {
                        System.out.println(orderDetail.getOrder());
                    }
                }
            }
        }

        System.out.print("Enter 'so' to sign out, or anything else continue:");
        String userOption = scanner.next();
        if (userOption.equals("so")) {
            OnlineMarketDemo.currentUser.setSignedIn(false);
        }
    }

    @Override
    public void showShoppingCart(ShoppingCart mycart, Role userRole) {
        if (userRole.equals(Role.CUSTOMER)) {
            if (mycart.getProducts().size() > 0) {
                System.out.println(mycart);
                System.out.print("Complete the payment? Enter 'Y' for 'yes', or any other word for 'no': ");
                String choiceStr = scanner.next();
                if (choiceStr.equals("Y") || choiceStr.equals("y")) {
                    boolean orderResult = orderService.completeOrder();
                    if (orderResult) {
                        //order complete bo`lganidan keyingina qo`shilsin!
                        if (!OnlineMarketDemo.orders.contains(myOrder)) {
                            OnlineMarketDemo.orders.add(myOrder);
                        }
                        if (!OnlineMarketDemo.orderDetails.contains(myOrderDetails)) {
                            OnlineMarketDemo.orderDetails.add(myOrderDetails);
                        }

                        OnlineMarketDemo.shoppingCarts.remove(mycart);
                        myCart = new ShoppingCart(mycart.getId() + 1, OnlineMarketDemo.currentUser.getId());
                        System.out.println("==================================");
                        System.out.println("    YOUR ORDER IS COMPLETE!      ");
                        System.out.println("==================================");


                        //next Action

                        System.out.println("1. Print Order Info");
                        System.out.println("2. Continue shopping");
                        System.out.println("3. Sign out");
                        System.out.print("Choose next menu:");
                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                printOrderInfo(OnlineMarketDemo.currentUser, userRole);
                                break;
                            case 2:
                                //cancelOption = true;
                                break;
                            case 3:
                                //cancelOption = true;
                                OnlineMarketDemo.currentUser.setSignedIn(false);
                                break;
                        }
                    } else
                        System.out.println("Payment was not successful.");
                }
            } else {
                System.out.println("Your shopping cart is empty!");
            }
        } else if (userRole.equals(Role.SALESMAN)) {
            for (ShoppingCart shoppingCart : OnlineMarketDemo.shoppingCarts) {
                if (shoppingCart.getProducts().size() > 0) {
                    System.out.println(shoppingCart);
                } else {
                    System.out.println("Customer with id (" + shoppingCart.getCustomerId() +
                            ") has been checking your products " +
                            "but has not added any products yet.");
                }
            }
        }

    }


    @Override
    public void showSalesmanMenu() {
        while (OnlineMarketDemo.currentUser.getSignedIn()) {
            System.out.println("============ SALESMAN FORM ==============");
            scanner = new Scanner(System.in);
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Info");
            System.out.println("3. Delete Product");
            System.out.println("4. Add Category");
            System.out.println("5. Edit Category");
            System.out.println("6. Delete Category");
            System.out.println("7. View All Categories");
            System.out.println("8. View All Products");
            System.out.println("9. View All Orders");
            System.out.println("10. View All Customers");
            System.out.println("0. Sign out");
            System.out.print("Choose: ");
            // add more menus for salesman

            int choice = scanner.nextInt();
            long innerChoice = 0L;
            switch (choice) {
                case 1:             // add product
                    System.out.print("Name: ");
                    scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    Category category = null;
                    Category subCategory = null;
                    System.out.println("Please, choose a category: ");

                    for (Category cat : OnlineMarketDemo.categories) {
                        System.out.println(cat.getId() + ". " + cat.getName());
                    }
                    int numCategories = OnlineMarketDemo.categories.size();
                    System.out.println((numCategories + 1) + ". New");
                    scanner = new Scanner(System.in);
                    innerChoice = scanner.nextInt();
                    if (innerChoice > OnlineMarketDemo.categories.size()) {
                        category = addNewCategory();

                    } else {
                        category = categoryService.findById(innerChoice - 1);
                    }

                    // TODO: 11/30/2021 Subcategory logic needs to be put here
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
                    Long prodId = OnlineMarketDemo.products.size() + 1L;
                    Product product = new Product(
                            prodId,
                            name,
                            category,
                            null,
                            price,
                            OnlineMarketDemo.currentUser
                    );
                    productService.addProduct(product, quantity);
                    System.out.println("Product " + product.getName() + " ( quantity: " + quantity+  ") was successfully added to the list.");
                    break;
                case 2:             // update product price
                case 3:              // delete product
                    // Ikkita case (holat) ni bir joyda boshqarish
                    // o'xshashlik yuqori bo`lgani uchun shunday qildik!

                    printProductInfo();
                    System.out.print("Choose product index: ");
                    innerChoice = scanner.nextInt();
                    Map.Entry<Product, Double> productToUpdate = productService.findById(innerChoice);
                    System.out.println(productToUpdate);
                    if (choice == 2) {
                        System.out.print("Enter new price: ");
                        Double newPrice = scanner.nextDouble();
                        productToUpdate.getKey().setPrice(newPrice);
                        System.out.println("Successfully updated.");
                        printProductInfo();
                    } else {
                        System.out.println("---------------------");
                        System.out.println(productService.findById(innerChoice));
                        System.out.print("Would you really like to delete this product? Enter Y for yes, or anything else for no:");
                        scanner = new Scanner(System.in);
                        String response = scanner.next();
                        if (response.equals("Y") || response.equals("y")) {
                            productService.deleteProduct(innerChoice);
                            System.out.println("Successfully deleted.");
                        }
                        printProductInfo();
                    }
                    break;
                case 4:
                    addNewCategory();
                    break;
                case 5:
                    printCategoryInfo();
                    System.out.print("Choose category id to be edited:");
                    innerChoice = scanner.nextInt();
                    Category categoryToEdit = categoryService.findById(innerChoice);
                    System.out.println("Enter new name below or leave it empty for no changes");
                    System.out.print("Name:");
                    scanner = new Scanner(System.in);
                    String catName = scanner.nextLine();
                    if (!catName.isEmpty()) {
                        categoryToEdit.setName(catName);
                    }

                    System.out.println("Enter below new description or leave it empty for no changes");
                    String catDescription = scanner.nextLine();
                    if (!catDescription.isEmpty()) {
                        categoryToEdit.setDescription(catDescription);
                    }
                    if (catName.isEmpty() && catDescription.isEmpty()) {
                        System.out.println("No changes were made.");
                    } else {
                        System.out.println("Category was successfully edited.");
                    }
                    System.out.println(categoryToEdit);
                    break;
                case 6:
                    printCategoryInfo();
                    System.out.print("Choose category id to be deleted:");
                    innerChoice = scanner.nextInt();
                    Category categoryToBeDeleted = categoryService.findById(innerChoice);
                    System.out.println("Are you sure you want to delete category " + categoryToBeDeleted.getName() + "? Enter Y for yes, or anything else for no:");
                    scanner = new Scanner(System.in);
                    String response = scanner.next();
                    if (response.equals("Y") || response.equals("y")) {
                        categoryService.deleteCategory(innerChoice);
                        System.out.printf("Category %s was successfully deleted.\n", categoryToBeDeleted.getName());
                    }

                    break;
                case 7:
                    printCategoryInfo();
                    break;
                case 8:
                    printProductInfo(OnlineMarketDemo.currentUser);
                    break;
                case 9:
                    printOrderInfo(OnlineMarketDemo.currentUser, Role.SALESMAN);
                    break;
                case 10:
                    // TODO: 11/30/2021 Need to improve the printing format
                    System.out.println("ID \t|\t Name \t|\t Order Amount \t|\t");
                    for (Order order : OnlineMarketDemo.orders) {
                        User customer = order.getCustomer();
                        // TODO: 11/30/21 need to create customer list for the salesman
                        System.out.println(customer.getId() + " \t|\t " + customer.getFullName() + " \t|\t " + order.getTotalPrice());
                    }
                    break;
                case 0:
                    OnlineMarketDemo.currentUser.setSignedIn(false);
                    System.out.println("Signed out.");
                    break;
                default:
                    System.out.println("No such a case! Wrong entry.");
            }
        }

    }


    @Override
    public void showManagerMenu() {

    }

    @Override
    public void showDirectorMenu() {

    }


    /**
     * Print order details for all orders for the given user role
     *
     * @param user user
     * @param role customer or salesman
     */
    public void printOrderInfo(User user, Role role) {
        System.out.println("============= ORDER INFO ===============");
        int counter = 0;
        for (OrderDetails orderDetail : OnlineMarketDemo.orderDetails) {
            if (role.equals(Role.SALESMAN)) {
                for (Map.Entry<Product, Double> productQtyEntry : orderDetail.getProducts().entrySet()) {
                    if (productQtyEntry.getKey().getSeller().equals(user)) {
                        System.out.println(orderDetail);
                        counter++;
                    }
                }

            } else if (role.equals(Role.CUSTOMER)) {
                if (orderDetail.getOrder().getCustomer().equals(user)) {
                    // TODO: 11/30/2021 needs to be overridden to print in a custom format
                    System.out.println(orderDetail);
                    counter++;
                }
            }
        }
        if(counter == 0){
            System.out.println("No orders were placed so far.");
        }
    }


    /**
     * Print product ID, name, and quantity for all products in all categories
     */
    public Map<Product, Double> printProductInfo() {
        System.out.println("============ PRODUCTS ==============");
        System.out.println(String.format("%1$-6s", "Index") + " |\t"+ String.format("%1$-15s", "Name") + " | Available in stock (pcs)");
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            System.out.println(String.format("%1$-6s", prodWithQty.getKey().getId()) + " | " + String.format("%1$-15s", prodWithQty.getKey().getName())  + " | " + prodWithQty.getValue());
        }
        return OnlineMarketDemo.products;
    }


    /**
     * Print product ID, name, and quantity for all products in the given category
     */
    public Map<Product, Double> printProductInfo(Category userCategory) {
        Map<Product, Double> productsCategory = new HashMap<>();
        System.out.println("============ PRODUCTS ==============");
        System.out.println(String.format("%1$-6s", "Index") + " |\t"+ String.format("%1$-15s", "Name") + " | Available in stock (pcs)");
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if (userCategory.equals(prodWithQty.getKey().getCategory())) {
                productsCategory.put(prodWithQty.getKey(), prodWithQty.getValue());
                System.out.println(String.format("%1$-6s", prodWithQty.getKey().getId()) + " | " + String.format("%1$-15s", prodWithQty.getKey().getName())  + " | " + prodWithQty.getValue());
            }
        }
        return productsCategory;
    }

    /**
     * Print product ID, name, and quantity for all products in all categories for the given seller
     */
    public Map<Product, Double> printProductInfo(User seller) {
        Map<Product, Double> productsSeller = new HashMap<>();
        System.out.println("============ PRODUCTS ==============");
        System.out.println(String.format("%1$-6s", "Index") + " | "+ String.format("%1$-15s", "Name") + " | Available in stock (pcs)");
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if (prodWithQty.getKey().getSeller().equals(seller)) {
                System.out.println(String.format("%1$-6s", prodWithQty.getKey().getId()) + " | " + String.format("%1$-15s", prodWithQty.getKey().getName())  + " | " + prodWithQty.getValue());
                productsSeller.put(prodWithQty.getKey(), prodWithQty.getValue());
            }
        }
        return productsSeller;
    }


    /**
     * Print product ID, name, and quantity for all products in the given category and seller
     */
    public Map<Product, Double> printProductInfo(User seller, Category category) {
        Map<Product, Double> productsSellerCategory = new HashMap<>();
        System.out.println("============ PRODUCTS ==============");
        System.out.println(String.format("%1$-6s", "Index") + " | "+ String.format("%1$-15s", "Name") + " | Available in stock (pcs)");
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if (prodWithQty.getKey().getCategory().equals(category) && prodWithQty.getKey().getSeller().equals(seller)) {
                System.out.println(String.format("%1$-6s", prodWithQty.getKey().getId()) + " | " + String.format("%1$-15s", prodWithQty.getKey().getName())  + " | " + prodWithQty.getValue());
                productsSellerCategory.put(prodWithQty.getKey(), prodWithQty.getValue());
            }
        }
        return productsSellerCategory;
    }

    public void printCategoryInfo() {
        System.out.println("========= ALL CATEGORIES ===========");
        System.out.println(String.format("%1$-6s", "Index") + " | "+ String.format("%1$-15s", "Name") + " | Description");
        for (Category category : OnlineMarketDemo.categories) {
            System.out.println(String.format("%1$-6s", category.getId()) + " | " + String.format("%1$-15s", category.getName())  + " | " + category.getDescription());
        }
    }

    public Category addNewCategory() {
        System.out.println("======= NEW CATEGORY ENTRY =========");
        scanner = new Scanner(System.in);
        String catName = "";
        int counter = 3;
        while (catName.isEmpty() && --counter > 0) {
            System.out.print("Category name: ");
            catName = scanner.nextLine();
            if (counter == 1) {
                catName = "New Category " + (OnlineMarketDemo.categories.size() + 1);
            }
        }
        String catDesc = "";
        counter = 3;
        while (catDesc.isEmpty() && --counter > 0) {
            System.out.println("Enter category description: ");
            catDesc = scanner.nextLine();
            if (counter == 1) {
                catDesc = "No description";
            }
        }

        Long catId = OnlineMarketDemo.categories.size() + 1L;
        Category category = new Category(catId, catName, catDesc);
        categoryService.addCategory(category);
        return category;
    }


}
