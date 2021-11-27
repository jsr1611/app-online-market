package service.implement;

import model.Product;
import realization.OnlineMarketDemo;
import service.ProductService;

import java.util.Map;
import java.util.Set;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 8:19 PM
 */
public class ProductServiceImpl implements ProductService {

    @Override
    public boolean addProduct(Product product) {
        if(OnlineMarketDemo.products.containsKey(product)){
            return false;
        }
        Integer key = OnlineMarketDemo.products.size()+1;
        OnlineMarketDemo.products.put(product, key);
        return true;
    }

    @Override
    public boolean editProduct(Product product) {
        System.out.println("----------EDIT PRODUCT----------");
        return false;
    }

    @Override
    public boolean editProduct(Long id) {
        System.out.println("----------EDIT PRODUCT----------");
        return false;
    }



    @Override
    public boolean deleteProduct(Product product) {
        if(OnlineMarketDemo.products.containsKey(product)){
            OnlineMarketDemo.products.remove(product);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Set<Map.Entry<Product, Integer>> entries = OnlineMarketDemo.products.entrySet();
        for (Map.Entry<Product, Integer> entry : entries) {
            if(entry.getKey().getId().equals(id)){
                OnlineMarketDemo.products.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }


    public Product findById(Long id){
        Set<Map.Entry<Product, Integer>> entries = OnlineMarketDemo.products.entrySet();
        for (Map.Entry<Product, Integer> entry : entries) {
            if(entry.getKey().getId().equals(id)){
                return entry.getKey();
            }
        }
        return null;
    }
}
