package service.implement;

import model.Category;
import realization.OnlineMarketDemo;
import service.CategoryService;

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
        return false;
    }

    @Override
    public boolean deleteCategory(Long id) {
        return false;
    }
}
