package model;

// Tetap pertahankan warisan dari class Orang agar class lain tidak error
public class Peserta extends Orang {
    private String nim;
    private String prodi;
    private String jenisKelamin; // Tambahan baru
    private String email;        // Tambahan baru

    // CONSTRUCTOR 1: Untuk mempertahankan kode lama Anda agar tidak error
    public Peserta(int id, String nama, String noHp, String nim, String prodi) {
        super(id, nama, noHp);
        setNim(nim);
        setProdi(prodi);
    }

    // CONSTRUCTOR 2: Untuk kebutuhan fitur database baru (XAMPP)
    public Peserta(int id, String nama, String noHp, String nim, String jenisKelamin, String prodi, String email) {
        super(id, nama, noHp);
        setNim(nim);
        setProdi(prodi);
        this.jenisKelamin = jenisKelamin;
        this.email = email;
    }

    // Getter & Setter untuk NIM dan Prodi (dengan Validasi lama Anda)
    public String getNim() { return nim; }
    public final void setNim(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIM wajib diisi");
        }
        this.nim = nim.trim();
    }

    public String getProdi() { return prodi; }
    public final void setProdi(String prodi) {
        if (prodi == null || prodi.trim().isEmpty()) {
            throw new IllegalArgumentException("Prodi wajib diisi");
        }
        this.prodi = prodi.trim();
    }

    // Getter & Setter Tambahan Baru
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getInfo() {
        return "PESERTA | " + super.getInfo()
                + " | NIM: " + getNim()
                + " | Prodi: " + getProdi();
    }
}
