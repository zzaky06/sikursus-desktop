package dao; // Tambahkan ini di baris nomor 1 paling atas file MainTest.java

import dao.PesertaDAO; // Bagian ini bisa dihapus karena sudah satu package
import model.Peserta;
import java.sql.SQLException;
import java.util.List;

public class MainTest {
    // ... sisa kode lainnya tetap sama
    public static void main(String[] args) {
        System.out.println("=== Memulai Uji Coba PesertaDAO ===");
        
        PesertaDAO dao = new PesertaDAO();
        
        try {
            // 1. Uji Coba INSERT (Opsional - aktifkan jika ingin tes tambah data)
            /*
            System.out.println("\n[1] Mencoba Insert Data Baru...");
            Peserta mhsBaru = new Peserta();
            mhsBaru.setNim("2200101");
            mhsBaru.setNama("Zaky Alfadlani");
            mhsBaru.setJenisKelamin("Laki-laki");
            mhsBaru.setProdi("Teknik Informatika");
            mhsBaru.setNoHp("081234567890");
            mhsBaru.setEmail("zaky@email.com");
            
            boolean isSukses = dao.insert(mhsBaru);
            System.out.println("Status Insert: " + (isSukses ? "BERHASIL" : "GAGAL"));
            */

            // 2. Uji Coba FIND ALL (Mengambil semua data)
            System.out.println("\n[2] Mengambil semua data dari database...");
            List<Peserta> listPeserta = dao.findAll();
            
            if (listPeserta.isEmpty()) {
                System.out.println("Database kosong atau belum ada data peserta.");
            } else {
                System.out.println("Data ditemukan (Urutan Terbaru):");
                for (Peserta p : listPeserta) {
                    System.out.println("ID: " + p.getId() 
                            + " | NIM: " + p.getNim() 
                            + " | Nama: " + p.getNama()
                            + " | Prodi: " + p.getProdi());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("\n[ERROR] Terjadi masalah pada database/koneksi!");
            System.err.println("Pesan Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Uji Coba Selesai ===");
    }
}
