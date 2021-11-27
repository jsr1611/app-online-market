package service.implement;

import model.Category;
import realization.OnlineMarketDemo;
import service.CategoryService;

import java.util.Scanner;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:18 PM
 */
public class CategoryServiceImpl implements CategoryService {

    // TODO: 11/20/2021 collectionlarimizga ma'lumotlarni yuklash


    @Override
    public boolean addCategory(Category category) {
        if(!OnlineMarketDemo.categories.contains(category)){
            OnlineMarketDemo.categories.add(category);
            return true;
        }
        return false;
    }

    @Override
    public boolean editCategory(Category category) {
        Scanner scanner = new Scanner(System.in);
        if(OnlineMarketDemo.categories.contains(category)){
            System.out.println("----------EDIT CATEGORY----------");
            System.out.print("Enter new category name: ");
            String newName = scanner.nextLine();
            if(!newName.isEmpty())
                category.setName(newName);
            System.out.println("Enter below the updated category description: ");
            String newDecription = scanner.nextLine();
            if(!newDecription.isEmpty()){
                category.setDescription(newDecription);
            }
            System.out.println("Category information was successfully updated.");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(Long id) {
        for(Category category : OnlineMarketDemo.categories){
            if(category.getId().equals(id)){
                OnlineMarketDemo.categories.remove(category);
                return true;
            }
        }
        return false;
    }

    @Override
    public Category findById(Long id) {
        for (Category category : OnlineMarketDemo.categories) {
            if(category.getId().equals(id)){
                return category;
            }
        }
        return null;
    }
}
