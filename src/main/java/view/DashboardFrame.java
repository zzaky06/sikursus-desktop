
package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.User;
import report.ReportService;
import util.UserSession;
import util.WindowManager;

public class DashboardFrame extends JFrame {

    // ================= USER SESSION =================

    private final JLabel lblUser = new JLabel();
    private final JLabel lblRole = new JLabel();

    // ================= COLOR PALETTE =================

    private final Color NAVY = new Color(15, 35, 70);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color BG = new Color(241, 245, 249);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);

    private final Color LIGHT_BLUE = new Color(219, 234, 254);
    private final Color SOFT_BG = new Color(248, 250, 252);

    // ================= NAVIGATION BUTTONS =================

    private final JButton btnUsers = new JButton();
    private final JButton btnPeserta = new JButton();
    private final JButton btnInstruktur = new JButton();
    private final JButton btnKursus = new JButton();
    private final JButton btnKelas = new JButton();

    // Popup hanya ditampilkan satu kali per instance dashboard.
    private boolean welcomeShown = false;

    // ================= CONSTRUCTOR =================

    public DashboardFrame() {

        super("SIKURSUS | Dashboard");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1150, 720);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        build();
        applySession();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {

                if (!welcomeShown
                    && UserSession.getCurrentUser() != null) {

                    welcomeShown = true;

                    SwingUtilities.invokeLater(
                        () -> showWelcomePopup()
                    );
                }
            }
        });
    }

    // ================= MAIN BUILD =================

    private void build() {

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(BG);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ==================================================
    // SIDEBAR
    // ==================================================

    private JPanel buildSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setLayout(
            new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBackground(NAVY);

        sidebar.setPreferredSize(
            new Dimension(235, 720)
        );

        sidebar.setBorder(
            new EmptyBorder(28, 18, 22, 18)
        );

        // BRAND

        JLabel brand = new JLabel("SIKURSUS");

        brand.setFont(
            new Font("Segoe UI", Font.BOLD, 27)
        );

        brand.setForeground(WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandSub = new JLabel(
            "COURSE MANAGEMENT"
        );

        brandSub.setFont(
            new Font("Segoe UI", Font.PLAIN, 10)
        );

        brandSub.setForeground(
            new Color(147, 197, 253)
        );

        brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(brandSub);
        sidebar.add(Box.createVerticalStrut(45));

        // MENU UTAMA

        addSidebarTitle(sidebar, "MENU UTAMA");

        JButton dashboard = createNavButton("Dashboard");

        sidebar.add(dashboard);

        sidebar.add(Box.createVerticalStrut(25));

        // MASTER DATA

        addSidebarTitle(sidebar, "MASTER DATA");

        btnUsers.setText("Users");
        btnPeserta.setText("Peserta");
        btnInstruktur.setText("Instruktur");
        btnKursus.setText("Kursus");
        btnKelas.setText("Kelas");

        sidebar.add(createNavButton("Users", btnUsers));
        sidebar.add(createNavButton("Peserta", btnPeserta));
        sidebar.add(createNavButton("Instruktur", btnInstruktur));
        sidebar.add(createNavButton("Kursus", btnKursus));
        sidebar.add(createNavButton("Kelas", btnKelas));

        sidebar.add(Box.createVerticalStrut(25));

        // TRANSAKSI

        addSidebarTitle(sidebar, "TRANSAKSI");

        JButton pendaftaran = createNavButton("Pendaftaran");
        JButton pembayaran = createNavButton("Pembayaran");

        sidebar.add(pendaftaran);
        sidebar.add(pembayaran);

        sidebar.add(Box.createVerticalStrut(25));

        // LAPORAN

        addSidebarTitle(sidebar, "LAPORAN");

        JButton laporanPeserta =
            createNavButton("Laporan Peserta");

        JButton laporanPendaftaran =
            createNavButton("Laporan Pendaftaran");

        JButton laporanPembayaran =
            createNavButton("Laporan Pembayaran");

        JButton laporanPeriode =
            createNavButton("Laporan Periode");

        sidebar.add(laporanPeserta);
        sidebar.add(laporanPendaftaran);
        sidebar.add(laporanPembayaran);
        sidebar.add(laporanPeriode);

        sidebar.add(Box.createVerticalGlue());

        // LOGOUT

        JButton logout = createNavButton("Logout");

        sidebar.add(logout);

        JLabel footer = new JLabel(
            "SIKURSUS Desktop v1.0"
        );

        footer.setFont(
            new Font("Segoe UI", Font.PLAIN, 10)
        );

        footer.setForeground(
            new Color(148, 163, 184)
        );

        footer.setBorder(
            new EmptyBorder(15, 5, 0, 0)
        );

        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(footer);

        // ================= NAVIGATION ACTIONS =================

        dashboard.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                this,
                "Anda sedang berada di Dashboard."
            );
        });

        btnUsers.addActionListener(e ->
            WindowManager.open(
                UserFrame.class,
                UserFrame::new
            )
        );

        btnPeserta.addActionListener(e ->
            WindowManager.open(
                PesertaFrame.class,
                PesertaFrame::new
            )
        );

        btnInstruktur.addActionListener(e ->
            WindowManager.open(
                InstrukturFrame.class,
                InstrukturFrame::new
            )
        );

        btnKursus.addActionListener(e ->
            WindowManager.open(
                KursusFrame.class,
                KursusFrame::new
            )
        );

        btnKelas.addActionListener(e ->
            WindowManager.open(
                KelasFrame.class,
                KelasFrame::new
            )
        );

        pendaftaran.addActionListener(e ->
            WindowManager.open(
                PendaftaranFrame.class,
                PendaftaranFrame::new
            )
        );

        pembayaran.addActionListener(e ->
            WindowManager.open(
                PembayaranFrame.class,
                PembayaranFrame::new
            )
        );

        laporanPeserta.addActionListener(e ->
            ReportService.showPeserta(this)
        );

        laporanPendaftaran.addActionListener(e ->
            ReportService.showPendaftaranSemua(this)
        );

        laporanPembayaran.addActionListener(e ->
            ReportService.showPembayaranSemua(this)
        );

        laporanPeriode.addActionListener(e ->
            showPaymentPeriod()
        );

        logout.addActionListener(e -> logout());

        return sidebar;
    }

    // ==================================================
    // MAIN CONTENT
    // ==================================================

    private JPanel buildMainContent() {

        JPanel main = new JPanel(
            new BorderLayout(0, 22)
        );

        main.setBackground(BG);

        main.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        // HEADER

        JPanel header = new JPanel(
            new BorderLayout()
        );

        header.setBackground(WHITE);

        header.setBorder(
            new EmptyBorder(18, 22, 18, 22)
        );

        JPanel headerLeft = new JPanel();

        headerLeft.setLayout(
            new BoxLayout(headerLeft, BoxLayout.Y_AXIS)
        );

        headerLeft.setBackground(WHITE);

        JLabel title = new JLabel("Dashboard");

        title.setFont(
            new Font("Segoe UI", Font.BOLD, 25)
        );

        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Selamat datang di sistem informasi kursus."
        );

        subtitle.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        subtitle.setForeground(MUTED);

        headerLeft.add(title);
        headerLeft.add(Box.createVerticalStrut(5));
        headerLeft.add(subtitle);

        // PROFILE

        JPanel profile = new JPanel();

        profile.setLayout(
            new BoxLayout(profile, BoxLayout.Y_AXIS)
        );

        profile.setBackground(WHITE);

        lblUser.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        lblUser.setForeground(TEXT);
        lblUser.setAlignmentX(Component.RIGHT_ALIGNMENT);

        lblRole.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        lblRole.setForeground(MUTED);
        lblRole.setAlignmentX(Component.RIGHT_ALIGNMENT);

        profile.add(lblUser);
        profile.add(Box.createVerticalStrut(4));
        profile.add(lblRole);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(profile, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // DASHBOARD BODY

        JPanel content = new JPanel();

        content.setLayout(
            new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        content.setBackground(BG);

        // WELCOME CARD

        JPanel welcome = new JPanel();

        welcome.setLayout(
            new BoxLayout(welcome, BoxLayout.Y_AXIS)
        );

        welcome.setBackground(BLUE);

        welcome.setBorder(
            new EmptyBorder(28, 30, 28, 30)
        );

        welcome.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 165)
        );

        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel(
            "Kelola Kursus Lebih Mudah!"
        );

        welcomeTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 24)
        );

        welcomeTitle.setForeground(WHITE);

        JLabel welcomeText = new JLabel(
            "Semua kebutuhan pengelolaan kursus tersedia dalam satu aplikasi."
        );

        welcomeText.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        welcomeText.setForeground(
            new Color(219, 234, 254)
        );

        welcome.add(welcomeTitle);
        welcome.add(Box.createVerticalStrut(12));
        welcome.add(welcomeText);

        content.add(welcome);
        content.add(Box.createVerticalStrut(25));

        // SECTION TITLE

        JLabel sectionTitle = new JLabel("Akses Cepat");

        sectionTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 19)
        );

        sectionTitle.setForeground(TEXT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(15));

        // QUICK ACCESS CARDS

        JPanel cards = new JPanel(
            new GridLayout(2, 2, 15, 15)
        );

        cards.setBackground(BG);

        cards.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 230)
        );

        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        cards.add(createCard(
            "MASTER DATA",
            "Kelola pengguna, peserta,\ninstruktur, kursus dan kelas.",
            "01",
            new Color(37, 99, 235)
        ));

        cards.add(createCard(
            "TRANSAKSI",
            "Kelola pendaftaran dan\npembayaran peserta kursus.",
            "02",
            new Color(5, 150, 105)
        ));

        cards.add(createCard(
            "LAPORAN",
            "Lihat dan cetak laporan\ndata sistem kursus.",
            "03",
            new Color(124, 58, 237)
        ));

        cards.add(createCard(
            "SISTEM INFORMASI",
            "View - DAO - JDBC - MySQL",
            "04",
            new Color(234, 88, 12)
        ));

        content.add(cards);
        content.add(Box.createVerticalStrut(25));

        // SYSTEM INFORMATION

        JPanel info = new JPanel();

        info.setLayout(
            new BoxLayout(info, BoxLayout.Y_AXIS)
        );

        info.setBackground(WHITE);

        info.setBorder(
            new EmptyBorder(20, 22, 20, 22)
        );

        info.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 125)
        );

        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoTitle = new JLabel("Informasi Sistem");

        infoTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 16)
        );

        infoTitle.setForeground(TEXT);

        JLabel database = new JLabel(
            "Database : db_sikursus"
        );

        JLabel architecture = new JLabel(
            "Arsitektur : View -> DAO -> JDBC -> MySQL"
        );

        JLabel tips = new JLabel(
            "Tips : Selesaikan master data sebelum menguji transaksi."
        );

        for (JLabel label : new JLabel[]{
            database, architecture, tips
        }) {

            label.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
            );

            label.setForeground(MUTED);
        }

        info.add(infoTitle);
        info.add(Box.createVerticalStrut(10));
        info.add(database);
        info.add(architecture);
        info.add(tips);

        content.add(info);

        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // ==================================================
    // SIDEBAR HELPERS
    // ==================================================

    private void addSidebarTitle(
        JPanel panel,
        String text
    ) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Segoe UI", Font.BOLD, 10)
        );

        label.setForeground(
            new Color(148, 163, 184)
        );

        label.setBorder(
            new EmptyBorder(0, 5, 10, 0)
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
    }

    private JButton createNavButton(String text) {

        JButton button = new JButton(text);

        styleNavButton(button);

        return button;
    }

    private JPanel createNavButton(
        String text,
        JButton button
    ) {

        styleNavButton(button);

        return wrapNavButton(button);
    }

    private void styleNavButton(JButton button) {

        button.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        button.setForeground(
            new Color(226, 232, 240)
        );

        button.setBackground(NAVY);

        button.setHorizontalAlignment(
            SwingConstants.LEFT
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBorder(
            new EmptyBorder(8, 12, 8, 8)
        );

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                if (button.isEnabled()) {
                    button.setBackground(
                        new Color(37, 64, 105)
                    );
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(NAVY);
            }
        });
    }

    private JPanel wrapNavButton(JButton button) {

        JPanel wrapper = new JPanel(
            new BorderLayout()
        );

        wrapper.setBackground(NAVY);

        wrapper.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(button, BorderLayout.CENTER);

        return wrapper;
    }

    // ==================================================
    // DASHBOARD CARD
    // ==================================================

    private JPanel createCard(
        String title,
        String description,
        String number,
        Color accent
    ) {

        JPanel card = new JPanel(
            new BorderLayout(0, 8)
        );

        card.setBackground(WHITE);

        card.setBorder(
            new EmptyBorder(17, 20, 17, 20)
        );

        JLabel num = new JLabel(number);

        num.setFont(
            new Font("Segoe UI", Font.BOLD, 23)
        );

        num.setForeground(accent);

        JLabel name = new JLabel(title);

        name.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        name.setForeground(TEXT);

        JTextArea desc = new JTextArea(description);

        desc.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        desc.setForeground(MUTED);
        desc.setBackground(WHITE);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setOpaque(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        JPanel top = new JPanel(
            new BorderLayout()
        );

        top.setBackground(WHITE);

        top.add(name, BorderLayout.WEST);
        top.add(num, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);

        return card;
    }

    // ==================================================
    // WELCOME POPUP
    // ==================================================

    private void showWelcomePopup() {

        User u = UserSession.getCurrentUser();

        if (u == null) return;

        JDialog dialog = new JDialog(
            this,
            "Selamat Datang di SIKURSUS",
            true
        );

        dialog.setSize(700, 650);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(
            new BorderLayout()
        );

        root.setBackground(WHITE);

        root.setBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            )
        );

        // HEADER BANNER

        JPanel banner = new JPanel(
            new BorderLayout()
        );

        banner.setBackground(NAVY);

        banner.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        JPanel brand = new JPanel();

        brand.setLayout(
            new BoxLayout(brand, BoxLayout.Y_AXIS)
        );

        brand.setBackground(NAVY);

        JLabel logo = new JLabel("SIKURSUS");

        logo.setFont(
            new Font("Segoe UI", Font.BOLD, 28)
        );

        logo.setForeground(WHITE);

        JLabel brandSub = new JLabel(
            "COURSE MANAGEMENT SYSTEM"
        );

        brandSub.setFont(
            new Font("Segoe UI", Font.BOLD, 10)
        );

        brandSub.setForeground(
            new Color(147, 197, 253)
        );

        brand.add(logo);
        brand.add(Box.createVerticalStrut(5));
        brand.add(brandSub);

        JLabel version = new JLabel("DESKTOP v1.0");

        version.setFont(
            new Font("Segoe UI", Font.BOLD, 10)
        );

        version.setForeground(
            new Color(191, 219, 254)
        );

        banner.add(brand, BorderLayout.WEST);
        banner.add(version, BorderLayout.EAST);

        root.add(banner, BorderLayout.NORTH);

        // MAIN CONTENT

        JPanel content = new JPanel();

        content.setLayout(
            new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        content.setBackground(WHITE);

        content.setBorder(
            new EmptyBorder(24, 30, 18, 30)
        );

        // WELCOME TEXT

        JLabel welcome = new JLabel(
            "Selamat Datang, " + u.getUsername() + "!"
        );

        welcome.setFont(
            new Font("Segoe UI", Font.BOLD, 23)
        );

        welcome.setForeground(TEXT);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
            "Senang melihat Anda kembali. Siap mengelola kursus hari ini?"
        );

        description.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        description.setForeground(MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(welcome);
        content.add(Box.createVerticalStrut(7));
        content.add(description);
        content.add(Box.createVerticalStrut(18));

        // USER PROFILE CARD

        JPanel profile = new JPanel(
            new BorderLayout(12, 0)
        );

        profile.setBackground(SOFT_BG);

        profile.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(226, 232, 240)
                ),
                new EmptyBorder(13, 16, 13, 16)
            )
        );

        JLabel avatar = new JLabel("U");

        avatar.setFont(
            new Font("Segoe UI", Font.BOLD, 22)
        );

        avatar.setForeground(WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(BLUE);

        avatar.setPreferredSize(
            new Dimension(48, 48)
        );

        JPanel userInfo = new JPanel();

        userInfo.setLayout(
            new BoxLayout(userInfo, BoxLayout.Y_AXIS)
        );

        userInfo.setBackground(SOFT_BG);

        JLabel username = new JLabel(u.getUsername());

        username.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        username.setForeground(TEXT);

        JLabel role = new JLabel(
            "Hak akses: " + u.getRole()
        );

        role.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        role.setForeground(MUTED);

        userInfo.add(username);
        userInfo.add(Box.createVerticalStrut(5));
        userInfo.add(role);

        JLabel status = new JLabel("AKUN AKTIF");

        status.setFont(
            new Font("Segoe UI", Font.BOLD, 10)
        );

        status.setForeground(
            new Color(5, 150, 105)
        );

        profile.add(avatar, BorderLayout.WEST);
        profile.add(userInfo, BorderLayout.CENTER);
        profile.add(status, BorderLayout.EAST);

        profile.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 76)
        );

        profile.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(profile);
        content.add(Box.createVerticalStrut(22));

        // FEATURE TITLE

        JLabel featureTitle = new JLabel(
            "Apa yang bisa Anda lakukan?"
        );

        featureTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 15)
        );

        featureTitle.setForeground(TEXT);
        featureTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(featureTitle);
        content.add(Box.createVerticalStrut(12));

        // FEATURE GRID

        JPanel features = new JPanel(
            new GridLayout(2, 2, 12, 12)
        );

        features.setBackground(WHITE);
        features.setAlignmentX(Component.LEFT_ALIGNMENT);

        features.add(createWelcomeFeature(
            "01",
            "Master Data",
            "Kelola pengguna, peserta, instruktur, kursus dan kelas.",
            new Color(37, 99, 235)
        ));

        features.add(createWelcomeFeature(
            "02",
            "Pendaftaran",
            "Kelola pendaftaran peserta ke dalam kelas kursus.",
            new Color(5, 150, 105)
        ));

        features.add(createWelcomeFeature(
            "03",
            "Pembayaran",
            "Kelola transaksi dan status pembayaran peserta.",
            new Color(124, 58, 237)
        ));

        features.add(createWelcomeFeature(
            "04",
            "Laporan",
            "Lihat rekapitulasi dan cetak laporan sistem.",
            new Color(234, 88, 12)
        ));

        features.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 150)
        );

        content.add(features);
        content.add(Box.createVerticalStrut(17));

        // SYSTEM INFORMATION

        JPanel systemInfo = new JPanel(
            new GridLayout(2, 2, 8, 6)
        );

        systemInfo.setBackground(SOFT_BG);

        systemInfo.setBorder(
            new EmptyBorder(12, 14, 12, 14)
        );

        systemInfo.add(createInfoLabel(
            "DATABASE", "MySQL"
        ));

        systemInfo.add(createInfoLabel(
            "PLATFORM", "Java Desktop"
        ));

        systemInfo.add(createInfoLabel(
            "ARSITEKTUR", "View - DAO - JDBC"
        ));

        systemInfo.add(createInfoLabel(
            "STATUS", "Siap digunakan"
        ));

        systemInfo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 65)
        );

        content.add(systemInfo);

        root.add(content, BorderLayout.CENTER);

        // FOOTER

        JPanel footer = new JPanel(
            new BorderLayout()
        );

        footer.setBackground(WHITE);

        footer.setBorder(
            new EmptyBorder(12, 30, 22, 30)
        );

        JLabel footerText = new JLabel(
            "SIKURSUS Desktop - Sistem Informasi Manajemen Kursus"
        );

        footerText.setFont(
            new Font("Segoe UI", Font.PLAIN, 10)
        );

        footerText.setForeground(MUTED);

        JButton start = new JButton("Mulai Sekarang");

        start.setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        start.setForeground(WHITE);
        start.setBackground(BLUE);
        start.setFocusPainted(false);
        start.setBorderPainted(false);
        start.setOpaque(true);

        start.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        start.setPreferredSize(
            new Dimension(155, 40)
        );

        start.addActionListener(e -> dialog.dispose());

        footer.add(footerText, BorderLayout.WEST);
        footer.add(start, BorderLayout.EAST);

        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.getRootPane().setDefaultButton(start);

        dialog.setVisible(true);
    }

    // ==================================================
    // WELCOME FEATURE CARD
    // ==================================================

    private JPanel createWelcomeFeature(
        String number,
        String title,
        String description,
        Color accent
    ) {

        JPanel card = new JPanel(
            new BorderLayout(0, 5)
        );

        card.setBackground(SOFT_BG);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(226, 232, 240)
                ),
                new EmptyBorder(10, 12, 10, 12)
            )
        );

        JLabel numberLabel = new JLabel(number);

        numberLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 11)
        );

        numberLabel.setForeground(accent);

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        titleLabel.setForeground(TEXT);

        JTextArea desc = new JTextArea(description);

        desc.setFont(
            new Font("Segoe UI", Font.PLAIN, 10)
        );

        desc.setForeground(MUTED);
        desc.setBackground(SOFT_BG);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBorder(null);

        JPanel heading = new JPanel(
            new BorderLayout()
        );

        heading.setBackground(SOFT_BG);

        heading.add(titleLabel, BorderLayout.WEST);
        heading.add(numberLabel, BorderLayout.EAST);

        card.add(heading, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);

        return card;
    }

    // ==================================================
    // WELCOME INFO LABEL
    // ==================================================

    private JPanel createInfoLabel(
        String title,
        String value
    ) {

        JPanel panel = new JPanel();

        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setBackground(SOFT_BG);

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 9)
        );

        titleLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);

        valueLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 11)
        );

        valueLabel.setForeground(TEXT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(valueLabel);

        return panel;
    }

    // ==================================================
    // SESSION & ROLE
    // ==================================================

    private void applySession() {

        User u = UserSession.getCurrentUser();

        if (u == null) {

            dispose();

            SwingUtilities.invokeLater(
                () -> new LoginFrame().setVisible(true)
            );

            return;
        }

        lblUser.setText(
            "Pengguna: " + u.getUsername()
        );

        lblRole.setText(
            "Role: " + u.getRole()
        );

        boolean admin = u.isAdmin();

        btnUsers.setEnabled(admin);
        btnInstruktur.setEnabled(admin);
        btnKursus.setEnabled(admin);
        btnKelas.setEnabled(admin);

        btnPeserta.setEnabled(true);
    }

    // ==================================================
    // REPORT PERIOD
    // ==================================================

    private void showPaymentPeriod() {

        String a = JOptionPane.showInputDialog(
            this,
            "Tanggal awal (yyyy-MM-dd):",
            ViewUtil.today()
        );

        if (a == null) return;

        String b = JOptionPane.showInputDialog(
            this,
            "Tanggal akhir (yyyy-MM-dd):",
            ViewUtil.today()
        );

        if (b == null) return;

        try {

            Date start = Date.valueOf(a.trim());
            Date end = Date.valueOf(b.trim());

            if (end.before(start)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Tanggal akhir tidak boleh sebelum tanggal awal.",
                    "Validasi Tanggal",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            ReportService.showPembayaranPeriode(
                this, start, end
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Format tanggal harus yyyy-MM-dd.",
                "Format Tanggal Salah",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==================================================
    // LOGOUT
    // ==================================================

    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        WindowManager.closeAll();

        UserSession.clear();

        dispose();

        SwingUtilities.invokeLater(
            () -> new LoginFrame().setVisible(true)
        );
    }
}
