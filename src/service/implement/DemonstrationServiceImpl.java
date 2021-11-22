package service.implement;

import model.Category;
import model.Product;
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

    @Override
    public void showCustomerMenu() {
        scanner = new Scanner(System.in);
        List<Category> categories = OnlineMarketDemo.categories;

        System.out.println("============ CUSTOMER FORM ==============");
        System.out.println("PlEASE CHOOSE CATEGORY:");
        for (Category category : categories) {
            System.out.println(category.getId() + ". " + category.getName());
        }
        System.out.println("+ Choose All   - Cancel   v - View Shopping Cart   o - My Orders");
        System.out.println("=========================================");
        System.out.println("Choose:");
        int choice = scanner.nextInt();
        Category userCategory = categories.get(choice-1);
        for(Map.Entry<Integer, Product> prod : OnlineMarketDemo.products.entrySet()){
            if(categories.contains(userCategory)){
                System.out.println(prod.getKey() + ". " + prod.getValue().getName());
            }
        }
    }

    @Override
    public void showSalesmanMenu() {
        scanner = new Scanner(System.in);

        System.out.println("1. Add Product");
        // add more menus for salesman
        // 2. Change Price
        // 3. Delete Product
        // 4. ...
        int choice = scanner.nextInt();
        /**
         *
     *      private Long id;
         *     private String name;
         *     private Category category;
         *     private Category subCategory;
         *     private Double price;
         *
         */
        int innerChoice = 0;
        switch (choice) {
            case 1:
                System.out.print("Name: ");
                scanner = new Scanner(System.in);
                String name = scanner.nextLine();
                Category category =  null;
                Category subCategory = null;
                System.out.println("Please, choose a category: ");
                int index = 1;
                for(Category cat: OnlineMarketDemo.categories){
                    System.out.println(index + ". " + cat.getName());
                    index++;
                }
                System.out.println(index + ". New");
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
                    category = OnlineMarketDemo.categories.get(innerChoice-1);
                }

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
        }

    }

    @Override
    public void showManagerMenu() {

    }

    @Override
    public void showDirectorMenu() {

    }
}
