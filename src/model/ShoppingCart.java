package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:15 PM
 */
public class ShoppingCart {

    private Long id;
    private Map<Product, Integer> products;
    private Double totalAmount;

    public ShoppingCart(Long id) {
        this.id = id;
        this.products = new HashMap<>();
        totalAmount = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void addProduct(Product product, Integer qtyToAdd){
        if(products.containsKey(product)) {
            products.forEach((prod, qty) -> {
                if (prod.equals(product)) {
                    qty += qtyToAdd;
                }
            });
        }
        else {
            products.put(product, qtyToAdd);
        }
        System.out.println("Product was successfully added to the cart.");
    }


    @Override
    public String toString() {
        StringBuilder prods = new StringBuilder();
        for (Map.Entry<Product, Integer> productIntegerEntry : products.entrySet()) {
            prods.append(productIntegerEntry.getKey().getName()).append(", quantity: ").append(productIntegerEntry.getValue().toString()).append("\n");
        }

        return "Id: " + id +
                ", \nproducts: " +  prods +
                ", \ntotalAmount: " + totalAmount;
    }
}
