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
    private Map<Product, Integer> products;

    public OrderDetails(Long id, Order order, Map<Product, Integer> products){
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

    public Map<Product, Integer> getProducts(){
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }


    @Override
    public String toString() {
        StringBuilder prods = new StringBuilder();
        int counter = 0;
        Set<Map.Entry<Product, Integer>> entries = products.entrySet();
        for(Map.Entry<Product, Integer> prod: products.entrySet()){
                prods.append(counter++)
                        .append(". ")
                        .append("\t")
                        .append("Order " +  order.getId())
                        .append("\t")
                        .append(prod.getKey().getName())
                        .append("\t")
                        .append(prod.getValue())
                        .append("\t")
                        .append(prod.getKey().getPrice() * prod.getValue())
                        .append("\n");
        }
        return "Order details\n" +
                "Id: " + id +
                "\n" + order +
                "\n" + prods;
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
