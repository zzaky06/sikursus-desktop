package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

    // Palet Biru Langit Elegan
    private final Color biruGelap = new Color(15, 23, 42);      // Slate Gelap Profesional
    private final Color biruLangit = new Color(2, 132, 199);    // Biru Langit Utama
    private final Color biruTerang = new Color(224, 242, 254);  // Biru Sangat Muda (Background Aksen)
    private final Color latar = new Color(248, 250, 252);       // Background Utama Lembut
    private final Color putih = Color.WHITE;
    private final Color teks = new Color(30, 41, 59);           // Teks Utama
    private final Color teksSekunder = new Color(100, 116, 139);// Teks Sekunder / Footer
    private final Color garis = new Color(203, 213, 225);       // Border / Garis Pembatas

    private JPanel panelUtama;
    private JPanel panelHeader;
    private JPanel panelKonten;
    private JPanel panelForm;
    private JPanel panelRingkasan;
    private JPanel panelJenis;
    private JPanel panelFasilitas;
    private JPanel panelTombol;

    private JLabel lblJudul;
    private JLabel lblSubjudul;
    private JLabel lblNama;
    private JLabel lblKodeAkses;
    private JLabel lblJenis;
    private JLabel lblKursus;
    private JLabel lblFasilitas;
    private JLabel lblRingkasan;

    private JTextField txtNama;
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

        txtRingkasan.setEditable(false);
        txtRingkasan.setLineWrap(true);
        txtRingkasan.setWrapStyleWord(true);
        getRootPane().setDefaultButton(btnProses);

        setMinimumSize(new Dimension(850, 620));
        setSize(new Dimension(1050, 720));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        panelUtama = new JPanel(new BorderLayout(0, 0));
        panelHeader = new JPanel(new BorderLayout());
        panelKonten = new JPanel(new GridBagLayout());
        panelForm = new JPanel(new GridBagLayout());
        panelRingkasan = new JPanel(new BorderLayout(0, 12));
        panelJenis = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        panelFasilitas = new JPanel(new GridBagLayout());
        panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));

        lblJudul = new JLabel("Pendaftaran Kursus");
        lblSubjudul = new JLabel("SIKURSUS  •  Kembangkan keterampilan, raih peluang baru");
        lblNama = new JLabel("Nama Peserta");
        lblKodeAkses = new JLabel("Kode Akses");
        lblJenis = new JLabel("Jenis Peserta");
        lblKursus = new JLabel("Pilih Kursus");
        lblFasilitas = new JLabel("Fasilitas Tambahan");
        lblRingkasan = new JLabel("Ringkasan Pendaftaran");

        txtNama = new JTextField();
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
        panelForm.setBackground(putih);
        panelRingkasan.setBackground(putih);
        panelJenis.setBackground(putih);
        panelFasilitas.setBackground(putih);
        panelTombol.setBackground(putih);

        panelHeader.setBorder(new EmptyBorder(24, 32, 24, 32));
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblJudul.setForeground(putih);
        lblSubjudul.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubjudul.setForeground(new Color(186, 230, 253));

        JPanel teksHeader = new JPanel(new GridBagLayout());
        teksHeader.setOpaque(false);
        GridBagConstraints h = new GridBagConstraints();
        h.gridx = 0;
        h.gridy = 0;
        h.anchor = GridBagConstraints.WEST;
        h.insets = new Insets(0, 0, 5, 0);
        teksHeader.add(lblJudul, h);
        h.gridy = 1;
        h.insets = new Insets(0, 0, 0, 0);
        teksHeader.add(lblSubjudul, h);
        panelHeader.add(teksHeader, BorderLayout.WEST);

        JLabel logo = new JLabel("S");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(putih);
        logo.setOpaque(true);
        logo.setBackground(biruLangit);
        logo.setPreferredSize(new Dimension(54, 54));
        logo.setBorder(new LineBorder(new Color(56, 189, 248), 1, true));
        panelHeader.add(logo, BorderLayout.EAST);

        panelKonten.setBorder(new EmptyBorder(24, 28, 26, 28));
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(garis, 1, true), new EmptyBorder(22, 22, 20, 22)));
        panelRingkasan.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(garis, 1, true), new EmptyBorder(22, 22, 20, 22)));

        styleLabel(lblNama);
        styleLabel(lblKodeAkses);
        styleLabel(lblJenis);
        styleLabel(lblKursus);
        styleLabel(lblFasilitas);
        styleLabel(lblRingkasan);
        lblRingkasan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRingkasan.setForeground(biruGelap);

        styleField(txtNama);
        styleField(txtKodeAkses);
        styleCombo(cmbKursus);
        txtKodeAkses.setToolTipText("Kode akses demo praktikum: PBO2026");

        rbMahasiswa.setBackground(putih);
        rbUmum.setBackground(putih);
        rbMahasiswa.setForeground(teks);
        rbUmum.setForeground(teks);
        rbMahasiswa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbUmum.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbMahasiswa.setFocusPainted(false);
        rbUmum.setFocusPainted(false);
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
        txtRingkasan.setBackground(new Color(241, 245, 249));
        txtRingkasan.setBorder(new EmptyBorder(14, 14, 14, 14));
        txtRingkasan.setCaretColor(biruLangit);
        scrollRingkasan.setBorder(new LineBorder(garis, 1, true));
        scrollRingkasan.getViewport().setBackground(new Color(241, 245, 249));

        btnProses.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnProses.setBackground(biruLangit);
        btnProses.setForeground(putih);
        btnProses.setFocusPainted(false);
        btnProses.setBorderPainted(false);
        btnProses.setOpaque(true);
        btnProses.setPreferredSize(new Dimension(190, 42));
        btnProses.addActionListener(evt -> btnProsesActionPerformed());

        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.setBackground(biruTerang);
        btnReset.setForeground(biruGelap);
        btnReset.setFocusPainted(false);
        btnReset.setBorderPainted(false);
        btnReset.setOpaque(true);
        btnReset.setPreferredSize(new Dimension(125, 42));
        btnReset.addActionListener(evt -> btnResetActionPerformed());

        panelTombol.add(btnProses);
        panelTombol.add(btnReset);

        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.weightx = 1.0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.anchor = GridBagConstraints.NORTHWEST;
        f.insets = new Insets(0, 0, 7, 0);

        f.gridy = 0;
        panelForm.add(lblNama, f);
        f.gridy = 1;
        f.insets = new Insets(0, 0, 17, 0);
        panelForm.add(txtNama, f);

        f.gridy = 2;
        f.insets = new Insets(0, 0, 7, 0);
        panelForm.add(lblKodeAkses, f);
        f.gridy = 3;
        f.insets = new Insets(0, 0, 17, 0);
        panelForm.add(txtKodeAkses, f);

        f.gridy = 4;
        f.insets = new Insets(0, 0, 7, 0);
        panelForm.add(lblJenis, f);
        f.gridy = 5;
        f.insets = new Insets(0, 0, 17, 0);
        panelForm.add(panelJenis, f);

        f.gridy = 6;
        f.insets = new Insets(0, 0, 7, 0);
        panelForm.add(lblKursus, f);
        f.gridy = 7;
        f.insets = new Insets(0, 0, 17, 0);
        panelForm.add(cmbKursus, f);

        f.gridy = 8;
        f.insets = new Insets(0, 0, 7, 0);
        panelForm.add(lblFasilitas, f);
        f.gridy = 9;
        f.insets = new Insets(0, 0, 20, 0);
        panelForm.add(panelFasilitas, f);

        f.gridy = 10;
        f.insets = new Insets(0, 0, 0, 0);
        panelForm.add(panelTombol, f);

        panelRingkasan.add(lblRingkasan, BorderLayout.NORTH);
        panelRingkasan.add(scrollRingkasan, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 16);
        c.gridx = 0;
        c.weightx = 0.95;
        panelKonten.add(panelForm, c);

        c.gridx = 1;
        c.weightx = 1.05;
        c.insets = new Insets(0, 0, 0, 0);
        panelKonten.add(panelRingkasan, c);

        JLabel footer = new JLabel("  SIKURSUS  |  Form pendaftaran kursus desktop");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(teksSekunder);
        footer.setBorder(new EmptyBorder(0, 30, 12, 0));

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelKonten, BorderLayout.CENTER);
        panelUtama.add(footer, BorderLayout.SOUTH);
        setContentPane(panelUtama);
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(teks);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(teks);
        field.setBackground(putih);
        field.setPreferredSize(new Dimension(300, 39));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(garis, 1, true), new EmptyBorder(7, 10, 7, 10)));
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(teks);
        combo.setBackground(putih);
        combo.setPreferredSize(new Dimension(300, 39));
        combo.setBorder(new LineBorder(garis, 1, true));
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setForeground(teks);
        checkBox.setBackground(putih);
        checkBox.setFocusPainted(false);
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
        String nama = txtNama.getText().trim();
        String kode = new String(txtKodeAkses.getPassword());

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama peserta wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
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

        String nama = txtNama.getText().trim();
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
                + "Nama Peserta    : " + nama + "\n"
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
                "Pendaftaran berhasil diproses.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
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
        txtNama.setText("");
        txtKodeAkses.setText("");
        groupJenis.clearSelection();
        cmbKursus.setSelectedIndex(0);
        cbModul.setSelected(false);
        cbSertifikat.setSelected(false);
        cbRekaman.setSelected(false);
        txtRingkasan.setText("");
        txtNama.requestFocus();
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
                    .log(java.util.logging.Level.WARNING, "Nimbus tidak dapat diaktifkan", ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormPendaftaranKursus().setVisible(true);
            }
        });
    }
}