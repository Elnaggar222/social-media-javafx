package test;
import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class MainTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        // 1️⃣ Register
        String hashedPassword = PasswordUtil.hashPassword("1234");
        System.out.println(hashedPassword);

        User newUser = new User("Mohamed", "hos@g.com", hashedPassword);

        boolean registered = userDAO.register(newUser);

        if (registered) {
            System.out.println("✅ User registered successfully!");
        }

        // 2️⃣ Login
        String loginHash = PasswordUtil.hashPassword("1234");

        User loggedInUser = userDAO.login("hos@g.com", loginHash);

        if (loggedInUser != null) {
            System.out.println("✅ Login successful!");
            System.out.println("Welcome " + loggedInUser.getName());
        } else {
            System.out.println("❌ Login failed!");
        }
    }
}