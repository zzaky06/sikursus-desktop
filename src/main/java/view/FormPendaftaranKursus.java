
package view;

import model.Peserta;
import dao.PesertaDAO;

import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FormPendaftaranKursus extends JFrame {

    // ================= WARNA =================
    private final Color burgundy = new Color(75, 18, 39);
    private final Color rose = new Color(145, 43, 75);
    private final Color blush = new Color(250, 229, 235);
    private final Color background = new Color(250, 246, 247);
    private final Color border = new Color(230, 207, 215);
    private final Color ink = new Color(48, 38, 43);

    // ================= DATABASE =================
    private final PesertaDAO dao = new PesertaDAO();
    private int selectedId = -1;

    // ================= FORM =================
    private JTextField txtNim;
    private JTextField txtNama;
    private JTextField txtProdi;
    private JTextField txtNoHp;
    private JTextField txtEmail;
    private JPasswordField txtKodeAkses;

    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private JRadioButton rbMahasiswa;
    private JRadioButton rbUmum;

    private ButtonGroup groupGender;
    private ButtonGroup groupJenis;

    private JComboBox<String> cmbKursus;

    private JCheckBox cbModul;
    private JCheckBox cbSertifikat;
    private JCheckBox cbRekaman;

    // Menyimpan hasil proses biaya untuk dicetak
    private JTextArea txtRingkasan;

    private JLabel lblMode;

    // ================= TABEL =================
    private JTextField txtCari;
    private JTable tblPeserta;
    private DefaultTableModel tableModel;

    // ================= CONSTRUCTOR =================
    public FormPendaftaranKursus() {

        setTitle("SIKURSUS Desktop | Pendaftaran Kursus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1050, 760));
        setSize(1280, 900);
        setLocationRelativeTo(null);

        txtRingkasan = new JTextArea();

        initComponents();
        loadTable("");
    }

    // ================= INIT COMPONENTS =================
    private void initComponents() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(background);

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(burgundy);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("Pendaftaran Kursus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "SIKURSUS | Sistem Informasi Pendaftaran Kursus"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(blush);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        header.add(heading, BorderLayout.WEST);

        JLabel logo = new JLabel("S", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);
        logo.setOpaque(true);
        logo.setBackground(rose);
        logo.setPreferredSize(new Dimension(60, 60));
        logo.setBorder(new LineBorder(blush, 2, true));

        header.add(logo, BorderLayout.EAST);

        // CONTENT
        JPanel content = new JPanel(new GridLayout(1, 2, 18, 0));
        content.setBackground(background);
        content.setBorder(new EmptyBorder(20, 24, 24, 24));

        content.add(buildFormPanel());
        content.add(buildTablePanel());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        JLabel footer = new JLabel(
            "SIKURSUS | Sistem Pendaftaran Kursus Desktop"
        );
        footer.setBorder(new EmptyBorder(10, 20, 10, 10));
        footer.setForeground(ink);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ================= CARD =================
    private JPanel createCard(String title) {

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(border, 1, true),
                new EmptyBorder(18, 18, 18, 18)
            )
        );

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(burgundy);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    // ================= FORM PANEL =================
    private JPanel buildFormPanel() {

        JPanel panel = createCard("Data Pendaftaran");

        lblMode = new JLabel("Mode: Data Baru");
        lblMode.setForeground(rose);
        lblMode.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMode.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblMode);
        panel.add(Box.createVerticalStrut(10));

        txtNim = createField();
        txtNama = createField();
        txtProdi = createField();
        txtNoHp = createField();
        txtEmail = createField();
        txtKodeAkses = new JPasswordField();

        styleField(txtKodeAkses);

        addField(panel, "NIM", txtNim);
        addField(panel, "Nama Peserta", txtNama);

        // JENIS KELAMIN
        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");

        groupGender = new ButtonGroup();
        groupGender.add(rbLaki);
        groupGender.add(rbPerempuan);

        styleRadio(rbLaki);
        styleRadio(rbPerempuan);

        addField(
            panel,
            "Jenis Kelamin",
            createInline(rbLaki, rbPerempuan)
        );

        addField(panel, "Program Studi", txtProdi);
        addField(panel, "Nomor HP", txtNoHp);
        addField(panel, "Email", txtEmail);
        addField(panel, "Kode Akses", txtKodeAkses);

        txtKodeAkses.setToolTipText("Kode akses: PBO2026");

        // JENIS PESERTA
        rbMahasiswa = new JRadioButton("Mahasiswa");
        rbUmum = new JRadioButton("Umum");

        groupJenis = new ButtonGroup();
        groupJenis.add(rbMahasiswa);
        groupJenis.add(rbUmum);

        styleRadio(rbMahasiswa);
        styleRadio(rbUmum);

        addField(
            panel,
            "Jenis Peserta",
            createInline(rbMahasiswa, rbUmum)
        );

        // KURSUS
        cmbKursus = new JComboBox<>(new String[]{
            "-- Pilih Kursus --",
            "Java Desktop",
            "Data Science",
            "Web Programming",
            "UI/UX Design"
        });

        styleCombo(cmbKursus);
        addField(panel, "Pilih Kursus", cmbKursus);

        // FASILITAS
        cbModul = new JCheckBox("Modul Cetak (+Rp50.000)");
        cbSertifikat = new JCheckBox("Sertifikat (+Rp75.000)");
        cbRekaman = new JCheckBox("Akses Rekaman (+Rp40.000)");

        styleCheck(cbModul);
        styleCheck(cbSertifikat);
        styleCheck(cbRekaman);

        JPanel fasilitas = new JPanel();
        fasilitas.setOpaque(false);
        fasilitas.setLayout(new BoxLayout(fasilitas, BoxLayout.Y_AXIS));

        fasilitas.add(cbModul);
        fasilitas.add(cbSertifikat);
        fasilitas.add(cbRekaman);

        addField(panel, "Fasilitas Tambahan", fasilitas);

        // BUTTON
        JPanel buttons = new JPanel(new GridLayout(2, 3, 7, 7));
        buttons.setOpaque(false);

        JButton btnSimpan = createButton(
            "Simpan", rose, Color.WHITE
        );

        JButton btnUbah = createButton(
            "Ubah", new Color(75, 112, 150), Color.WHITE
        );

        JButton btnHapus = createButton(
            "Hapus", new Color(174, 58, 68), Color.WHITE
        );

        JButton btnReset = createButton(
            "Reset", blush, burgundy
        );

        JButton btnCetak = createButton(
            "Cetak", burgundy, Color.WHITE
        );

        JButton btnProses = createButton(
            "Proses Biaya", new Color(105, 75, 120), Color.WHITE
        );

        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());
        btnCetak.addActionListener(e -> cetakRingkasan());
        btnProses.addActionListener(e -> prosesRingkasan());

        buttons.add(btnSimpan);
        buttons.add(btnUbah);
        buttons.add(btnHapus);
        buttons.add(btnReset);
        buttons.add(btnCetak);
        buttons.add(btnProses);

        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(buttons);

        return panel;
    }

    // ================= TABLE PANEL =================
    private JPanel buildTablePanel() {

        JPanel panel = createCard("Data Peserta");

        JLabel label = new JLabel("Cari NIM / Nama / Prodi:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(ink);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCari = createField();

        JPanel search = new JPanel(new BorderLayout(7, 0));
        search.setOpaque(false);

        JButton btnRefresh = createButton(
            "Muat Ulang", blush, burgundy
        );

        JButton btnCetakData = createButton(
            "Cetak Data", burgundy, Color.WHITE
        );

        btnRefresh.addActionListener(
            e -> loadTable(txtCari.getText())
        );

        btnCetakData.addActionListener(e -> cetakDataPeserta());

        JPanel tombolTabel = new JPanel(
            new GridLayout(1, 2, 5, 0)
        );
        tombolTabel.setOpaque(false);
        tombolTabel.add(btnRefresh);
        tombolTabel.add(btnCetakData);

        search.add(txtCari, BorderLayout.CENTER);
        search.add(tombolTabel, BorderLayout.EAST);

        search.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(search);
        panel.add(Box.createVerticalStrut(12));

        // MODEL TABEL
        tableModel = new DefaultTableModel(
            new Object[]{
                "ID",
                "NIM",
                "Nama",
                "Jenis Kelamin",
                "Prodi",
                "No. HP",
                "Email"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPeserta = new JTable(tableModel);

        tblPeserta.setRowHeight(30);
        tblPeserta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPeserta.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tblPeserta.setAutoCreateRowSorter(true);

        tblPeserta.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        tblPeserta.getTableHeader().setBackground(blush);
        tblPeserta.getTableHeader().setForeground(burgundy);

        tblPeserta.setGridColor(border);
        tblPeserta.setSelectionBackground(blush);
        tblPeserta.setSelectionForeground(burgundy);

        JScrollPane tableScroll = new JScrollPane(tblPeserta);
        tableScroll.setPreferredSize(new Dimension(500, 600));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(tableScroll);

        // PENCARIAN LANGSUNG
        txtCari.getDocument().addDocumentListener(
            new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    loadTable(txtCari.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    loadTable(txtCari.getText());
                }

                public void changedUpdate(DocumentEvent e) {
                    loadTable(txtCari.getText());
                }
            }
        );

        // KLIK BARIS TABEL
        tblPeserta.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tblPeserta.getSelectedRow() >= 0) {

                int row = tblPeserta.convertRowIndexToModel(
                    tblPeserta.getSelectedRow()
                );

                pilihDataTabel(row);
            }
        });

        return panel;
    }

    // ================= PILIH DATA TABEL =================
    private void pilihDataTabel(int row) {

        selectedId = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString()
        );

        txtNim.setText(valueAt(row, 1));
        txtNama.setText(valueAt(row, 2));

        String jk = valueAt(row, 3);

        rbLaki.setSelected("Laki-laki".equals(jk));
        rbPerempuan.setSelected("Perempuan".equals(jk));

        txtProdi.setText(valueAt(row, 4));
        txtNoHp.setText(valueAt(row, 5));
        txtEmail.setText(valueAt(row, 6));

        lblMode.setText("Mode: Mengubah data ID " + selectedId);
    }

    private String valueAt(int row, int col) {

        Object value = tableModel.getValueAt(row, col);

        return value == null ? "" : value.toString();
    }

    // ================= LOAD DATA DATABASE =================
    private void loadTable(String keyword) {

        if (tableModel == null) return;

        try {

            List<Peserta> data = dao.findAll();

            tableModel.setRowCount(0);

            String key = keyword == null
                ? ""
                : keyword.trim().toLowerCase(Locale.ROOT);

            for (Peserta p : data) {

                String jk = p.getJenisKelamin();

                String jkTampil =
                    "L".equalsIgnoreCase(jk)
                        ? "Laki-laki"
                        : ("P".equalsIgnoreCase(jk)
                            ? "Perempuan" : jk);

                String gabung =
                    p.getNim() + " "
                    + p.getNama() + " "
                    + p.getProdi();

                if (gabung.toLowerCase(Locale.ROOT).contains(key)) {

                    tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNim(),
                        p.getNama(),
                        jkTampil,
                        p.getProdi(),
                        p.getNoHp(),
                        p.getEmail()
                    });
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= VALIDASI DATA PESERTA =================
    private boolean validasiDataPeserta() {

        if (txtNim.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtProdi.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "NIM, nama, dan program studi wajib diisi."
            );

            return false;
        }

        if (!rbLaki.isSelected() && !rbPerempuan.isSelected()) {

            JOptionPane.showMessageDialog(
                this,
                "Silakan pilih jenis kelamin."
            );

            return false;
        }

        return true;
    }

    // ================= VALIDASI PROSES BIAYA =================
    private boolean validasiProsesBiaya() {

        if (!validasiDataPeserta()) {
            return false;
        }

        if (!new String(txtKodeAkses.getPassword())
                .equals("PBO2026")) {

            JOptionPane.showMessageDialog(
                this,
                "Kode akses salah! Gunakan PBO2026."
            );

            txtKodeAkses.requestFocus();
            return false;
        }

        if (!rbMahasiswa.isSelected() && !rbUmum.isSelected()) {

            JOptionPane.showMessageDialog(
                this,
                "Silakan pilih jenis peserta."
            );

            return false;
        }

        if (cmbKursus.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Silakan pilih kursus terlebih dahulu."
            );

            return false;
        }

        return true;
    }

    // ================= MEMBUAT OBJECT PESERTA =================
    private Peserta buatPeserta(int id) {

        String jk = rbLaki.isSelected() ? "L" : "P";

        return new Peserta(
            id,
            txtNama.getText().trim(),
            txtNoHp.getText().trim(),
            txtNim.getText().trim(),
            jk,
            txtProdi.getText().trim(),
            txtEmail.getText().trim()
        );
    }

    // ================= SIMPAN DATA =================
    private void simpanData() {

        if (!validasiDataPeserta()) {
            return;
        }

        try {

            Peserta peserta = buatPeserta(0);

            if (dao.insert(peserta)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Data peserta berhasil disimpan ke database!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadTable(txtCari.getText());
                kosongkanForm();
            }

        } catch (SQLException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan data:\n" + ex.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= UBAH DATA =================
    private void ubahData() {

        if (selectedId < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Pilih data peserta pada tabel terlebih dahulu."
            );

            return;
        }

        if (!validasiDataPeserta()) {
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin memperbarui data peserta ini?",
            "Konfirmasi Ubah",
            JOptionPane.YES_NO_OPTION
        );

        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            Peserta peserta = buatPeserta(selectedId);

            if (dao.update(peserta)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Data peserta berhasil diperbarui di database!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadTable(txtCari.getText());
                kosongkanForm();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Data tidak ditemukan atau tidak mengalami perubahan."
                );
            }

        } catch (SQLException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal memperbarui data:\n" + ex.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= HAPUS DATA =================
    private void hapusData() {

        if (selectedId < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Pilih data peserta pada tabel terlebih dahulu."
            );

            return;
        }

        int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus data peserta ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (dao.delete(selectedId)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Data peserta berhasil dihapus dari database!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadTable(txtCari.getText());
                kosongkanForm();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Data peserta tidak ditemukan."
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal menghapus data:\n" + ex.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= PROSES BIAYA =================
    private void prosesRingkasan() {

        if (!validasiProsesBiaya()) {
            return;
        }

        String kursus = cmbKursus.getSelectedItem().toString();

        double dasar = biayaDasar(kursus);

        double diskon = rbMahasiswa.isSelected()
            ? dasar * 0.10
            : 0;

        double fasilitas =
            (cbModul.isSelected() ? 50000 : 0)
            + (cbSertifikat.isSelected() ? 75000 : 0)
            + (cbRekaman.isSelected() ? 40000 : 0);

        double total = dasar - diskon + fasilitas;

        StringBuilder daftarFasilitas = new StringBuilder();

        if (cbModul.isSelected()) {
            daftarFasilitas.append("- Modul Cetak\n");
        }

        if (cbSertifikat.isSelected()) {
            daftarFasilitas.append("- Sertifikat\n");
        }

        if (cbRekaman.isSelected()) {
            daftarFasilitas.append("- Akses Rekaman\n");
        }

        if (daftarFasilitas.length() == 0) {
            daftarFasilitas.append("- Tidak ada fasilitas tambahan\n");
        }

        String ringkasan =
            "              RINGKASAN PENDAFTARAN KURSUS\n"
            + "================================================\n\n"
            + "DATA PESERTA\n"
            + "------------------------------------------------\n"
            + "NIM             : " + txtNim.getText() + "\n"
            + "Nama Peserta    : " + txtNama.getText() + "\n"
            + "Jenis Kelamin   : "
            + (rbLaki.isSelected() ? "Laki-laki" : "Perempuan") + "\n"
            + "Program Studi   : " + txtProdi.getText() + "\n"
            + "Nomor HP        : " + txtNoHp.getText() + "\n"
            + "Email           : " + txtEmail.getText() + "\n"
            + "Jenis Peserta   : "
            + (rbMahasiswa.isSelected() ? "Mahasiswa" : "Umum") + "\n"
            + "Kursus          : " + kursus + "\n\n"
            + "RINCIAN BIAYA\n"
            + "------------------------------------------------\n"
            + "Biaya Dasar     : " + rupiah(dasar) + "\n"
            + "Diskon          : " + rupiah(diskon) + "\n"
            + "Fasilitas:\n" + daftarFasilitas
            + "Biaya Fasilitas : " + rupiah(fasilitas) + "\n"
            + "------------------------------------------------\n"
            + "TOTAL BIAYA     : " + rupiah(total) + "\n"
            + "================================================\n"
            + "Terima kasih telah mendaftar di SIKURSUS.\n";

        // Simpan hasil agar bisa dicetak
        txtRingkasan.setText(ringkasan);
        txtRingkasan.setCaretPosition(0);

        // Tampilkan hasil proses biaya
        JTextArea areaHasil = new JTextArea(ringkasan);
        areaHasil.setEditable(false);
        areaHasil.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHasil.setBackground(new Color(255, 249, 251));
        areaHasil.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(areaHasil);
        scroll.setPreferredSize(new Dimension(480, 420));

        JOptionPane.showMessageDialog(
            this,
            scroll,
            "Hasil Proses Biaya",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= BIAYA KURSUS =================
    private double biayaDasar(String kursus) {

        switch (kursus) {

            case "Java Desktop":
                return 500000;

            case "Data Science":
                return 650000;

            case "Web Programming":
                return 450000;

            case "UI/UX Design":
                return 400000;

            default:
                return 0;
        }
    }

    // ================= FORMAT RUPIAH =================
    private String rupiah(double value) {

        NumberFormat nf =
            NumberFormat.getCurrencyInstance(
                new Locale("id", "ID")
            );

        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);

        return nf.format(value);
    }

    // ================= CETAK RINGKASAN =================
    private void cetakRingkasan() {

        if (txtRingkasan.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Silakan tekan Proses Biaya terlebih dahulu."
            );

            return;
        }

        try {

            txtRingkasan.print();

        } catch (PrinterException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal mencetak ringkasan:\n" + ex.getMessage(),
                "Kesalahan Cetak",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= CETAK TABEL PESERTA =================
    private void cetakDataPeserta() {

        if (tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Tidak ada data peserta untuk dicetak."
            );

            return;
        }

        try {

            boolean selesai = tblPeserta.print(
                JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat("DATA PESERTA SIKURSUS"),
                new java.text.MessageFormat("Halaman {0}")
            );

            if (selesai) {

                JOptionPane.showMessageDialog(
                    this,
                    "Proses pencetakan data selesai."
                );
            }

        } catch (PrinterException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Gagal mencetak data peserta:\n" + ex.getMessage(),
                "Kesalahan Cetak",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= RESET FORM =================
    private void resetForm() {

        int jawab = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin mengosongkan seluruh form?",
            "Konfirmasi Reset",
            JOptionPane.YES_NO_OPTION
        );

        if (jawab == JOptionPane.YES_OPTION) {

            kosongkanForm();

            JOptionPane.showMessageDialog(
                this,
                "Form berhasil dikosongkan."
            );
        }
    }

    private void kosongkanForm() {

        txtNim.setText("");
        txtNama.setText("");
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtKodeAkses.setText("");

        groupGender.clearSelection();
        groupJenis.clearSelection();

        cmbKursus.setSelectedIndex(0);

        cbModul.setSelected(false);
        cbSertifikat.setSelected(false);
        cbRekaman.setSelected(false);

        txtRingkasan.setText("");

        selectedId = -1;

        lblMode.setText("Mode: Data Baru");

        tblPeserta.clearSelection();

        txtNim.requestFocus();
    }

    // ================= KOMPONEN BANTUAN =================
    private JTextField createField() {

        JTextField field = new JTextField();

        styleField(field);

        return field;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(Color.WHITE);
        field.setForeground(ink);

        field.setPreferredSize(new Dimension(250, 36));
        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 36)
        );

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(border, 1, true),
                new EmptyBorder(7, 10, 7, 10)
            )
        );
    }

    private void styleCombo(JComboBox<String> combo) {

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(250, 36));
        combo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 36)
        );
    }

    private void styleRadio(JRadioButton radio) {

        radio.setOpaque(false);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        radio.setForeground(ink);
        radio.setFocusPainted(false);
    }

    private void styleCheck(JCheckBox check) {

        check.setOpaque(false);
        check.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        check.setForeground(ink);
        check.setFocusPainted(false);
    }

    private void addField(
            JPanel panel,
            String label,
            JComponent component) {

        JLabel lbl = new JLabel(label);

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(ink);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(component);
        panel.add(Box.createVerticalStrut(10));
    }

    private JPanel createInline(
            JComponent a,
            JComponent b) {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 14, 0)
        );

        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(a);
        panel.add(b);

        return panel;
    }

    private JButton createButton(
            String text,
            Color bg,
            Color fg) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bg);
        button.setForeground(fg);

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(new Dimension(110, 38));

        return button;
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        try {

            for (UIManager.LookAndFeelInfo info :
                    UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {

                    UIManager.setLookAndFeel(
                        info.getClassName()
                    );

                    break;
                }
            }

        } catch (Exception ex) {

            java.util.logging.Logger.getLogger(
                FormPendaftaranKursus.class.getName()
            ).log(
                java.util.logging.Level.WARNING,
                "Nimbus tidak dapat diaktifkan",
                ex
            );
        }

        EventQueue.invokeLater(() -> {
            new FormPendaftaranKursus().setVisible(true);
        });
    }
}
