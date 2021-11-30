package service.implement;

import enums.Role;
import model.Account;
import model.User;
import realization.OnlineMarketDemo;
import service.RegistrationService;

import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 11/20/2021
 * Time: 7:16 PM
 */
public class RegistrationServiceIml implements RegistrationService {

    public static Scanner scanner;
    private Long accountNum = 1002110000L;

    @Override
    public boolean signUp() {
        scanner = new Scanner(System.in);
        System.out.print("Fullname: ");
        String fullname = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();
        while (!validateEmail(email)){
            System.out.println("Incorrect email format!");
            System.out.print("Email: ");
            email = scanner.nextLine();
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();


        int indexOfRole = 1;
        for (Role value : Role.values()) {
            System.out.println(indexOfRole + " " + value);
            indexOfRole++;
        }
        System.out.print("Choose the role: ");
        int roleIndex = scanner.nextInt();
        while (roleIndex >= indexOfRole || roleIndex < 1){
            System.out.println("Incorrect input!");
            System.out.print("Choose the role: ");
            roleIndex = scanner.nextInt();
        }
        Role role = Role.values()[roleIndex - 1];

        int accountPassword = -1;
        while (accountPassword == -1){
            System.out.print("Please, enter your account password (4 digits): ");
            try {
                accountPassword = scanner.nextInt();
            }
            catch (Exception e){
                System.out.println("Incorrect password input!");
            }

        }

        Account account = new Account(accountNum++, accountPassword);

        Set<User> users = OnlineMarketDemo.users;
        int lastId = users.size() + 1;
        User newUser =
                new User(
                        Long.valueOf(lastId),
                        fullname,
                        email,
                        password,
                        role,
                        account
                        );

        OnlineMarketDemo.users.add(newUser);
        return true;
    }

    @Override
    public boolean signIn() {
        scanner = new Scanner(System.in);
        System.out.print("Email: ");
        String email = scanner.next();
        while (!validateEmail(email)){
            System.out.println("Wrong email format!");
            System.out.print("Email: ");
            email = scanner.next();
        }
        System.out.print("Password: ");
        String password = scanner.next();

        for (User user : OnlineMarketDemo.users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                OnlineMarketDemo.currentUser = user;
                return true;
            }
        }
        return false;
    }

    public static boolean validateEmail(String emailAddress) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
