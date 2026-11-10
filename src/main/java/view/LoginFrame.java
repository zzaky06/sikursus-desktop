
package view;

import dao.UserDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;
import util.UserSession;

public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(22);
    private final JPasswordField pwdPassword = new JPasswordField(22);
    private final UserDAO dao = new UserDAO();

    private final Color NAVY = new Color(15, 35, 70);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);

    public LoginFrame() {
        super("SIKURSUS | Login");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        build();
    }

    private void build() {

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG);

        // LEFT PANEL
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(NAVY);
        left.setBorder(new EmptyBorder(65, 45, 45, 40));

        JLabel brand = new JLabel("SIKURSUS");
        brand.setFont(new Font("SansSerif", Font.BOLD, 34));
        brand.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("SYSTEM INFORMASI KURSUS");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        subtitle.setForeground(new Color(147, 197, 253));

        JLabel welcome = new JLabel(
            "<html>Kelola Kursus<br>Dengan Lebih Mudah.</html>"
        );
        welcome.setFont(new Font("SansSerif", Font.BOLD, 29));
        welcome.setForeground(Color.WHITE);

        JLabel description = new JLabel(
            "<html>Platform pengelolaan kursus yang<br>"
            + "praktis, terstruktur, dan efisien.</html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(new Color(203, 213, 225));

        left.add(brand);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(100));
        left.add(welcome);
        left.add(Box.createVerticalStrut(18));
        left.add(description);
        left.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("SIKURSUS Desktop Application");
        footer.setForeground(new Color(148, 163, 184));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        left.add(footer);

        // RIGHT PANEL
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(38, 38, 38, 38));

        JLabel title = new JLabel("Selamat Datang!");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel("Silakan login ke akun Anda.");
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.setForeground(MUTED);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(info);
        card.add(Box.createVerticalStrut(35));

        addLabel(card, "Username");
        styleField(txtUsername);
        card.add(txtUsername);

        card.add(Box.createVerticalStrut(20));

        addLabel(card, "Password");
        styleField(pwdPassword);
        card.add(pwdPassword);

        card.add(Box.createVerticalStrut(30));

        JButton login = createButton("Login Sekarang", BLUE, Color.WHITE);
        JButton keluar = createButton("Keluar", new Color(241, 245, 249), TEXT);

        login.setAlignmentX(Component.LEFT_ALIGNMENT);
        keluar.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(login);
        card.add(Box.createVerticalStrut(12));
        card.add(keluar);

        card.add(Box.createVerticalStrut(25));

        JLabel demo = new JLabel(
            "<html><center>Akun Demo<br>"
            + "admin_demo / Demo#2026<br>"
            + "operator_demo / Demo#2026</center></html>"
        );

        demo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        demo.setForeground(MUTED);
        demo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(demo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 35, 25, 35);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        right.add(card, gbc);

        root.add(left);
        root.add(right);

        setContentPane(root);
        getRootPane().setDefaultButton(login);

        login.addActionListener(e -> login());
        keluar.addActionListener(e -> System.exit(0));
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(9));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(new Color(248, 250, 252));
        field.setCaretColor(BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240), 1
            ),
            new EmptyBorder(12, 14, 12, 14)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    private void login() {
        String username = txtUsername.getText().trim();
        char[] password = pwdPassword.getPassword();

        try {
            if (username.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Username dan password wajib diisi."
                );
                return;
            }

            User user = dao.authenticate(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Login gagal. Periksa username/password atau status akun.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
                );

                pwdPassword.setText("");
                return;
            }

            UserSession.setCurrentUser(user);
            dispose();

            SwingUtilities.invokeLater(() ->
                new DashboardFrame().setVisible(true)
            );

        } catch (SQLException ex) {
            ViewUtil.sqlError(this, "Login", ex);
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
