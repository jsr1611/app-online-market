package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:08 PM
 */
public class OrderDetails {

    private Long id;
    private Order order;
    private Map<Product, Double> products;

    public OrderDetails(Long id, Order order, Map<Product, Double> products){
        this.id = id;
        this.order = order;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Map<Product, Double> getProducts(){
        return products;
    }

    public void setProducts(Map<Product, Double> products) {
        this.products = products;
    }


    @Override
    public String toString() {
        StringBuilder productStr = new StringBuilder();
        int counter = 0;
        Product product = null;
        Double quantity = null;
        Double totalPrice = null;
        for(Map.Entry<Product, Double> prod: products.entrySet()){
            product = prod.getKey();
            quantity = prod.getValue();
            totalPrice = product.getPrice() * quantity;
            productStr.append(counter++)
                    .append(". Order ")
                    .append(order.getId())
                    .append("\t")
                    .append(product.getName())
                    .append("\t")
                    .append(quantity)
                    .append(" pcs\t")
                    .append(totalPrice)
                    .append("\n");
        }
        // 1. Order 1   iPhone 13   1 pcs      1300.0
        // 2. Order 1   Samsung
        // TODO: 11/30/2021 Improve the printing format
        return productStr.toString();
    }

    /*

    Order
    Order 1   Narxi: 190 000;

    OrderDetails:
    1.   Order 1   Samsung S3     5   25 000
    2.   Order 1   Redmi Note 3   2   75 000
    3.   Order 1   Shim           2   10 000
    4.   Order 1   Dazmol         1   80 000
     */


}
