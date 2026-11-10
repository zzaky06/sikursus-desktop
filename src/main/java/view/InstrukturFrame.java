
package view;

import dao.InstrukturDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.Instruktur;

public class InstrukturFrame extends JFrame {

    private final JTextField nama = new JTextField();
    private final JTextField keahlian = new JTextField();
    private final JTextField hp = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField search = new JTextField(18);

    private final JTable table = new JTable(
        ViewUtil.tableModel("ID", "Nama", "Keahlian", "No. HP", "Email")
    );

    private final InstrukturDAO dao = new InstrukturDAO();

    private int selected = -1;

    private final Color BLUE = new Color(37, 99, 235);
    private final Color NAVY = new Color(15, 35, 70);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color WHITE = Color.WHITE;

    public InstrukturFrame() {
        super("Master Instruktur | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        build();
        load("");
    }

    private void build() {

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(25, 28, 25, 28));

        // HEADER

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG);

        JLabel title = new JLabel("Data Instruktur");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola informasi dan data pengajar kursus."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        // CONTENT

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG);

        // FORM

        JPanel formCard = createCard();

        JPanel formTitle = new JPanel(new BorderLayout());
        formTitle.setBackground(WHITE);

        JLabel formHeading = new JLabel("Informasi Instruktur");
        formHeading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formHeading.setForeground(TEXT);

        JLabel formDesc = new JLabel(
            "Masukkan informasi instruktur pada formulir berikut."
        );
        formDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formDesc.setForeground(MUTED);

        JPanel formHeadingPanel = new JPanel();
        formHeadingPanel.setLayout(new BoxLayout(
            formHeadingPanel, BoxLayout.Y_AXIS
        ));
        formHeadingPanel.setBackground(WHITE);

        formHeadingPanel.add(formHeading);
        formHeadingPanel.add(Box.createVerticalStrut(5));
        formHeadingPanel.add(formDesc);

        formTitle.add(formHeadingPanel, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 2, 18, 15));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Nama Instruktur", nama));
        fields.add(createField("Keahlian", keahlian));
        fields.add(createField("Nomor HP", hp));
        fields.add(createField("Email", email));

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

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(fields, BorderLayout.CENTER);
        formCard.add(actions, BorderLayout.SOUTH);

        // TABLE

        JPanel tableCard = createCard();

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(WHITE);

        JPanel tableTitlePanel = new JPanel();
        tableTitlePanel.setLayout(new BoxLayout(
            tableTitlePanel, BoxLayout.Y_AXIS
        ));
        tableTitlePanel.setBackground(WHITE);

        JLabel tableTitle = new JLabel("Daftar Instruktur");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableTitle.setForeground(TEXT);

        JLabel tableSubtitle = new JLabel(
            "Informasi seluruh instruktur yang terdaftar."
        );
        tableSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tableSubtitle.setForeground(MUTED);

        tableTitlePanel.add(tableTitle);
        tableTitlePanel.add(Box.createVerticalStrut(5));
        tableTitlePanel.add(tableSubtitle);

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

        tableHeader.add(tableTitlePanel, BorderLayout.WEST);
        tableHeader.add(searchPanel, BorderLayout.EAST);

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(formCard, BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        setContentPane(root);

        // ACTIONS

        simpan.addActionListener(e -> save());
        ubah.addActionListener(e -> update());
        hapus.addActionListener(e -> delete());
        reset.addActionListener(e -> clear());

        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                pick();
            }
        });

        getRootPane().setDefaultButton(simpan);
    }

    // CARD

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

    // FORM FIELD

    private JPanel createField(String label, JTextField field) {

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

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(5, 10, 5, 10)
        ));

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);

        return panel;
    }

    // BUTTON

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

    // TABLE STYLE

    private void styleTable() {

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(38);
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

    // DATA

    private Instruktur data() {

        Instruktur x = new Instruktur();

        x.setId(Math.max(selected, 0));
        x.setNama(nama.getText().trim());
        x.setKeahlian(keahlian.getText().trim());
        x.setNoHp(hp.getText().trim());
        x.setEmail(email.getText().trim());

        return x;
    }

    private void save() {

        try {
            dao.insert(data());
            JOptionPane.showMessageDialog(
                this, "Data instruktur berhasil disimpan."
            );
            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Menyimpan instruktur");
        }
    }

    private void update() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih instruktur terlebih dahulu."
            );
            return;
        }

        try {
            dao.update(data());

            JOptionPane.showMessageDialog(
                this, "Data instruktur berhasil diubah."
            );

            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Mengubah instruktur");
        }
    }

    private void delete() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih instruktur terlebih dahulu."
            );
            return;
        }

        if (JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus instruktur ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this, "Data instruktur berhasil dihapus."
            );

            load("");
            clear();

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Menghapus instruktur", ex
            );
        }
    }

    private void load(String q) {

        try {
            List<Instruktur> list = dao.search(q);

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Instruktur x : list) {
                m.addRow(new Object[]{
                    x.getId(),
                    x.getNama(),
                    x.getKeahlian(),
                    x.getNoHp(),
                    x.getEmail()
                });
            }

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Memuat instruktur", ex
            );
        }
    }

    private void pick() {

        int r = table.getSelectedRow();

        if (r < 0) return;

        selected = Integer.parseInt(
            table.getValueAt(r, 0).toString()
        );

        nama.setText(String.valueOf(table.getValueAt(r, 1)));
        keahlian.setText(String.valueOf(table.getValueAt(r, 2)));
        hp.setText(String.valueOf(table.getValueAt(r, 3)));
        email.setText(String.valueOf(table.getValueAt(r, 4)));
    }

    private void clear() {

        selected = -1;

        nama.setText("");
        keahlian.setText("");
        hp.setText("");
        email.setText("");

        table.clearSelection();
        nama.requestFocus();
    }

    private void handle(Exception ex, String action) {

        if (ex instanceof SQLException) {
            ViewUtil.sqlError(
                this, action, (SQLException) ex
            );
        } else {
            JOptionPane.showMessageDialog(
                this, ex.getMessage()
            );
        }
    }
}
