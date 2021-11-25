package test;

import java.util.HashSet;
import java.util.Set;

public class test {
    public static void main(String[] args) {
       Set<Car> carSet1 = new HashSet<>();
       carSet1.add(new Car("Hyundai", 160000));
        carSet1.add(new Car("Chevrolet", 130000));
        carSet1.add(new Car("Toyota", 150000));
        carSet1.add(new Car("Hyundai", 150000));
        carSet1.add(new Car("Hyundai", 150000));
        Object[] objects = carSet1.toArray();
        Car[] cars = new Car[objects.length];
        int index = 0;
        for (Object object : objects) {
            cars[index++] = (Car) object;
        }
        for(Car car : cars){
           System.out.println(car.getName() + " " + car.getPrice());
       }


    }
}
