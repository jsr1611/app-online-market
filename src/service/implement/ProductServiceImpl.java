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
    public boolean addProduct(Product product, Double quantity) {
        OnlineMarketDemo.products.put(product, quantity);
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
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if(prodWithQty.getKey().getId().equals(id)){
                OnlineMarketDemo.products.remove(prodWithQty.getKey());
                return true;
            }
        }
        return false;
    }


    public Map.Entry<Product, Double> findById(Long id){
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if(prodWithQty.getKey().getId().equals(id)){
                return prodWithQty;
            }
        }
        return null;
    }

    @Override
    public Boolean updateQuantity(Product product, Double quantity) {
        for (Map.Entry<Product, Double> prodWithQty : OnlineMarketDemo.products.entrySet()) {
            if(prodWithQty.getKey().equals(product)){
                Double currentQty = prodWithQty.getValue();
                currentQty += quantity;
                prodWithQty.setValue(currentQty);
                return true;
            }
        }

        return false;
    }
}
