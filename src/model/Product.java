package model;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 6:38 PM
 */
public class Product {

    // name, category, price, amount	(Optional: Warehourse)
    private Long id;
    private String name;
    private Category category;
    private Category subCategory;
    private Double price;
    private User seller;

    public Product() {
    }

    public Product(Long id, String name, Category category, Category subCategory, Double price, User seller) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return name +
                ", category: " + (category != null ? category.getName() : "no category") +
                ", subCategory: " + (subCategory != null ? subCategory.getName() : "no subcategory") +
                ", price: " + price +
                ", seller: " + seller.getFullName() + "(" + seller.getEmail() + ")";
    }
}
