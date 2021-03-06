import java.io.IOException;
import java.util.Scanner;

public class Passenger {
    private String name;
    private String email;
    private String gender;
    private int phoneNumber;
    private int age;
    private double discount = 0.0;
    private Scanner input = new Scanner(System.in);
    public  DataStore writer = new DataStore();

    Passenger() {
        init();
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setName() {
        System.out.println("\nWhat's your name? ");
        this.name = input.nextLine();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmail() {
        System.out.println("\nWhat's your email address ? ");
        this.email = input.nextLine();
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumber() {
        System.out.println("\nWhat is your Phone Number? ");
        this.phoneNumber = Integer.parseInt(input.nextLine());
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.setDiscount(gender);
    }

    public void setGender() {
        System.out.println("\nWhat is your Gender ? ");
        String in = input.nextLine().toUpperCase();
        switch (in) {
            case "M":
            case "MALE":
            case "F":
            case "FEMALE":
                this.setGender(in);
                break;
            default:
                this.gender = "other";
                break;
        }
    }

    public void setAge(int age) {
        this.age = age;
        this.setDiscount(age);
    }

    public void setAge() {
        // TODO UPDATE: create a date method to take in D.O.B. vs age explicitly.
        System.out.println("\nHow old are you ? ");
        this.setAge(Integer.parseInt(input.nextLine()));
    }

    public void setDiscount(int age) {
        if (age <= 12) {
            this.discount += .5;
        } else if (age >= 60) {
            this.discount += .6;
        }
    }

    public void setDiscount(String gender) {
        if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("f")) {
            this.discount += .25;
        }
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public double getDiscount() {
        return this.discount;
    }

    // Methods

    public void storeData() {
        String fileName = "output/Passenger.txt";
        boolean append = true;
        try {
            this.writer.writeToAFile(fileName,"NAME",String.valueOf(this.getName()).toUpperCase(),!append);
            this.writer.writeToAFile(fileName,"AGE",String.valueOf(this.getAge()),append);
            this.writer.writeToAFile(fileName,"EMAIL",String.valueOf(this.getEmail()).toUpperCase(),append);
            this.writer.writeToAFile(fileName,"GENDER",String.valueOf(this.getGender()),append);
            this.writer.writeToAFile(fileName,"PHONE",String.valueOf(this.getPhoneNumber()),append);
            this.writer.writeToAFile(fileName,"DISCOUNT",String.valueOf(this.getDiscount()),append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.setName();
        this.setAge();
        this.setEmail();
        this.setGender();
        this.setPhoneNumber();
        this.storeData();
    }
}
