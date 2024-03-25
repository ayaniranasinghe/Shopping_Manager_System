public class User {
    // Private fields to store user information
    private String username;
    private String password;

    // Constructor to initialize the User object with a username and password
    public User(String username, String password) {
        // Set username and password using the provided setters
        setUsername(username);
        setPassword(password);
    }

    // Getter for retrieving the username
    public String getUsername() {
        return username;
    }

    // Setter for updating the username with validation
    public void setUsername(String username) {
        // Validate and set the username
        if (isValidUsername(username)) {
            this.username = username;
        } else {
            // Throw an exception for invalid username
            throw new IllegalArgumentException("Invalid username");
        }
    }

    // Getter for retrieving the password
    public String getPassword() {
        return password;
    }

    // Setter for updating the password with validation
    public void setPassword(String password) {
        // Validate and set the password
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            // Throw an exception for invalid password
            throw new IllegalArgumentException("Invalid password");
        }
    }



    // Validate whether the username is not null and not empty
    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }

    // Validate whether the password is not null and not empty
    private boolean isValidPassword(String password) {
        return password != null && !password.isEmpty();
    }
}
