package dao;

// 4.1 Import yang diperlukan (Lengkap)
import config.Database;
import model.Peserta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PesertaDAO {

    // Method Insert (Punya Anda)
    public boolean insert(Peserta p) throws SQLException {
        String sql = "INSERT INTO peserta "
                + "(nim, nama, jenis_kelamin, prodi, no_hp, email) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNim());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenisKelamin());
            ps.setString(4, p.getProdi());
            ps.setString(5, p.getNoHp());
            ps.setString(6, p.getEmail());
            return ps.executeUpdate() == 1;
        }
    }

public boolean update(Peserta p) throws SQLException {

    String sql = "UPDATE peserta SET nim=?, nama=?, "
            + "jenis_kelamin=?, prodi=?, no_hp=?, email=? "
            + "WHERE id_peserta=?";

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, p.getNim());
        ps.setString(2, p.getNama());
        ps.setString(3, p.getJenisKelamin());
        ps.setString(4, p.getProdi());
        ps.setString(5, p.getNoHp());
        ps.setString(6, p.getEmail());
        ps.setInt(7, p.getId());

        return ps.executeUpdate() == 1;
    }
}

public boolean delete(int id) throws SQLException {

    String sql = "DELETE FROM peserta WHERE id_peserta=?";

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);

        return ps.executeUpdate() == 1;
    }
}



    // 4.2 Helper mapRow() dari Dosen
    // Berfungsi mengubah satu baris ResultSet data database menjadi objek Peserta
    private Peserta mapRow(ResultSet rs) throws SQLException {
        Peserta p = new Peserta();
        p.setId(rs.getInt("id_peserta"));
        p.setNim(rs.getString("nim"));
        p.setNama(rs.getString("nama"));
        p.setJenisKelamin(rs.getString("jenis_kelamin"));
        p.setProdi(rs.getString("prodi"));
        p.setNoHp(rs.getString("no_hp"));
        p.setEmail(rs.getString("email"));
        return p;
    }

    // 4.3 Implementasikan findAll() dari Dosen
    // Method untuk READ/mengambil semua data dari database
    public List<Peserta> findAll() throws SQLException {
        List<Peserta> data = new ArrayList<>();
        String sql = "SELECT id_peserta, nim, nama, jenis_kelamin, "
                + "prodi, no_hp, email "
                + "FROM peserta ORDER BY id_peserta DESC";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.add(mapRow(rs));
            }
        }
        return data;
    }
}
