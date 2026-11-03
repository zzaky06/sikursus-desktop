package view;

// Import yang diperlukan untuk komponen GUI, DAO, Model, dan Database
import dao.PesertaDAO;
import model.Peserta;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PesertaFrame extends javax.swing.JFrame {

    // 5.3 Membuat object DAO pada form (Sesuai panduan dosen)
    private final PesertaDAO dao = new PesertaDAO();
    private int selectedId = -1;

    // Deklarasi Komponen GUI Mandiri secara Manual (Tanpa Tab Design)
    private javax.swing.JTable tblPeserta;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtNim, txtNama, txtProdi, txtNoHp, txtEmail;
    private javax.swing.JRadioButton rbLaki, rbPerempuan;
    private javax.swing.ButtonGroup btnGroupJK;
    private javax.swing.JButton btnUbah, btnHapus, btnSimpan;

    // 5.5 Constructor PesertaFrame
    public PesertaFrame() {
        initComponentsManual(); // Inisialisasi susunan komponen dasar
        configureTable();      // 5.2 Konfigurasi JTable agar non-editable
        loadTable();           // 5.4 Memuat data awal dari MySQL ke JTable
        clearForm();           // Membersihkan form inputan awal
    }

    // Inisialisasi susunan tata letak komponen secara manual lewat kode
    private void initComponentsManual() {
        // Pengaturan properti jendela Frame
        setTitle("SIKURSUS Desktop - PesertaFrame");
        setSize(850, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Menggunakan Absolute Layout agar mudah mengatur letak koordinat

        // Inisialisasi Form Input Teks Kontrol
        txtNim = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtProdi = new javax.swing.JTextField();
        txtNoHp = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        
        rbLaki = new javax.swing.JRadioButton("L");
        rbPerempuan = new javax.swing.JRadioButton("P");
        btnGroupJK = new javax.swing.ButtonGroup();
        btnGroupJK.add(rbLaki);
        btnGroupJK.add(rbPerempuan);

        btnSimpan = new javax.swing.JButton("Simpan");
        btnUbah = new javax.swing.JButton("Ubah");
        btnHapus = new javax.swing.JButton("Hapus");

        // --- 5.1 Menyiapkan komponen JTable & JScrollPane ---
        tblPeserta = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setViewportView(tblPeserta);

        // Menentukan posisi JTable (X, Y, Lebar, Tinggi) agar berada di bagian bawah form
        jScrollPane1.setBounds(20, 250, 790, 250);
        add(jScrollPane1);

        // --- Event Listener klik Baris ---
        // 5.6 Menambahkan event klik baris JTable secara manual lewat kode
        tblPeserta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesertaMouseClicked(evt);
            }
        });
    }

    // 5.2 Konfigurasi model tabel (Sesuai instruksi dosen)
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "NIM", "Nama", "JK", "Prodi", "No. HP", "Email"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Mengunci tabel agar cell data tidak bisa diketik langsung
            }
        };
        tblPeserta.setModel(model);
        tblPeserta.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }

    // 5.4 Mengubah List<Peserta> menjadi baris/row di JTable
    private void fillTable(List<Peserta> data) {
        DefaultTableModel model = (DefaultTableModel) tblPeserta.getModel();
        model.setRowCount(0); // Mencegah data terlihat duplikat/menumpuk saat direfresh
        for (Peserta p : data) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNim(),
                p.getNama(),
                p.getJenisKelamin(),
                p.getProdi(),
                p.getNoHp(),
                p.getEmail()
            });
        }
    }

    // 5.4 Memanggil fungsi findAll dari objek DAO
    private void loadTable() {
        try {
            fillTable(dao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // 5.6 Fungsi Event Klik Baris JTable (Data pindah ke form input)
    private void tblPesertaMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tblPeserta.getSelectedRow();
        if (row == -1) {
            return;
        }
        
        // Mengambil id peserta dari kolom 0
        selectedId = Integer.parseInt(tblPeserta.getValueAt(row, 0).toString());
        
        // Memindahkan data sel tabel kembali ke dalam form teks inputan Anda
        txtNim.setText(tblPeserta.getValueAt(row, 1).toString());
        txtNama.setText(tblPeserta.getValueAt(row, 2).toString());
        
        String jk = tblPeserta.getValueAt(row, 3).toString();
        rbLaki.setSelected("L".equals(jk));
        rbPerempuan.setSelected("P".equals(jk));
        
        txtProdi.setText(tblPeserta.getValueAt(row, 4).toString());
        txtNoHp.setText(tblPeserta.getValueAt(row, 5).toString());
        txtEmail.setText(tblPeserta.getValueAt(row, 6).toString());
        
        // Mengaktifkan tombol aksi ubah dan hapus setelah baris dipilih
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    // Helper Pembersih nilai form inputan
    private void clearForm() {
        txtNim.setText("");
        txtNama.setText("");
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        btnGroupJK.clearSelection();
        selectedId = -1;
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    // Method main untuk menjalankan aplikasi secara langsung
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new PesertaFrame().setVisible(true);
        });
    }
}
