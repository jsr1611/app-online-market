package service;

import enums.Role;
import model.Order;
import model.ShoppingCart;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:59 PM
 */
public interface DemonstrationService {

    void showCustomerMenu();

    void showSalesmanMenu();

    void showManagerMenu();

    void showDirectorMenu();

    void showOrders(Order order, Role role);

    void showShoppingCart(ShoppingCart cart, Role userRole);

}
