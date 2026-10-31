import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

class User {
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

class WelcomePage extends Frame {
    WelcomePage(User user) {
        super("Welcome");
        setLayout(new GridLayout(2, 1, 5, 5));
        Label welcome = new Label("Hello, " + user.getUsername(), Label.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(welcome);
        add(new Label("Role: " + user.getRole(), Label.CENTER));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(350, 180);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {
    private List<User> users = new ArrayList<>();
    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);
        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);
        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);
        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);
        add(content);

        login.addActionListener(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String enteredRole = role.getSelectedItem();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = findUser(enteredUsername);
            if (user == null || !user.getPassword().equals(enteredPassword)
                    || !user.getRole().equals(enteredRole)) {
                failedAttempts++;
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);

                if (failedAttempts >= MAX_ATTEMPTS) {
                    login.setEnabled(false);
                    JOptionPane.showMessageDialog(this,
                            "You entered incorrect information 3 times. Login is disabled.",
                            "Login Disabled", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }

            failedAttempts = 0;
            new WelcomePage(user).setVisible(true);
            dispose();
        });

        cancel.addActionListener(e -> dispose());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("student1", "1234", "Student"));
        users.add(new User("ta1", "1234", "TA"));
        users.add(new User("prof1", "1234", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
