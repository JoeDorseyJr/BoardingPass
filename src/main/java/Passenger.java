public class Passenger {
    private String name;
    private String email;
    private int phoneNumber;
    private String gender;
    private int age;
    private double discount = 0.0;

    // TODO 1. Create a Constructor that take in user's info.

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.setDiscount(gender);
    }

    public void setAge(int age) {
        this.age = age;
        this.setDiscount(age);
    }

    public void setDiscount(int age) {
        if (age <= 12) {
            this.discount += .5;
        } else if (age >= 60) {
            this.discount += .6;
        }
    }

    public void setDiscount(String gender) {
        if (gender.equals("female")) {
            this.discount += .25;
        }
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getEmail(String email) {
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
    * TODO 2. Create a method that outputs gender (Switch) Statement.
    * TODO 3. Create a method that stores the user data to a file.
    * */
}
