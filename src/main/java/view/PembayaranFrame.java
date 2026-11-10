
package view;

import dao.PembayaranDAO;
import dao.PendaftaranDAO;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.Pembayaran;
import report.ReportService;
import util.LookupItem;
import util.Validation;

public class PembayaranFrame extends JFrame {

    private final JComboBox<LookupItem> pendaftaran = new JComboBox<>();

    private final JTextField tanggal = new JTextField(ViewUtil.today());
    private final JTextField nominal = new JTextField();
    private final JTextField search = new JTextField(18);

    private final JTextField awal = new JTextField(ViewUtil.today());
    private final JTextField akhir = new JTextField(ViewUtil.today());

    private final JComboBox<String> metode = new JComboBox<>(
        new String[]{"TUNAI", "TRANSFER", "QRIS"}
    );

    private final JComboBox<String> status = new JComboBox<>(
        new String[]{"MENUNGGU", "LUNAS", "BATAL"}
    );

    private final JTable table = new JTable(
        ViewUtil.tableModel(
            "ID", "Pendaftaran", "Peserta", "Kelas",
            "Tanggal", "Nominal", "Metode", "Status"
        )
    );

    private final PembayaranDAO dao = new PembayaranDAO();

    private int selected = -1;

    private final Color BLUE = new Color(37, 99, 235);
    private final Color NAVY = new Color(15, 35, 70);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color WHITE = Color.WHITE;

    public PembayaranFrame() {

        super("Transaksi Pembayaran | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1050, 700));
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

        JLabel title = new JLabel("Transaksi Pembayaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola pembayaran, status transaksi, dan laporan keuangan kursus."
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

        JLabel title = new JLabel("Informasi Pembayaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Masukkan detail transaksi pembayaran peserta kursus."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // FORM FIELDS

        JPanel fields = new JPanel(new GridLayout(2, 3, 16, 15));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Pendaftaran", pendaftaran));
        fields.add(createField("Tanggal Bayar (yyyy-MM-dd)", tanggal));
        fields.add(createField("Nominal Pembayaran (Rp)", nominal));
        fields.add(createField("Metode Pembayaran", metode));
        fields.add(createField("Status Pembayaran", status));

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

        JButton semua = createButton(
            "Cetak Semua", new Color(124, 58, 237), WHITE
        );

        JPanel actions = new JPanel(new FlowLayout(
            FlowLayout.RIGHT, 8, 0
        ));
        actions.setBackground(WHITE);

        actions.add(semua);
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

        semua.addActionListener(e ->
            ReportService.showPembayaranSemua(this)
        );

        getRootPane().setDefaultButton(simpan);

        return card;
    }

    // ================= TABLE =================

    private JPanel buildTable() {

        JPanel card = createCard();

        // TABLE HEADER

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(
            titlePanel, BoxLayout.Y_AXIS
        ));
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("Riwayat Pembayaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Daftar seluruh transaksi pembayaran yang tercatat."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        // FILTER PANEL

        JPanel filter = new JPanel(new FlowLayout(
            FlowLayout.LEFT, 8, 8
        ));
        filter.setBackground(WHITE);

        JLabel cariLabel = createFilterLabel("Cari");
        JLabel awalLabel = createFilterLabel("Awal");
        JLabel akhirLabel = createFilterLabel("Akhir");

        JButton cari = createButton(
            "Cari", BLUE, WHITE
        );

        JButton periode = createButton(
            "Cetak Periode", new Color(5, 150, 105), WHITE
        );

        styleSearchInput(search);
        styleSearchInput(awal);
        styleSearchInput(akhir);

        search.setPreferredSize(new Dimension(180, 36));
        awal.setPreferredSize(new Dimension(115, 36));
        akhir.setPreferredSize(new Dimension(115, 36));

        filter.add(cariLabel);
        filter.add(search);
        filter.add(cari);

        filter.add(Box.createHorizontalStrut(8));

        filter.add(awalLabel);
        filter.add(awal);

        filter.add(akhirLabel);
        filter.add(akhir);

        filter.add(periode);

        JPanel tableTop = new JPanel(new BorderLayout());
        tableTop.setBackground(WHITE);

        tableTop.add(header, BorderLayout.NORTH);
        tableTop.add(filter, BorderLayout.CENTER);

        // TABLE

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        card.add(tableTop, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        // EVENTS

        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

        periode.addActionListener(e -> period());

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

    private JLabel createFilterLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT);

        return label;
    }

    private void styleSearchInput(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setForeground(TEXT);
        field.setBackground(new Color(248, 250, 252));

        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(5, 9, 5, 9)
        ));
    }

    private JButton createButton(
        String text, Color background, Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(110, 37));

        return button;
    }

