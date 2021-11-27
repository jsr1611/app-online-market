package realization;

import enums.Role;
import model.*;
import service.CategoryService;
import service.DemonstrationService;
import service.RegistrationService;
import service.implement.CategoryServiceImpl;
import service.implement.DemonstrationServiceImpl;
import service.implement.RegistrationServiceIml;

import java.util.*;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:19 PM
 */
public class OnlineMarketDemo {

    public static Scanner scanner = new Scanner(System.in);
    public static Set<User> users = new HashSet<>();                        // DONE
    public static List<Category> categories = new ArrayList<>();
    public static Map<Category, Category> subCategories = new HashMap<>();
    public static Map<Product, Integer> products = new HashMap<>();     // Map<Product, Stock>
                                                            // stock = quantity available in stock
    /**
     * orders, orderDetails, shoppingCarts - larga RUNTIME da load bo'ladi
     */
    public static List<Order> orders = new ArrayList<>();
    public static List<OrderDetails> orderDetails = new ArrayList<>();
    public static List<ShoppingCart> shoppingCarts = new ArrayList<>();

    public static Map<Product, Integer> tempMap = new LinkedHashMap<>();

    static RegistrationService registrationService;
    static DemonstrationService demonstrationService;
    static CategoryService categoryService = new CategoryServiceImpl();

    public static User currentUser;

    public static void main(String[] args) {

        users.add(new User(1L, "Jumanazar Saidov", "js@gmail.com", "js123",
                Role.CUSTOMER, new Account(1000_11_00001L, 1234)));
        users.add(new User(2L, "Ali Shermat", "ali@gmail.com", "ali123",
                Role.SALESMAN, new Account(1000_22_00001L,1122)));
        users.add(new User(3L, "Jamshid Babajon", "jama@gmail.com", "jama123",
                Role.DIRECTOR, new Account(1000_33_00001L, 1111)));
        users.add(new User(4L, "Komil Alimov", "komil@gmail.com","komil123",
                Role.MANAGER, new Account(1002_33_00002L,2222)));

        Category electronics = new Category(1L, "Electronics", "Phones, iPhones, PCs, Notebooks, Cameras, Smartwatches, etc." );
        Category food = new Category(2L, "Food", "Fruits, vegetables, other food products, etc. ");

        Category phones = new Category(1L, "Phones", "Smartphones, iPhones, etc.");
        Category pc = new Category(2L, "PC", "Laptops, Notebooks, PCs, etc. ");
        Category camera = new Category(3L, "Cameras", "Digital cameras");

        Category fruits = new Category(1L, "Fruits", "Apples, bananas, grapes, other fruits");


        categoryService.addCategory(electronics);
        categoryService.addCategory(food);

        // key: sub-category, value: main category
        subCategories.put(pc, electronics);
        subCategories.put(phones, electronics);
        subCategories.put(camera, electronics);

        subCategories.put(fruits, food);

        // adding 10 pcs of iPhone 13 of phones sub-category inside electronics big category which is 1300$ each
        products.put(
                new Product(1L, "iPhone 13", electronics, phones, 1300.0),
                10);
        products.put(
                new Product(2L, "Samsung Laptop", electronics, pc, 1500.0 ),
                10);

        int choice;
        do {
            showMainMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    signIn();
                    break;
                case 2:
                    signUp();
                    break;
                default:
                    System.out.println("Incorrect option!");
            }

        } while (choice != 0);
    }

    private static void signUp() {
        registrationService = new RegistrationServiceIml();
        boolean isSuccess = registrationService.signUp();
        if (isSuccess)
            // TODO: 11/20/2021 will write logic here
            System.out.println("Success!");
        else
            System.out.println("Something went wrong!");
    }

    private static void signIn() {
        registrationService = new RegistrationServiceIml();
        boolean isSuccess = registrationService.signIn();
        currentUser.setSignedIn(isSuccess);
        if (isSuccess) {
            demonstrationService = new DemonstrationServiceImpl();
            while (currentUser.getSignedIn()) {
                switch (currentUser.getRole()) {
                    case CUSTOMER:
                        demonstrationService.showCustomerMenu();
                        break;

                    case SALESMAN:
                        demonstrationService.showSalesmanMenu();
                        break;

                    case MANAGER:
                        demonstrationService.showManagerMenu();
                        break;

                    case DIRECTOR:
                        demonstrationService.showDirectorMenu();
                        break;
                }
            }
        } else {
            System.out.println("User not found!");
        }
    }

    private static void showMainMenu() {
        System.out.println("Menu");
        System.out.println("1. Sign In");
        System.out.println("2. Sign Up");
        System.out.println("0. Exit");
    }


}
