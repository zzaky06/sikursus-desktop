
package view;

import dao.InstrukturDAO;
import dao.KelasDAO;
import dao.KursusDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.Kelas;
import util.LookupItem;
import util.Validation;

public class KelasFrame extends JFrame {

    private final JComboBox<LookupItem> kursus = new JComboBox<>();
    private final JComboBox<LookupItem> instruktur = new JComboBox<>();

    private final JTextField nama = new JTextField();
    private final JTextField tanggal = new JTextField(ViewUtil.today());
    private final JTextField kapasitas = new JTextField("20");
    private final JTextField search = new JTextField(18);

    private final JTable table = new JTable(
        ViewUtil.tableModel(
            "ID", "Kelas", "Kursus",
            "Instruktur", "Tanggal Mulai", "Kapasitas"
        )
    );

    private final KelasDAO dao = new KelasDAO();

    private int selected = -1;

    private final Color BLUE = new Color(37, 99, 235);
    private final Color NAVY = new Color(15, 35, 70);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color WHITE = Color.WHITE;

    public KelasFrame() {

        super("Master Kelas | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(950, 680));
        setLocationRelativeTo(null);

        build();
        loadLookups();
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

        JLabel title = new JLabel("Data Kelas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola kelas, kursus, instruktur, dan kapasitas peserta."
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

        JLabel title = new JLabel("Informasi Kelas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Lengkapi informasi kelas yang akan dikelola."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // FIELD GRID

        JPanel fields = new JPanel(new GridLayout(2, 3, 16, 16));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Pilih Kursus", kursus));
        fields.add(createField("Pilih Instruktur", instruktur));
        fields.add(createField("Nama Kelas", nama));
        fields.add(createField("Tanggal Mulai (yyyy-MM-dd)", tanggal));
        fields.add(createField("Kapasitas Peserta", kapasitas));

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

        JLabel title = new JLabel("Daftar Kelas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Data kelas yang telah terdaftar di sistem."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

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

    // ================= LOOKUP =================

    private void loadLookups() {

        try {

            kursus.removeAllItems();

            for (LookupItem x : new KursusDAO().lookup()) {
                kursus.addItem(x);
            }

            instruktur.removeAllItems();

            for (LookupItem x : new InstrukturDAO().lookup()) {
                instruktur.addItem(x);
            }

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Memuat pilihan kelas", ex
            );
        }
    }

    // ================= DATA =================

    private Kelas data() {

        LookupItem k = (LookupItem) kursus.getSelectedItem();
        LookupItem i = (LookupItem) instruktur.getSelectedItem();

        if (k == null || i == null) {
            throw new IllegalArgumentException(
                "Kursus dan instruktur wajib tersedia."
            );
        }

        Kelas x = new Kelas();

        x.setIdKelas(Math.max(selected, 0));
        x.setIdKursus(k.getId());
        x.setIdInstruktur(i.getId());

        x.setNamaKelas(nama.getText().trim());

        x.setTanggalMulai(
            Validation.date(
                tanggal.getText(), "Tanggal mulai"
            )
        );

        x.setKapasitas(
            Validation.positiveInt(
                kapasitas.getText(), "Kapasitas"
            )
        );

        return x;
    }

    // ================= CRUD =================

    private void save() {

        try {

            dao.insert(data());

            JOptionPane.showMessageDialog(
                this, "Data kelas berhasil disimpan."
            );

            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Menyimpan kelas");
        }
    }

    private void update() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih kelas terlebih dahulu."
            );
            return;
        }

        try {

            dao.update(data());

            JOptionPane.showMessageDialog(
                this, "Data kelas berhasil diubah."
            );

            load("");
            clear();

        } catch (Exception ex) {
            handle(ex, "Mengubah kelas");
        }
    }

    private void delete() {

        if (selected < 0) {
            JOptionPane.showMessageDialog(
                this, "Pilih kelas terlebih dahulu."
            );
            return;
        }

        if (JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus kelas ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this, "Data kelas berhasil dihapus."
            );

            load("");
            clear();

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Menghapus kelas", ex
            );
        }
    }

    // ================= LOAD TABLE =================

    private void load(String q) {

        try {

            List<Kelas> list = dao.search(q);

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Kelas x : list) {

                m.addRow(new Object[]{
                    x.getIdKelas(),
                    x.getNamaKelas(),
                    x.getKodeKursus() + " - " + x.getNamaKursus(),
                    x.getNamaInstruktur(),
                    x.getTanggalMulai(),
                    x.getKapasitas()
                });
            }

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Memuat kelas", ex
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

        try {

            for (Kelas x : dao.findAll()) {

                if (x.getIdKelas() == selected) {

                    nama.setText(x.getNamaKelas());

                    tanggal.setText(
                        x.getTanggalMulai().toString()
                    );

                    kapasitas.setText(
                        String.valueOf(x.getKapasitas())
                    );

                    ViewUtil.selectLookup(
                        kursus, x.getIdKursus()
                    );

                    ViewUtil.selectLookup(
                        instruktur, x.getIdInstruktur()
                    );

                    break;
                }
            }

        } catch (SQLException ex) {
            ViewUtil.sqlError(
                this, "Membaca kelas", ex
            );
        }
    }

    // ================= RESET =================

    private void clear() {

        selected = -1;

        nama.setText("");
        tanggal.setText(ViewUtil.today());
        kapasitas.setText("20");

        loadLookups();

        table.clearSelection();
        nama.requestFocus();
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
