package service.implement;

import model.Product;
import realization.OnlineMarketDemo;
import service.ProductService;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 8:19 PM
 */
public class ProductServiceImpl implements ProductService {

    @Override
    public boolean addProduct(Product product) {
        if(OnlineMarketDemo.products.containsValue(product)){
            return false;
        }
        Integer key = OnlineMarketDemo.products.size()+1;
        OnlineMarketDemo.products.put(key, product);
        return true;
    }

    @Override
    public boolean editProduct(Product product) {
        System.out.println("Not implemented yet");
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        if(OnlineMarketDemo.products.containsKey(id)){
            OnlineMarketDemo.products.remove(id);
            return true;
        }
        return false;
    }
}
