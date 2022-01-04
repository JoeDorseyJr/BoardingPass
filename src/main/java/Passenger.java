import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Passenger {
    private String name;
    private String email;
    private int phoneNumber;
    private String gender;
    private int age;
    private double discount = 0.0;
    private Scanner input = new Scanner(System.in);

    // TODO 1. Create a Constructor that take in user's info.
    Passenger() {
        init();
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setName() {
        System.out.println("What's your name? ");
        this.name = input.nextLine();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmail() {
        System.out.println("What's your email address ? ");
        this.email = input.nextLine();
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumber() {
        System.out.println("What is your Phone Number? ");
        this.phoneNumber = Integer.parseInt(input.nextLine());
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.setDiscount(gender);
    }

    public void setGender() {
        System.out.println("What is your Gender ? ");
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
        System.out.println("How old are you ? ");
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

    /* Methods to Create
     * TODO 3. Create a method that stores the user data to a file.
     * */
    BufferedWriter writer;

    public void storeData() {

        try {
            writer = new BufferedWriter(new FileWriter("Passenger.txt"));
            writer.append(String.valueOf(this.getName()));
            writer.newLine();
            writer.append(String.valueOf(this.getAge()));
            writer.newLine();
            writer.append(String.valueOf(this.getEmail()));
            writer.newLine();
            writer.append(String.valueOf(this.getGender()));
            writer.newLine();
            writer.append(String.valueOf(this.getPhoneNumber()));
            writer.newLine();

            writer.close();
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