    // ================= TABLE STYLE =================

    private void styleTable() {

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(39);
        table.setForeground(TEXT);
        table.setBackground(WHITE);

        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);

        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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

        // COLUMN WIDTH

        int[] widths = {55, 100, 155, 150, 110, 120, 100, 105};

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i)
                .setPreferredWidth(widths[i]);
        }
    }

    // ================= LOOKUP =================

    private void loadLookups() {

        try {

            pendaftaran.removeAllItems();

            for (LookupItem x : new PendaftaranDAO().lookup()) {
                pendaftaran.addItem(x);
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat pilihan pembayaran", ex
            );
        }
    }

    // ================= DATA =================

    private Pembayaran data() {

        LookupItem p =
            (LookupItem) pendaftaran.getSelectedItem();

        if (p == null) {
            throw new IllegalArgumentException(
                "Pendaftaran wajib tersedia."
            );
        }

        Pembayaran x = new Pembayaran();

        x.setIdPembayaran(Math.max(selected, 0));
        x.setIdPendaftaran(p.getId());

        x.setTanggalBayar(
            Validation.date(
                tanggal.getText(), "Tanggal bayar"
            )
        );

        x.setNominal(
            Validation.positiveMoney(
                nominal.getText(), "Nominal"
            )
        );

        x.setMetode(metode.getSelectedItem().toString());
        x.setStatus(status.getSelectedItem().toString());

        return x;
    }

    // ================= CRUD =================

    private void save() {

        try {

            dao.insert(data());

            JOptionPane.showMessageDialog(
                this, "Pembayaran berhasil disimpan."
            );

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Menyimpan pembayaran");
        }
    }

    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih pembayaran terlebih dahulu."
            );

            return;
        }

        try {

            dao.update(data());

            JOptionPane.showMessageDialog(
                this, "Pembayaran berhasil diperbarui."
            );

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Mengubah pembayaran");
        }
    }

    private void delete() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih pembayaran terlebih dahulu."
            );

            return;
        }

        if (JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus pembayaran ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this, "Pembayaran berhasil dihapus."
            );

            load("");
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Menghapus pembayaran", ex
            );
        }
    }

    // ================= LOAD TABLE =================

    private void load(String q) {

        try {

            List<Pembayaran> list = dao.search(q);

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Pembayaran x : list) {

                m.addRow(new Object[]{
                    x.getIdPembayaran(),
                    x.getIdPendaftaran(),
                    x.getNamaPeserta(),
                    x.getNamaKelas(),
                    x.getTanggalBayar(),
                    x.getNominal(),
                    x.getMetode(),
                    x.getStatus()
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat pembayaran", ex
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

        int pid = Integer.parseInt(
            table.getValueAt(r, 1).toString()
        );

        ViewUtil.selectLookup(pendaftaran, pid);

        tanggal.setText(
            table.getValueAt(r, 4).toString()
        );

        nominal.setText(
            table.getValueAt(r, 5).toString()
        );

        metode.setSelectedItem(
            table.getValueAt(r, 6)
        );

        status.setSelectedItem(
            table.getValueAt(r, 7)
        );
    }

    // ================= REPORT PERIOD =================

    private void period() {

        try {

            Date a = Validation.date(
                awal.getText(), "Tanggal awal"
            );

            Date b = Validation.date(
                akhir.getText(), "Tanggal akhir"
            );

            if (a.after(b)) {

                throw new IllegalArgumentException(
                    "Tanggal awal tidak boleh setelah tanggal akhir."
                );
            }

            ReportService.showPembayaranPeriode(
                this, a, b
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validasi Tanggal",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ================= RESET =================

    private void clear() {

        selected = -1;

        tanggal.setText(ViewUtil.today());
        nominal.setText("");

        metode.setSelectedIndex(0);
        status.setSelectedIndex(0);

        table.clearSelection();

        loadLookups();

        pendaftaran.requestFocus();
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
