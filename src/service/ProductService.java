package service;

import model.Product;

import java.util.Map;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 8:18 PM
 */
public interface ProductService {

    boolean addProduct(Product product, Double quantity);

    boolean editProduct(Product product);

    boolean editProduct(Long id);

    boolean deleteProduct(Product product);

    boolean deleteProduct(Long id);

    Map.Entry<Product, Double> findById(Long id);

    Boolean updateQuantity(Product product, Double quantity);
}
