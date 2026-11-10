
package view;

import dao.KursusDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.Kursus;

public class KursusFrame extends JFrame {

    private final JTextField kode = new JTextField();
    private final JTextField nama = new JTextField();
    private final JTextField biaya = new JTextField();
    private final JTextField search = new JTextField(18);

    private final JComboBox<String> level = new JComboBox<>(
        new String[]{"BASIC", "INTERMEDIATE", "ADVANCED"}
    );

    private final JTable table = new JTable(
        ViewUtil.tableModel(
            "ID", "Kode", "Nama Kursus", "Level", "Biaya"
        )
    );

    private final KursusDAO dao = new KursusDAO();

    private int selected = -1;

    private final Color BLUE = new Color(37, 99, 235);
    private final Color NAVY = new Color(15, 35, 70);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color WHITE = Color.WHITE;

    public KursusFrame() {

        super("Master Kursus | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        build();
        load("");
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

        JLabel title = new JLabel("Data Kursus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola informasi, level, dan biaya program kursus."
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

        JLabel title = new JLabel("Informasi Kursus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Masukkan detail program kursus yang tersedia."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // INPUT FIELDS

        JPanel fields = new JPanel(new GridLayout(2, 2, 18, 16));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Kode Kursus", kode));
        fields.add(createField("Nama Kursus", nama));
        fields.add(createField("Level Kursus", level));
        fields.add(createField("Biaya Kursus (Rp)", biaya));

        // BUTTONS

        JButton simpan = createButton(
            "Simpan", BLUE, WHITE
        );

        JButton ubah = createButton(
            "Ubah", new Color(5, 150, 105), WHITE
        );

        JButton hapus = createButton(
            "Hapus", new Color(220, 38, 38), WHITE
        );

        JButton reset = createButton(
            "Reset", new Color(226, 232, 240), TEXT
        );

        JPanel actions = new JPanel(new FlowLayout(
            FlowLayout.RIGHT, 10, 0
        ));
        actions.setBackground(WHITE);

        actions.add(reset);
        actions.add(hapus);
        actions.add(ubah);
        actions.add(simpan);

        card.add(heading, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        // EVENTS

        simpan.addActionListener(e -> save());
        ubah.addActionListener(e -> update());
        hapus.addActionListener(e -> delete());
        reset.addActionListener(e -> clear());

        getRootPane().setDefaultButton(simpan);

        return card;
    }

    // ================= TABLE =================

    private JPanel buildTable() {

        JPanel card = createCard();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(
            titlePanel, BoxLayout.Y_AXIS
        ));
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("Daftar Kursus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Daftar seluruh program kursus yang terdaftar."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        // SEARCH

        JButton cari = createButton(
            "Cari", BLUE, WHITE
        );

        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setPreferredSize(new Dimension(220, 38));

        search.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(5, 10, 5, 10)
        ));

        JPanel searchPanel = new JPanel(new FlowLayout(
            FlowLayout.RIGHT, 8, 0
        ));
        searchPanel.setBackground(WHITE);

        searchPanel.add(search);
        searchPanel.add(cari);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        // TABLE STYLE

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

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
        button.setPreferredSize(new Dimension(100, 38));

        return button;
    }

    // ================= TABLE STYLE =================

    private void styleTable() {

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
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
                setBorder(new EmptyBorder(0, 10, 0, 10));

                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i)
                .setCellRenderer(renderer);
        }
    }

    // ================= DATA =================

    private Kursus data() {

        double n;

        try {
            n = Double.parseDouble(biaya.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Biaya harus berupa angka."
            );
        }

        if (n < 0) {
            throw new IllegalArgumentException(
                "Biaya tidak boleh negatif."
            );
        }

        return new Kursus(
            Math.max(selected, 0),
            kode.getText().trim(),
            nama.getText().trim(),
            level.getSelectedItem().toString(),
            n
        );
    }

    // ================= CRUD =================

    private void save() {

        try {

            dao.insert(data());

            JOptionPane.showMessageDialog(
                this, "Data kursus berhasil disimpan."
            );

            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Menyimpan kursus");
        }
    }

    private void update() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih kursus terlebih dahulu."
            );
            return;
        }

        try {

            dao.update(data());

            JOptionPane.showMessageDialog(
                this, "Data kursus berhasil diubah."
            );

            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Mengubah kursus");
        }
    }

    private void delete() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih kursus terlebih dahulu."
            );
            return;
        }

        if (JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus kursus ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this, "Data kursus berhasil dihapus."
            );

            load("");
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Menghapus kursus", ex
            );
        }
    }

    // ================= LOAD TABLE =================

    private void load(String q) {

        try {

            List<Kursus> list = dao.search(q);

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Kursus x : list) {

                m.addRow(new Object[]{
                    x.getIdKursus(),
                    x.getKode(),
                    x.getNama(),
                    x.getLevel(),
                    String.format("%,.0f", x.getBiaya())
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat kursus", ex
            );
        }
    }

    // ================= SELECT ROW =================

    private void pick() {

        int r = table.getSelectedRow();

        if (r < 0) return;

        selected = Integer.parseInt(
            table.getValueAt(r, 0).toString()
        );

        kode.setText(
            table.getValueAt(r, 1).toString()
        );

        nama.setText(
            table.getValueAt(r, 2).toString()
        );

        level.setSelectedItem(
            table.getValueAt(r, 3)
        );

        biaya.setText(
            table.getValueAt(r, 4).toString().replace(",", "")
        );
    }

    // ================= RESET =================

    private void clear() {

        selected = -1;

        kode.setText("");
        nama.setText("");
        biaya.setText("");

        level.setSelectedIndex(0);

        table.clearSelection();
        kode.requestFocus();
    }

    // ================= ERROR =================

    private void handle(Exception ex, String action) {

        if (ex instanceof SQLException) {

            ViewUtil.sqlError(
                this, action, (SQLException) ex
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
