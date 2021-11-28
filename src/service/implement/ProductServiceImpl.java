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
        OnlineMarketDemo.products.add(product);
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
        if(OnlineMarketDemo.products.contains(product)){
            OnlineMarketDemo.products.remove(product);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        for (Product product : OnlineMarketDemo.products) {
            if(product.getId().equals(id)){
                OnlineMarketDemo.products.remove(product);
                return true;
            }
        }
        return false;
    }


    public Product findById(Long id){
        for (Product product : OnlineMarketDemo.products) {
            if(product.getId().equals(id)){
                return product;
            }
        }
        return null;
    }

    @Override
    public Boolean updateQuantity(Product product, Double quantity) {
        for (Product prod : OnlineMarketDemo.products) {
            if(prod.equals(product)){
                Double currentQty = prod.getQuantity();
                currentQty += quantity;
                product.setQuantity(currentQty);
                return true;
            }
        }

        return false;
    }
}
