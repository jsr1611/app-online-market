package service;

import model.Product;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 8:18 PM
 */
public interface ProductService {

    boolean addProduct(Product product);

    boolean editProduct(Product product);

    boolean editProduct(Long id);

    boolean deleteProduct(Product product);

    boolean deleteProduct(Long id);

    Product findById(Long id);

    Boolean updateQuantity(Product product, Double quantity);
}
