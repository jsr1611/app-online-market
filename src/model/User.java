package model;

import enums.CardType;
import enums.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 6:33 PM
 */
public class User {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private Role role;              // enum
    private Boolean signedIn;
    private Account account;
    private List<PaymentMethod> paymentMethods;

    public User(Long id, String fullName, String email, String password, Role role, Account account) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.signedIn = false;
        this.account = account;
        this.paymentMethods = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<PaymentMethod> getPaymentMethods(){
        return paymentMethods;
    }
    public PaymentMethod getPaymentMethodByCardType(CardType cardType){
        for (PaymentMethod paymentMethod : paymentMethods) {
            if(paymentMethod.getCardType().equals(cardType)){
                return paymentMethod;
            }
        }
        return null;
    }

    public PaymentMethod getPaymentMethodByCardType(CardType cardType, CardType cardType2){
        for (PaymentMethod paymentMethod : paymentMethods) {
            if(paymentMethod.getCardType().equals(cardType) || paymentMethod.getCardType().equals(cardType2)){
                return paymentMethod;
            }
        }
        return null;
    }

    public void addPaymentMethod(PaymentMethod paymentMethod){
        this.paymentMethods.add(paymentMethod);
    }

    @Override
    public String toString() {
        return "Id" + id +
                ", full name: " + fullName +
                ", email: " + email +
                ", password: " + password  +
                ", role: " + role;
    }
}
