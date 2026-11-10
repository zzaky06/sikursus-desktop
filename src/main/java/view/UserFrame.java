
package view;

import dao.UserDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.User;
import util.UserSession;

public class UserFrame extends JFrame {

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();

    private final JComboBox<String> cmbRole =
        new JComboBox<>(new String[]{"ADMIN", "OPERATOR"});

    private final JCheckBox chkAktif =
        new JCheckBox("Akun aktif", true);

    private final JTable table = new JTable(
        ViewUtil.tableModel("ID", "Username", "Role", "Status")
    );

    private final UserDAO dao = new UserDAO();

    private int selected = -1;

    private final Color NAVY = new Color(15, 35, 70);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color BG = new Color(241, 245, 249);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);

    public UserFrame() {

        super("Manajemen User | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        build();
        load();
    }

    // ================= MAIN UI =================

    private void build() {

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(25, 28, 25, 28));

        // HEADER

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);

        JLabel title = new JLabel("Manajemen User & Role");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola akun pengguna, hak akses, dan status akun sistem."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        root.add(header, BorderLayout.NORTH);

        // CONTENT

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG);

        content.add(buildForm(), BorderLayout.NORTH);
        content.add(buildTable(), BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    // ================= FORM =================

    private JPanel buildForm() {

        JPanel card = createCard();

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(WHITE);

        JLabel title = new JLabel("Informasi Akun");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Tambahkan akun baru atau kelola hak akses pengguna."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // FORM FIELDS

        JPanel fields = new JPanel(new GridLayout(2, 2, 18, 15));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Username", txtUsername));

        fields.add(createField(
            "Password / Password Baru", txtPassword
        ));

        fields.add(createField("Role Pengguna", cmbRole));

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(
            statusPanel, BoxLayout.Y_AXIS
        ));
        statusPanel.setBackground(WHITE);

        JLabel statusLabel = new JLabel("Status Akun");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(TEXT);

        chkAktif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkAktif.setForeground(TEXT);
        chkAktif.setBackground(WHITE);
        chkAktif.setFocusPainted(false);
        chkAktif.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusPanel.add(statusLabel);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(chkAktif);

        fields.add(statusPanel);

        // BUTTONS

        JButton tambah = createButton(
            "Tambah User", BLUE, WHITE
        );

        JButton update = createButton(
            "Ubah Role", new Color(5, 150, 105), WHITE
        );

        JButton resetPass = createButton(
            "Reset Password", new Color(124, 58, 237), WHITE
        );

        JButton hapus = createButton(
            "Hapus", new Color(220, 38, 38), WHITE
        );

        JButton clear = createButton(
            "Reset Form", new Color(226, 232, 240), TEXT
        );

        JPanel actions = new JPanel(new FlowLayout(
            FlowLayout.RIGHT, 9, 0
        ));
        actions.setBackground(WHITE);

        actions.add(clear);
        actions.add(hapus);
        actions.add(resetPass);
        actions.add(update);
        actions.add(tambah);

        card.add(heading, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        // EVENTS

        tambah.addActionListener(e -> create());
        update.addActionListener(e -> update());
        resetPass.addActionListener(e -> resetPassword());
        hapus.addActionListener(e -> delete());
        clear.addActionListener(e -> clear());

        getRootPane().setDefaultButton(tambah);

        return card;
    }

    // ================= TABLE =================

    private JPanel buildTable() {

        JPanel card = createCard();

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(WHITE);

        JLabel title = new JLabel("Daftar Pengguna");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Pilih akun untuk mengubah role, status, atau password."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        card.add(heading, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                pick();
            }
        });

        return card;
    }

    // ================= COMPONENT HELPERS =================

    private JPanel createCard() {

        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(20, 22, 20, 22)
        ));

        return card;
    }

    private JPanel createField(String label, JComponent field) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);

        JLabel title = new JLabel(label);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(TEXT);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBackground(new Color(248, 250, 252));

        field.setPreferredSize(new Dimension(200, 39));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 39));

        if (field instanceof JTextField) {

            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(226, 232, 240)
                ),
                new EmptyBorder(5, 10, 5, 10)
            ));
        }

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);

        return panel;
    }

    private JButton createButton(
        String text, Color background, Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(125, 38));

        return button;
    }

    // ================= TABLE STYLE =================

    private void styleTable() {

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(42);
        table.setForeground(TEXT);
        table.setBackground(WHITE);

        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);

        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer renderer =
            new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                JTable t, Object value, boolean selected,
                boolean focus, int row, int column
            ) {

                super.getTableCellRendererComponent(
                    t, value, selected, focus, row, column
                );

                if (selected) {
                    setBackground(new Color(219, 234, 254));
                } else {
                    setBackground(row % 2 == 0
                        ? WHITE
                        : new Color(248, 250, 252));
                }

                setForeground(TEXT);
                setBorder(new EmptyBorder(0, 12, 0, 12));

                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i)
                .setCellRenderer(renderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
    }

    // ================= LOAD DATA =================

    private void load() {

        try {

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (User u : dao.findAll()) {

                m.addRow(new Object[]{
                    u.getIdUser(),
                    u.getUsername(),
                    u.getRole(),
                    u.isAktif() ? "Aktif" : "Nonaktif"
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat users", ex
            );
        }
    }

    // ================= SELECT USER =================

    private void pick() {

        int r = table.getSelectedRow();

        if (r < 0) return;

        selected = Integer.parseInt(
            table.getValueAt(r, 0).toString()
        );

        txtUsername.setText(
            table.getValueAt(r, 1).toString()
        );

        txtUsername.setEditable(false);

        // Password sengaja tidak diisi dari tabel.
        txtPassword.setText("");

        cmbRole.setSelectedItem(
            table.getValueAt(r, 2)
        );

        chkAktif.setSelected(
            "Aktif".equals(table.getValueAt(r, 3).toString())
        );
    }

    // ================= CREATE USER =================

    private void create() {

        char[] password = txtPassword.getPassword();

        try {

            if (txtUsername.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Username wajib diisi."
                );
            }

            if (password.length == 0) {
                throw new IllegalArgumentException(
                    "Password wajib diisi."
                );
            }

            dao.create(
                txtUsername.getText().trim(),
                password,
                cmbRole.getSelectedItem().toString(),
                chkAktif.isSelected()
            );

            JOptionPane.showMessageDialog(
                this,
                "User berhasil ditambahkan.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

            load();
            clear();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Menambah user", ex
            );

        } finally {

            Arrays.fill(password, '\0');
        }
    }

    // ================= UPDATE ROLE =================

    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih user terlebih dahulu."
            );

            return;
        }

        try {

            dao.updateRoleStatus(
                selected,
                cmbRole.getSelectedItem().toString(),
                chkAktif.isSelected()
            );

            JOptionPane.showMessageDialog(
                this,
                "Role dan status user berhasil diperbarui.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

            load();
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Mengubah user", ex
            );
        }
    }

    // ================= RESET PASSWORD =================

    private void resetPassword() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih user terlebih dahulu."
            );

            return;
        }

        char[] password = txtPassword.getPassword();

        try {

            if (password.length == 0) {
                throw new IllegalArgumentException(
                    "Masukkan password baru terlebih dahulu."
                );
            }

            dao.resetPassword(selected, password);

            JOptionPane.showMessageDialog(
                this,
                "Password berhasil direset.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

            txtPassword.setText("");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Reset Password",
                JOptionPane.WARNING_MESSAGE
            );

        } finally {

            Arrays.fill(password, '\0');
        }
    }

    // ================= DELETE USER =================

    private void delete() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih user terlebih dahulu."
            );

            return;
        }

        if (UserSession.getCurrentUser() != null
            && selected == UserSession.getCurrentUser().getIdUser()) {

            JOptionPane.showMessageDialog(
                this,
                "Akun yang sedang digunakan tidak boleh dihapus.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus user ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this,
                "User berhasil dihapus.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

            load();
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Menghapus user", ex
            );
        }
    }

    // ================= RESET FORM =================

    private void clear() {

        selected = -1;

        txtUsername.setText("");
        txtUsername.setEditable(true);

        txtPassword.setText("");

        cmbRole.setSelectedIndex(1);
        chkAktif.setSelected(true);

        table.clearSelection();

        txtUsername.requestFocus();
    }
}
