
package report;

import config.Database;

import java.awt.Component;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public final class ReportService {

    private ReportService() {
    }

    // ==========================================
    // 1. LAPORAN PESERTA
    // ==========================================

    public static void showPeserta(Component parent) {
        show(
            parent,
            "laporan_peserta",
            new HashMap<String, Object>()
        );
    }

    // ==========================================
    // 2. LAPORAN PENDAFTARAN SEMUA
    // ==========================================

    public static void showPendaftaranSemua(Component parent) {
        show(
            parent,
            "laporan_pendaftaran_semua",
            new HashMap<String, Object>()
        );
    }

    // ==========================================
    // 3. LAPORAN PENDAFTARAN BERDASARKAN ID
    // ==========================================

    public static void showPendaftaranById(
            Component parent, int idPendaftaran) {

        Map<String, Object> params = new HashMap<>();

        params.put("P_ID_PENDAFTARAN", idPendaftaran);

        show(
            parent,
            "laporan_pendaftaran",
            params
        );
    }

    public static void cetakPendaftaran(int idPendaftaran) {
        showPendaftaranById(null, idPendaftaran);
    }

    // ==========================================
    // 4. LAPORAN PEMBAYARAN SEMUA
    // ==========================================

    public static void showPembayaranSemua(Component parent) {
        show(
            parent,
            "laporan_pembayaran_semua",
            new HashMap<String, Object>()
        );
    }

    // ==========================================
    // 5. LAPORAN PEMBAYARAN TERPILIH
    // ==========================================

    public static void showPembayaranById(
            Component parent, int idPembayaran) {

        Map<String, Object> params = new HashMap<>();

        params.put("P_ID_PEMBAYARAN", idPembayaran);

        show(
            parent,
            "laporan_pembayaran_terpilih",
            params
        );
    }

    // ==========================================
    // 6. LAPORAN PEMBAYARAN BERDASARKAN PERIODE
    // ==========================================

    public static void showPembayaranPeriode(
            Component parent,
            Date awal,
            Date akhir) {

        if (awal == null || akhir == null) {

            JOptionPane.showMessageDialog(
                parent,
                "Tanggal awal dan akhir wajib diisi.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (awal.after(akhir)) {

            JOptionPane.showMessageDialog(
                parent,
                "Tanggal awal tidak boleh melebihi tanggal akhir.",
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Map<String, Object> params = new HashMap<>();

        params.put("P_TANGGAL_AWAL", awal);
        params.put("P_TANGGAL_AKHIR", akhir);

        show(
            parent,
            "laporan_pembayaran",
            params
        );
    }

    // ==========================================
    // 7. PROSES MENAMPILKAN LAPORAN
    // ==========================================

    private static void show(
            Component parent,
            String baseName,
            Map<String, Object> params) {

        try (Connection conn = Database.getConnection()) {

            JasperReport report = loadReport(baseName);

            JasperPrint print = JasperFillManager.fillReport(
                report,
                params,
                conn
            );

            if (print.getPages().isEmpty()) {

                JOptionPane.showMessageDialog(
                    parent,
                    "Report tidak memiliki data untuk kriteria yang dipilih.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            JasperViewer viewer = new JasperViewer(print, false);

            viewer.setTitle("Laporan SIKURSUS - " + baseName);
            viewer.setVisible(true);

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                parent,
                "Gagal membuka laporan " + baseName
                    + ":\n" + ex.getMessage(),
                "JasperReports",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (LinkageError err) {

            err.printStackTrace();

            JOptionPane.showMessageDialog(
                parent,
                "Terjadi kesalahan saat memuat class JasperReports:\n"
                    + err.getMessage()
                    + "\n\nSilakan lakukan Clean and Build.",
                "JasperReports Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // 8. LOAD DAN COMPILE JRXML
    // ==========================================

    private static JasperReport loadReport(
            String baseName) throws Exception {

        String source = "/report/" + baseName + ".jrxml";

        InputStream in =
            ReportService.class.getResourceAsStream(source);

        if (in == null) {

            throw new IllegalStateException(
                "Resource report tidak ditemukan: " + source
                + "\nPastikan file berada di src/main/resources/report/"
            );
        }

        try (InputStream reportStream = in) {

            return JasperCompileManager.compileReport(
                reportStream
            );
        }
    }

    // ==========================================
    // 9. EXPORT LAPORAN KE PDF
    // ==========================================

    public static void exportPdf(
            Component parent,
            JasperPrint print,
            String defaultName) {

        if (print == null) {

            JOptionPane.showMessageDialog(
                parent,
                "Tidak ada laporan untuk diekspor.",
                "Informasi",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setSelectedFile(
            new java.io.File(defaultName)
        );

        if (chooser.showSaveDialog(parent)
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        try {

            String path =
                chooser.getSelectedFile().getAbsolutePath();

            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }

            JasperExportManager.exportReportToPdfFile(
                print,
                path
            );

            JOptionPane.showMessageDialog(
                parent,
                "PDF berhasil disimpan:\n" + path,
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                parent,
                "Export PDF gagal:\n" + ex.getMessage(),
                "JasperReports",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
