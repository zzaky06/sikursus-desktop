
package view;

import dao.KelasDAO;
import dao.PendaftaranDAO;
import dao.PesertaDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.Pendaftaran;
import report.ReportService;
import util.LookupItem;
import util.Validation;

public class PendaftaranFrame extends JFrame {

    private final JComboBox<LookupItem> peserta = new JComboBox<>();
    private final JComboBox<LookupItem> kelas = new JComboBox<>();

    private final JTextField tanggal = new JTextField(ViewUtil.today());
    private final JTextField search = new JTextField(18);

    private final JComboBox<String> status = new JComboBox<>(
        new String[]{"DAFTAR", "AKTIF", "SELESAI", "BATAL"}
    );

    private final JTable table = new JTable(
        ViewUtil.tableModel(
            "ID", "NIM", "Peserta", "Kelas",
            "Kursus", "Tanggal", "Status"
        )
    );

    private final PendaftaranDAO dao = new PendaftaranDAO();

    private int selected = -1;

    private final Color BLUE = new Color(37, 99, 235);
    private final Color NAVY = new Color(15, 35, 70);
    private final Color BG = new Color(241, 245, 249);
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color WHITE = Color.WHITE;

    public PendaftaranFrame() {

        super("Transaksi Pendaftaran | SIKURSUS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1150, 780);
        setMinimumSize(new Dimension(1000, 700));
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

        JLabel title = new JLabel("Transaksi Pendaftaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Kelola pendaftaran peserta ke dalam kelas kursus."
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

        JLabel title = new JLabel("Informasi Pendaftaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Lengkapi informasi peserta dan kelas yang dipilih."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        // FORM FIELDS

        JPanel fields = new JPanel(new GridLayout(2, 2, 18, 16));
        fields.setBackground(WHITE);
        fields.setBorder(new EmptyBorder(22, 0, 20, 0));

        fields.add(createField("Pilih Peserta", peserta));
        fields.add(createField("Pilih Kelas", kelas));
        fields.add(createField("Tanggal Daftar (yyyy-MM-dd)", tanggal));
        fields.add(createField("Status Pendaftaran", status));

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

        JButton terpilih = createButton(
            "Cetak Terpilih", new Color(14, 165, 233), WHITE
        );

        JPanel actions = new JPanel(new FlowLayout(
            FlowLayout.RIGHT, 8, 0
        ));
        actions.setBackground(WHITE);

        actions.add(semua);
        actions.add(terpilih);
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
            ReportService.showPendaftaranSemua(this)
        );

        terpilih.addActionListener(e -> {

            if (selected < 0) {

                JOptionPane.showMessageDialog(
                    this, "Pilih baris pendaftaran terlebih dahulu."
                );

            } else {

                ReportService.showPendaftaranById(
                    this, selected
                );
            }
        });

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

        JLabel title = new JLabel("Daftar Pendaftaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Seluruh data pendaftaran peserta kursus."
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

        // TABLE

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

        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(115, 38));

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

            peserta.removeAllItems();

            for (LookupItem x : new PesertaDAO().lookup()) {
                peserta.addItem(x);
            }

            kelas.removeAllItems();

            for (LookupItem x : new KelasDAO().lookup()) {
                kelas.addItem(x);
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat pilihan pendaftaran", ex
            );
        }
    }

    // ================= DATA =================

    private Pendaftaran data() {

        LookupItem p = (LookupItem) peserta.getSelectedItem();
        LookupItem k = (LookupItem) kelas.getSelectedItem();

        if (p == null || k == null) {

            throw new IllegalArgumentException(
                "Peserta dan kelas wajib tersedia."
            );
        }

        Pendaftaran x = new Pendaftaran();

        x.setIdPendaftaran(Math.max(selected, 0));
        x.setIdPeserta(p.getId());
        x.setIdKelas(k.getId());

        x.setTanggalDaftar(
            Validation.date(
                tanggal.getText(), "Tanggal daftar"
            )
        );

        x.setStatus(status.getSelectedItem().toString());

        return x;
    }

    // ================= CRUD =================

    private void save() {

        try {

            dao.insert(data());

            JOptionPane.showMessageDialog(
                this, "Pendaftaran berhasil disimpan."
            );

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Menyimpan pendaftaran");
        }
    }

    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih pendaftaran terlebih dahulu."
            );

            return;
        }

        try {

            dao.update(data());

            JOptionPane.showMessageDialog(
                this, "Pendaftaran berhasil diperbarui."
            );

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Mengubah pendaftaran");
        }
    }

    private void delete() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                this, "Pilih pendaftaran terlebih dahulu."
            );

            return;
        }

        if (JOptionPane.showConfirmDialog(
            this,
            "Hapus pendaftaran? Pembayaran terkait harus dihapus lebih dahulu.",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                this, "Pendaftaran berhasil dihapus."
            );

            load("");
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Menghapus pendaftaran", ex
            );
        }
    }

    // ================= LOAD TABLE =================

    private void load(String q) {

        try {

            List<Pendaftaran> list = dao.search(q);

            DefaultTableModel m =
                (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Pendaftaran x : list) {

                m.addRow(new Object[]{
                    x.getIdPendaftaran(),
                    x.getNim(),
                    x.getNamaPeserta(),
                    x.getNamaKelas(),
                    x.getNamaKursus(),
                    x.getTanggalDaftar(),
                    x.getStatus()
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Memuat pendaftaran", ex
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

            for (Pendaftaran x : dao.findAll()) {

                if (x.getIdPendaftaran() == selected) {

                    ViewUtil.selectLookup(
                        peserta, x.getIdPeserta()
                    );

                    ViewUtil.selectLookup(
                        kelas, x.getIdKelas()
                    );

                    tanggal.setText(
                        x.getTanggalDaftar().toString()
                    );

                    status.setSelectedItem(x.getStatus());

                    break;
                }
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                this, "Membaca pendaftaran", ex
            );
        }
    }

    // ================= RESET =================

    private void clear() {

        selected = -1;

        tanggal.setText(ViewUtil.today());
        status.setSelectedIndex(0);

        table.clearSelection();

        loadLookups();

        peserta.requestFocus();
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
