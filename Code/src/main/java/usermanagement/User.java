package usermanagement;

public class User {
    private static int temp; // Static variable to generate unique user IDs
    private int id; // The unique ID for the user
    private String name; // The user's name
    private String email; // The user's email address
    private String phone; // The user's phone number

    // Constructor to initialize a User object with name, email, and phone
    public User(String name, String email, String phone) {
        this.id = ++temp; // Increment the static temp variable to generate a unique user ID
        this.name = name; // Set the user's name
        this.email = email; // Set the user's email address
        this.phone = phone; // Set the user's phone number
    }

    // Getter for the user ID
    public int getId() {
        return id;
    }

    // Setter for the user ID (although not typically needed, could be used for updates)
    public void setId(int id) {
        this.id = id;
    }

    // Getter for the user's email
    public String getEmail() {
        return email;
    }

    // Setter for the user's email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for the user's name
    public String getName() {
        return name;
    }

    // Setter for the user's name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the user's phone number
    public String getPhone() {
        return phone;
    }

    // Setter for the user's phone number
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
