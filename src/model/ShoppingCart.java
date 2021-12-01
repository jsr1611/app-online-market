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
    private Long customerId;
    private Map<Product, Double> products;
    private Double totalAmount;

    public ShoppingCart(Long id, Long customerId) {
        this.id = id;
        this.customerId = customerId;
        this.products = new HashMap<>();
        totalAmount = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Map<Product, Double> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Double> products) {
        this.products = products;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addProduct(Product product, Double qtyToAdd){
        if(products.containsKey(product)) {
            products.forEach((prod, qty) -> {
                if (prod.equals(product)) {
                    qty += qtyToAdd;
                    products.replace(prod, qty);
                    totalAmount += prod.getPrice() * qtyToAdd;

                }
            });
        }
        else {
            products.put(product, qtyToAdd);
            totalAmount+= product.getPrice() * qtyToAdd;
        }
        System.out.println("Product was successfully added to the cart.");
    }

    @Override
    public String toString() {
        StringBuilder prods = new StringBuilder();
        Product product;
        Double quantity;
        int counter = 1;
        for (Map.Entry<Product, Double> productsEntry : products.entrySet()) {
            product = productsEntry.getKey();
            quantity = productsEntry.getValue();
            prods.append(counter++)
                    .append(String.format(". %1$-15s",product.getName()))
                    .append(String.format("\t quantity: %1$-5s",quantity))
                    .append("\t price: ")
                    .append(product.getPrice())
                    .append("\n");
        }

        return "Shopping Cart " +
                "\nProducts:\n" +  prods +
                "Total amount: " + totalAmount;
    }
}
