package services;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import models.*;

public class AuthService extends BaseService {
    private static final String USER_SEPARATOR = ",";
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private ArrayList<User> users = new ArrayList<>();

    public AuthService() {
        super("users.txt");
    }

    // REGISTER logic flow
    public boolean register(String username, String password) {
        String cleanUsername = sanitizeUsername(username);
        String cleanPassword = sanitizePassword(password);

        if (cleanUsername == null || cleanPassword == null) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }

        if (isUsernameTaken(cleanUsername)) {
            System.out.println("Username already exists.");
            return false;
        }

        // Using Polymorphism to instantiate a Customer subclass
        users.add(new Customer(cleanUsername, cleanPassword));
        saveUser(cleanUsername, cleanPassword, "CUSTOMER");
        System.out.println("User registered!");
        return true;
    }

    // LOGIN logic flow
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String cleanUsername = username.trim();
        for (User user : users) {
            if (user.getUsername().equals(cleanUsername) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // LOAD USERS from file the file is at Root of the project
    // File name "user.txt" if need load other txt file just change the name in the
    // code
    @Override
    public void loadFromFile() {
        users.clear();

        try {
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split(USER_SEPARATOR);

                if (data.length < 2) {
                    continue;
                }

                String username = sanitizeUsername(data[0]);
                String password = sanitizePassword(data[1]);
                String role = normalizeRole(data.length >= 3 ? data[2] : "CUSTOMER");

                if (username != null && password != null && !isUsernameTaken(username)) {
                    if ("ADMIN".equals(role)) {
                        users.add(new Admin(username, password));
                    } else {
                        users.add(new Customer(username, password));
                    }
                }
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("No existing users file found.");
        }

        ensureDefaultAdmin();
    }

    private boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public void saveUser(String username, String password, String role) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(username + USER_SEPARATOR + password + USER_SEPARATOR + role + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Failed to save user to file.");
        }
    }

    private void ensureDefaultAdmin() {
        boolean hasAdmin = false;

        for (User user : users) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                hasAdmin = true;
                break;
            }
        }

        if (!hasAdmin) {
            users.add(new Admin(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD));
            saveUser(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, "ADMIN");
            System.out.println("Default admin created (username: admin, password: admin123).");
        }
    }

    @Override
    public void saveToFile() {
        // Implementation typically overrides line by line if full refresh is needed.
        // AuthService currently appends via saveUser(), but having this fulfills the polymorphic BaseService contract.
    }

    private String sanitizeUsername(String username) {
        if (username == null) {
            return null;
        }

        String clean = username.trim();
        if (clean.isEmpty() || clean.contains(USER_SEPARATOR)) {
            return null;
        }

        return clean;
    }

    private String sanitizePassword(String password) {
        if (password == null) {
            return null;
        }

        String clean = password.trim();
        if (clean.isEmpty() || clean.contains(USER_SEPARATOR)) {
            return null;
        }

        return clean;
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "CUSTOMER";
        }

        String clean = role.trim().toUpperCase();
        if ("ADMIN".equals(clean)) {
            return "ADMIN";
        }
        return "CUSTOMER";
    }
}