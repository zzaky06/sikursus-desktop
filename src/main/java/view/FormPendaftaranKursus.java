package view;

import model.Peserta;
import dao.PesertaDAO;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class FormPendaftaranKursus extends JFrame {

    // Palet warna modern burgundy, rose, dan blush
    private final Color biruGelap = new Color(59, 12,  30);
    private final Color biruLangit = new Color(128, 30, 55);
    private final Color biruTerang = new Color(250, 225, 230);
    private final Color latar = new Color(250, 245, 246);
    private final Color putih = Color.WHITE;
    private final Color teks = new Color(30, 41, 59);
    private final Color teksSekunder = new Color(245, 220, 226);
    private final Color garis = new Color(231, 207, 214);
    private final Color warnaKartu = new Color(255, 255, 255);
    private final Color cyan = new Color(173, 55, 83);

    private JPanel panelUtama;
    private JPanel panelHeader;
    private JPanel panelKonten;
    private JPanel panelForm;
    private JPanel panelRingkasan;
    private JPanel panelJenis;
    private JPanel panelGender;
    private JPanel panelFasilitas;
    private JPanel panelTombol;

    private JLabel lblJudul;
    private JLabel lblSubjudul;
    private JLabel lblNim;
    private JLabel lblNama;
    private JLabel lblGender;
    private JLabel lblProdi;
    private JLabel lblNoHp;
    private JLabel lblEmail;
    private JLabel lblKodeAkses;
    private JLabel lblJenis;
    private JLabel lblKursus;
    private JLabel lblFasilitas;
    private JLabel lblRingkasan;

    // Data peserta
    private JTextField txtNim;
    private JTextField txtNama;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private ButtonGroup groupGender;
    private JTextField txtProdi;
    private JTextField txtNoHp;
    private JTextField txtEmail;

    // Komponen pendaftaran kursus asli
    private JPasswordField txtKodeAkses;
    private JRadioButton rbMahasiswa;
    private JRadioButton rbUmum;
    private ButtonGroup groupJenis;
    private JComboBox<String> cmbKursus;
    private JCheckBox cbModul;
    private JCheckBox cbSertifikat;
    private JCheckBox cbRekaman;
    private JTextArea txtRingkasan;
    private JScrollPane scrollRingkasan;
    private JButton btnProses;
    private JButton btnReset;

    public FormPendaftaranKursus() {
        initComponents();

        groupJenis.add(rbMahasiswa);
        groupJenis.add(rbUmum);
        groupGender.add(rbLaki);
        groupGender.add(rbPerempuan);
        rbLaki.setSelected(true);

        txtRingkasan.setEditable(false);
        txtRingkasan.setLineWrap(true);
        txtRingkasan.setWrapStyleWord(true);
        getRootPane().setDefaultButton(btnProses);

        setMinimumSize(new Dimension(1050, 760));
        setSize(new Dimension(1280, 900));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        panelUtama = new JPanel(new BorderLayout(0, 0));
        panelHeader = new JPanel(new BorderLayout());
        panelKonten = new JPanel(new GridBagLayout());
        panelForm = new JPanel(new GridBagLayout());
        panelRingkasan = new JPanel(new BorderLayout(0, 12));
        panelJenis = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        panelGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        panelFasilitas = new JPanel(new GridBagLayout());
        panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));

        lblJudul = new JLabel("Pendaftaran Kursus");
        lblSubjudul = new JLabel("SIKURSUS  •  Lengkapi data peserta dan pilih kursus yang sesuai");
        lblNim = new JLabel("NIM");
        lblNama = new JLabel("Nama Peserta");
        lblGender = new JLabel("Jenis Kelamin");
        lblProdi = new JLabel("Program Studi");
        lblNoHp = new JLabel("Nomor HP");
        lblEmail = new JLabel("Email");
        lblKodeAkses = new JLabel("Kode Akses");
        lblJenis = new JLabel("Jenis Peserta");
        lblKursus = new JLabel("Pilih Kursus");
        lblFasilitas = new JLabel("Fasilitas Tambahan");
        lblRingkasan = new JLabel("Ringkasan Pendaftaran");

        txtNim = new JTextField();
        txtNama = new JTextField();
        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        groupGender = new ButtonGroup();
        txtProdi = new JTextField();
        txtNoHp = new JTextField();
        txtEmail = new JTextField();

        txtKodeAkses = new JPasswordField();
        rbMahasiswa = new JRadioButton("Mahasiswa");
        rbUmum = new JRadioButton("Umum");
        groupJenis = new ButtonGroup();

        cmbKursus = new JComboBox<String>(new String[] {
            "-- Pilih Kursus --",
            "Java Desktop",
            "Data Science",
            "Web Programming",
            "UI/UX Design"
        });

        cbModul = new JCheckBox("Modul Cetak  (+Rp50.000)");
        cbSertifikat = new JCheckBox("Sertifikat  (+Rp75.000)");
        cbRekaman = new JCheckBox("Akses Rekaman  (+Rp40.000)");

        txtRingkasan = new JTextArea();
        scrollRingkasan = new JScrollPane(txtRingkasan);
        btnProses = new JButton("Proses Pendaftaran");
        btnReset = new JButton("Reset Form");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SIKURSUS Desktop | Pendaftaran Kursus");

        panelUtama.setBackground(latar);
        panelHeader.setBackground(biruGelap);
        panelKonten.setBackground(latar);
        panelForm.setBackground(warnaKartu);
        panelRingkasan.setBackground(warnaKartu);
        panelJenis.setBackground(warnaKartu);
        panelGender.setBackground(warnaKartu);
        panelFasilitas.setBackground(warnaKartu);
        panelTombol.setBackground(warnaKartu);

        panelHeader.setBorder(new EmptyBorder(24, 34, 24, 34));
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblJudul.setForeground(putih);
        lblSubjudul.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubjudul.setForeground(new Color(250, 225, 230));

        JPanel teksHeader = new JPanel(new GridBagLayout());
        teksHeader.setOpaque(false);
        GridBagConstraints h = new GridBagConstraints();
        h.gridx = 0;
        h.gridy = 0;
        h.anchor = GridBagConstraints.WEST;
        h.insets = new Insets(0, 0, 6, 0);
        teksHeader.add(lblJudul, h);
        h.gridy = 1;
        h.insets = new Insets(0, 0, 0, 0);
        teksHeader.add(lblSubjudul, h);
        panelHeader.add(teksHeader, BorderLayout.WEST);

        JLabel logo = new JLabel("S");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(putih);
        logo.setOpaque(true);
        logo.setBackground(new Color(153, 41, 68));
        logo.setPreferredSize(new Dimension(66, 66));
        logo.setBorder(new LineBorder(cyan, 2, true));
        panelHeader.add(logo, BorderLayout.EAST);

        // Animasi aksen burgundy yang halus pada logo header.
        Timer animasiLogo = new Timer(45, new java.awt.event.ActionListener() {
            private int langkah = 0;
            private boolean menujuTerang = true;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                langkah += menujuTerang ? 3 : -3;
                if (langkah >= 30) { langkah = 30; menujuTerang = false; }
                if (langkah <= 0) { langkah = 0; menujuTerang = true; }
                int r = Math.min(255, 128 + langkah);
                int g = Math.min(255, 30 + langkah / 2);
                int b = Math.min(255, 55 + langkah / 2);
                logo.setBackground(new Color(r, g, b));
            }
        });
        animasiLogo.start();

        panelKonten.setBorder(new EmptyBorder(24, 28, 24, 28));
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(231, 207, 214), 1, true),
            new EmptyBorder(22, 24, 22, 24)));
        panelRingkasan.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(231, 207, 214), 1, true),
            new EmptyBorder(22, 22, 20, 22)));

        styleLabel(lblNim);
        styleLabel(lblNama);
        styleLabel(lblGender);
        styleLabel(lblProdi);
        styleLabel(lblNoHp);
        styleLabel(lblEmail);
        styleLabel(lblKodeAkses);
        styleLabel(lblJenis);
        styleLabel(lblKursus);
        styleLabel(lblFasilitas);
        lblRingkasan.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblRingkasan.setForeground(biruGelap);

        styleField(txtNim);
        styleField(txtNama);
        styleField(txtProdi);
        styleField(txtNoHp);
        styleField(txtEmail);
        styleField(txtKodeAkses);
        styleCombo(cmbKursus);
        txtKodeAkses.setToolTipText("Kode akses demo praktikum: PBO2026");

        styleRadio(rbLaki);
        styleRadio(rbPerempuan);
        styleRadio(rbMahasiswa);
        styleRadio(rbUmum);
        panelGender.add(rbLaki);
        panelGender.add(rbPerempuan);
        panelJenis.add(rbMahasiswa);
        panelJenis.add(rbUmum);

        styleCheckBox(cbModul);
        styleCheckBox(cbSertifikat);
        styleCheckBox(cbRekaman);
        addFacility(0, cbModul);
        addFacility(1, cbSertifikat);
        addFacility(2, cbRekaman);

        txtRingkasan.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtRingkasan.setForeground(teks);
        txtRingkasan.setBackground(new Color(255, 248, 250));
        txtRingkasan.setBorder(new EmptyBorder(14, 14, 14, 14));
        txtRingkasan.setCaretColor(biruLangit);
        scrollRingkasan.setBorder(new LineBorder(garis, 1, true));
        scrollRingkasan.getViewport().setBackground(new Color(255, 248, 250));

        styleButton(btnProses, biruLangit, putih, new Dimension(205, 46));
        styleButton(btnReset, biruTerang, biruGelap, new Dimension(135, 46));
        btnProses.addActionListener(evt -> btnProsesActionPerformed());
        btnReset.addActionListener(evt -> btnResetActionPerformed());
        panelTombol.add(btnProses);
        panelTombol.add(btnReset);

        // Form disusun rapi dalam satu kolom agar seluruh data peserta tersedia.
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.weightx = 1.0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.anchor = GridBagConstraints.NORTHWEST;

        addFormRow(lblNim, txtNim, 0, f);
        addFormRow(lblNama, txtNama, 2, f);
        addFormRow(lblGender, panelGender, 4, f);
        addFormRow(lblProdi, txtProdi, 6, f);
        addFormRow(lblNoHp, txtNoHp, 8, f);
        addFormRow(lblEmail, txtEmail, 10, f);
        addFormRow(lblKodeAkses, txtKodeAkses, 12, f);
        addFormRow(lblJenis, panelJenis, 14, f);
        addFormRow(lblKursus, cmbKursus, 16, f);
        addFormRow(lblFasilitas, panelFasilitas, 18, f);

        f.gridy = 20;
        f.insets = new Insets(8, 0, 0, 0);
        panelForm.add(panelTombol, f);

        panelRingkasan.add(lblRingkasan, BorderLayout.NORTH);
        panelRingkasan.add(scrollRingkasan, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 18);
        c.gridx = 0;
        c.weightx = 1.0;
        panelKonten.add(panelForm, c);

        c.gridx = 1;
        c.weightx = 0.9;
        c.insets = new Insets(0, 0, 0, 0);
        panelKonten.add(panelRingkasan, c);

        JLabel footer = new JLabel("  SIKURSUS  |  Sistem Pendaftaran Kursus Desktop");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(teksSekunder);
        footer.setBorder(new EmptyBorder(0, 30, 12, 0));

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelKonten, BorderLayout.CENTER);
        panelUtama.add(footer, BorderLayout.SOUTH);
        setContentPane(panelUtama);
    }

    private void addFormRow(JLabel label, java.awt.Component component,
                            int startRow, GridBagConstraints f) {
        f.gridy = startRow;
        f.insets = new Insets(0, 0, 6, 0);
        panelForm.add(label, f);
        f.gridy = startRow + 1;
        f.insets = new Insets(0, 0, 13, 0);
        panelForm.add(component, f);
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(teks);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(teks);
        field.setBackground(putih);
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(garis, 1, true),
            new EmptyBorder(8, 12, 8, 12)));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(biruLangit, 2, true),
                    new EmptyBorder(7, 11, 7, 11)));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(garis, 1, true),
                    new EmptyBorder(8, 12, 8, 12)));
            }
        });
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(teks);
        combo.setBackground(putih);
        combo.setPreferredSize(new Dimension(300, 40));
        combo.setBorder(new LineBorder(garis, 1, true));
    }

    private void styleRadio(JRadioButton radio) {
        radio.setBackground(warnaKartu);
        radio.setForeground(teks);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radio.setFocusPainted(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setForeground(teks);
        checkBox.setBackground(warnaKartu);
        checkBox.setFocusPainted(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleButton(JButton button, Color background, Color foreground,
                             Dimension size) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 90), 1, true),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        final Color hover = background.equals(biruLangit)
            ? new Color(166, 47, 76)
            : new Color(244, 205, 215);
        final Color[] current = { background };
        final Timer[] animation = { null };

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private void animateTo(Color target) {
                if (animation[0] != null && animation[0].isRunning()) {
                    animation[0].stop();
                }
                final Color from = current[0];
                final int[] step = { 0 };
                animation[0] = new Timer(15, event -> {
                    step[0]++;
                    float t = Math.min(1f, step[0] / 10f);
                    int r = (int) (from.getRed() + (target.getRed() - from.getRed()) * t);
                    int g = (int) (from.getGreen() + (target.getGreen() - from.getGreen()) * t);
                    int b = (int) (from.getBlue() + (target.getBlue() - from.getBlue()) * t);
                    Color color = new Color(r, g, b);
                    button.setBackground(color);
                    current[0] = color;
                    if (t >= 1f) {
                        animation[0].stop();
                        current[0] = target;
                    }
                });
                animation[0].start();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                animateTo(hover);
                button.setLocation(button.getX(), button.getY() - 1);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                animateTo(background);
                button.setLocation(button.getX(), button.getY() + 1);
            }
        });
    }

    private void addFacility(int row, JCheckBox checkBox) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 0, 4, 0);
        panelFasilitas.add(checkBox, c);
    }

    private String getJenisPeserta() {
        if (rbMahasiswa.isSelected()) {
            return "Mahasiswa";
        }
        if (rbUmum.isSelected()) {
            return "Umum";
        }
        return "";
    }

    private String getJenisKelamin() {
        if (rbLaki.isSelected()) {
            return "Laki-laki";
        }
        if (rbPerempuan.isSelected()) {
            return "Perempuan";
        }
        return "";
    }

    private double hitungBiayaDasar(String kursus) {
        switch (kursus) {
            case "Java Desktop":
                return 500_000;
            case "Data Science":
                return 650_000;
            case "Web Programming":
                return 450_000;
            case "UI/UX Design":
                return 400_000;
            default:
                return 0;
        }
    }

    private double hitungBiayaFasilitas() {
        double total = 0;
        if (cbModul.isSelected()) {
            total += 50_000;
        }
        if (cbSertifikat.isSelected()) {
            total += 75_000;
        }
        if (cbRekaman.isSelected()) {
            total += 40_000;
        }
        return total;
    }

    private String buatRingkasanFasilitas() {
        String[] fasilitasDipilih = new String[3];
        int jumlah = 0;

        if (cbModul.isSelected()) {
            fasilitasDipilih[jumlah] = "Modul Cetak";
            jumlah++;
        }
        if (cbSertifikat.isSelected()) {
            fasilitasDipilih[jumlah] = "Sertifikat";
            jumlah++;
        }
        if (cbRekaman.isSelected()) {
            fasilitasDipilih[jumlah] = "Akses Rekaman";
            jumlah++;
        }

        if (jumlah == 0) {
            return "- Tidak ada fasilitas tambahan\n";
        }

        StringBuilder hasil = new StringBuilder();
        for (int i = 0; i < jumlah; i++) {
            hasil.append("- ").append(fasilitasDipilih[i]).append("\n");
        }
        return hasil.toString();
    }

    private boolean validasiInput() {
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = txtProdi.getText().trim();
        String kode = new String(txtKodeAkses.getPassword());

        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty()
                || (!rbLaki.isSelected() && !rbPerempuan.isSelected())) {
            JOptionPane.showMessageDialog(this,
                "NIM, nama, jenis kelamin, dan prodi wajib diisi.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!kode.equals("PBO2026")) {
            JOptionPane.showMessageDialog(this,
                "Kode akses demo salah.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            txtKodeAkses.requestFocus();
            return false;
        }

        if (!rbMahasiswa.isSelected() && !rbUmum.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Pilih jenis peserta.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cmbKursus.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih kursus terlebih dahulu.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            cmbKursus.requestFocus();
            return false;
        }

        return true;
    }

    private void btnProsesActionPerformed() {
        if (!validasiInput()) {
            return;
        }

        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = txtProdi.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();

        // Nilai jenis kelamin yang disimpan ke database: L atau P
        String jenisKelamin = "";
        if (rbLaki.isSelected()) {
            jenisKelamin = "L";
        } else if (rbPerempuan.isSelected()) {
            jenisKelamin = "P";
        }

        // Bentuk object Peserta, lalu simpan melalui DAO
        Peserta peserta = new Peserta(
            0, nama, noHp, nim, jenisKelamin, prodi, email
        );

        PesertaDAO dao = new PesertaDAO();
        try {
            boolean berhasil = dao.insert(peserta);

            if (!berhasil) {
                JOptionPane.showMessageDialog(this,
                    "Data peserta tidak tersimpan.",
                    "Informasi",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Jika data peserta berhasil disimpan, tampilkan rincian kursus.
            String jenis = getJenisPeserta();
            String kursus = cmbKursus.getSelectedItem().toString();

            double biayaDasar = hitungBiayaDasar(kursus);
            double diskon = jenis.equals("Mahasiswa") ? biayaDasar * 0.10 : 0;
            double biayaFasilitas = hitungBiayaFasilitas();
            double total = biayaDasar - diskon + biayaFasilitas;

            String fasilitas = buatRingkasanFasilitas();

            String ringkasan =
                "RINGKASAN PENDAFTARAN KURSUS\n"
                + "====================================\n\n"
                + "DATA PESERTA\n"
                + "------------------------------------\n"
                + "NIM             : " + nilaiAtauStrip(nim) + "\n"
                + "Nama Peserta    : " + nama + "\n"
                + "Jenis Kelamin   : " + nilaiAtauStrip(getJenisKelamin()) + "\n"
                + "Program Studi   : " + nilaiAtauStrip(prodi) + "\n"
                + "Nomor HP        : " + nilaiAtauStrip(noHp) + "\n"
                + "Email           : " + nilaiAtauStrip(email) + "\n"
                + "Jenis Peserta   : " + jenis + "\n"
                + "Kursus          : " + kursus + "\n\n"
                + "RINCIAN BIAYA\n"
                + "------------------------------------\n"
                + "Biaya Dasar     : " + formatRupiah(biayaDasar) + "\n"
                + "Diskon          : " + formatRupiah(diskon) + "\n"
                + "Fasilitas:\n" + fasilitas
                + "Biaya Fasilitas : " + formatRupiah(biayaFasilitas) + "\n"
                + "------------------------------------\n"
                + "TOTAL           : " + formatRupiah(total) + "\n";

            txtRingkasan.setText(ringkasan);
            txtRingkasan.setCaretPosition(0);

            JOptionPane.showMessageDialog(this,
                "Data peserta berhasil disimpan dan pendaftaran berhasil diproses.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String nilaiAtauStrip(String nilai) {
        return nilai.isEmpty() ? "-" : nilai;
    }

    private void btnResetActionPerformed() {
        int jawab = JOptionPane.showConfirmDialog(
            this,
            "Kosongkan seluruh input?",
            "Konfirmasi Reset",
            JOptionPane.YES_NO_OPTION);

        if (jawab == JOptionPane.YES_OPTION) {
            resetForm();
        }
    }

    private void resetForm() {
        txtNim.setText("");
        txtNama.setText("");
        groupGender.clearSelection();
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtKodeAkses.setText("");
        groupJenis.clearSelection();
        cmbKursus.setSelectedIndex(0);
        cbModul.setSelected(false);
        cbSertifikat.setSelected(false);
        cbRekaman.setSelected(false);
        txtRingkasan.setText("");
        txtNim.requestFocus();
    }

    private String formatRupiah(double nilai) {
        String angka = String.format(Locale.US, "%,.0f", nilai).replace(',', '.');
        return "Rp" + angka;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranKursus.class.getName())
                .log(java.util.logging.Level.WARNING,
                    "Nimbus tidak dapat diaktifkan", ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormPendaftaranKursus().setVisible(true);
            }
        });
    }
}
