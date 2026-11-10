
package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import util.LookupItem;

/**
 * Design System SIKURSUS Desktop.
 * Tema Modern Blue - Navy, Blue, White, dan Soft Gray.
 */
public final class ViewUtil {

    // ================= COLOR PALETTE =================

    public static final Color BG = new Color(241, 245, 249);
    public static final Color CARD = Color.WHITE;

    public static final Color HEADER = new Color(15, 35, 70);
    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);

    public static final Color SUCCESS = new Color(5, 150, 105);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color SECONDARY = new Color(100, 116, 139);

    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color TEXT = new Color(30, 41, 59);
    public static final Color MUTED = new Color(100, 116, 139);

    public static final Color INPUT_BG = new Color(248, 250, 252);
    public static final Color SELECTED = new Color(219, 234, 254);

    private static final Font FONT =
        new Font("Segoe UI", Font.PLAIN, 13);

    private static final Font FONT_BOLD =
        new Font("Segoe UI", Font.BOLD, 13);

    private ViewUtil() {
    }

    // ================= TABLE MODEL =================

    public static DefaultTableModel tableModel(String... columns) {

        return new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // ================= FRAME =================

    public static void setupFrame(
        JFrame frame, int width, int height
    ) {

        frame.setSize(width, height);

        frame.setMinimumSize(new Dimension(
            Math.min(width, 760),
            Math.min(height, 520)
        ));

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);

        frame.setFont(FONT);
    }

    // ================= ROOT =================

    public static JPanel root(String title) {

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(BG);

        root.add(header(title), BorderLayout.NORTH);

        return root;
    }

    // ================= HEADER =================

    public static JPanel header(String title) {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(HEADER);
        header.setPreferredSize(new Dimension(100, 76));

        header.setBorder(
            new EmptyBorder(0, 28, 0, 28)
        );

        JLabel label = new JLabel(title);

        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 21));

        header.add(label, BorderLayout.WEST);

        return header;
    }

    // ================= BODY =================

    public static JPanel body() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));

        panel.setBackground(BG);

        panel.setBorder(
            new EmptyBorder(22, 25, 22, 25)
        );

        return panel;
    }

    // ================= FORM PANEL =================

    public static JPanel formPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(CARD);

        panel.setBorder(
            new EmptyBorder(12, 14, 12, 14)
        );

        return panel;
    }

    // ================= FORM ROW =================

    public static void addFormRow(
        JPanel panel,
        int row,
        String text,
        JComponent component
    ) {

        JLabel label = new JLabel(text);

        label.setForeground(TEXT);
        label.setFont(FONT_BOLD);

        GridBagConstraints lc = new GridBagConstraints();

        lc.gridx = 0;
        lc.gridy = row;

        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(9, 5, 9, 20);

        lc.ipadx = 5;

        panel.add(label, lc);

        styleInput(component);

        GridBagConstraints fc = new GridBagConstraints();

        fc.gridx = 1;
        fc.gridy = row;

        fc.weightx = 1.0;
        fc.fill = GridBagConstraints.HORIZONTAL;

        fc.anchor = GridBagConstraints.WEST;

        fc.insets = new Insets(9, 0, 9, 5);

        panel.add(component, fc);
    }

    public static void addWideRow(
        JPanel panel,
        int row,
        String text,
        JComponent component
    ) {

        addFormRow(panel, row, text, component);
    }

    // ================= INPUT STYLE =================

    public static void styleInput(JComponent component) {

        component.setFont(FONT);
        component.setForeground(TEXT);

        if (component instanceof JTextField
            || component instanceof JPasswordField) {

            component.setBackground(WHITE_COLOR());

            component.setPreferredSize(
                new Dimension(420, 38)
            );

            component.setMinimumSize(
                new Dimension(180, 36)
            );

            component.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(6, 10, 6, 10)
                )
            );

            // Efek fokus pada input

            if (component.getClientProperty("viewutil.focus") == null) {

                component.putClientProperty(
                    "viewutil.focus", Boolean.TRUE
                );

                BorderLayout unused = null;

                component.addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusGained(FocusEvent e) {

                        component.setBorder(
                            BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                    PRIMARY, 2
                                ),
                                new EmptyBorder(5, 9, 5, 9)
                            )
                        );
                    }

                    @Override
                    public void focusLost(FocusEvent e) {

                        component.setBorder(
                            BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(BORDER),
                                new EmptyBorder(6, 10, 6, 10)
                            )
                        );
                    }
                });
            }

        } else if (component instanceof JComboBox) {

            component.setBackground(Color.WHITE);

            component.setPreferredSize(
                new Dimension(420, 38)
            );

            component.setMinimumSize(
                new Dimension(180, 36)
            );

        } else if (component instanceof JCheckBox
            || component instanceof JRadioButton) {

            component.setBackground(CARD);
            ((AbstractButton) component).setFocusPainted(false);
        }
    }

    private static Color WHITE_COLOR() {
        return Color.WHITE;
    }

    // ================= BUTTONS =================

    public static JButton primaryButton(String text) {

        return button(text, PRIMARY, Color.WHITE);
    }

    public static JButton updateButton(String text) {

        return button(text, PRIMARY_DARK, Color.WHITE);
    }

    public static JButton successButton(String text) {

        return button(text, SUCCESS, Color.WHITE);
    }

    public static JButton dangerButton(String text) {

        return button(text, DANGER, Color.WHITE);
    }

    public static JButton neutralButton(String text) {

        return button(text, SECONDARY, Color.WHITE);
    }

    private static JButton button(
        String text,
        Color background,
        Color foreground
    ) {

        JButton b = new JButton(text);

        b.setFont(FONT_BOLD);

        b.setForeground(foreground);
        b.setBackground(background);

        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);

        b.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        b.setBorder(
            new EmptyBorder(9, 17, 9, 17)
        );

        b.setPreferredSize(new Dimension(
            Math.max(105, text.length() * 9 + 30),
            38
        ));

        // Hover effect

        b.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                if (b.isEnabled()) {
                    b.setBackground(background.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                b.setBackground(background);
            }
        });

        return b;
    }

    // ================= ACTION PANEL =================

    public static JPanel actionPanel(JButton... buttons) {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 8)
        );

        panel.setBackground(CARD);

        for (JButton button : buttons) {
            panel.add(button);
        }

        return panel;
    }

    // ================= SEARCH PANEL =================

    public static JPanel searchPanel(
        String labelText,
        JTextField field,
        JButton button
    ) {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 8)
        );

        panel.setBackground(CARD);

        JLabel label = new JLabel(labelText);

        label.setForeground(TEXT);
        label.setFont(FONT_BOLD);

        panel.add(label);

        styleInput(field);

        field.setPreferredSize(
            new Dimension(230, 38)
        );

        panel.add(field);
        panel.add(button);

        return panel;
    }

    // ================= SECTION CARD =================

    public static JPanel section(
        String title,
        JComponent content
    ) {

        JPanel card = new JPanel(
            new BorderLayout(0, 12)
        );

        card.setBackground(CARD);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 18, 18, 18)
            )
        );

        if (title != null && !title.trim().isEmpty()) {

            JLabel label = new JLabel(title);

            label.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
            );

            label.setForeground(HEADER);

            card.add(label, BorderLayout.NORTH);
        }

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE SCROLL =================

    public static JScrollPane tableScroll(JTable table) {

        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(
            BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(Color.WHITE);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    // ================= TABLE STYLE =================

    public static void styleTable(JTable table) {

        table.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        table.setRowHeight(36);

        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);

        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        table.setSelectionBackground(SELECTED);
        table.setSelectionForeground(TEXT);

        table.setGridColor(
            new Color(241, 245, 249)
        );

        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        table.setIntercellSpacing(
            new Dimension(0, 1)
        );

        table.setFillsViewportHeight(true);

        // Header

        JTableHeaderStyle(table);

        // Alternating row color

        DefaultTableCellRenderer renderer =
            new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                JTable t,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
            ) {

                super.getTableCellRendererComponent(
                    t, value, selected, focus, row, column
                );

                if (selected) {

                    setBackground(SELECTED);

                } else {

                    setBackground(
                        row % 2 == 0
                            ? Color.WHITE
                            : new Color(248, 250, 252)
                    );
                }

                setForeground(TEXT);

                setBorder(
                    new EmptyBorder(0, 9, 0, 9)
                );

                return this;
            }
        };

        table.setDefaultRenderer(
            Object.class, renderer
        );
    }

    private static void JTableHeaderStyle(JTable table) {

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        table.getTableHeader().setBackground(HEADER);
        table.getTableHeader().setForeground(Color.WHITE);

        table.getTableHeader().setPreferredSize(
            new Dimension(20, 40)
        );

        table.getTableHeader().setReorderingAllowed(false);

        table.getTableHeader().setOpaque(true);
    }

    // ================= NOTE =================

    public static JLabel note(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        label.setForeground(MUTED);

        return label;
    }

    // ================= SQL ERROR =================

    public static void sqlError(
        Component parent,
        String action,
        SQLException ex
    ) {

        JOptionPane.showMessageDialog(
            parent,
            action + " gagal.\n" + friendlySqlMessage(ex),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static String friendlySqlMessage(SQLException ex) {

        String m = ex.getMessage() == null
            ? ex.toString()
            : ex.getMessage();

        String lower = m.toLowerCase();

        if (m.contains("Duplicate entry")) {

            return "Data duplikat ditolak database.\n" + m;
        }

        if (lower.contains("foreign key constraint")) {

            return "Operasi ditolak karena data masih digunakan oleh relasi lain.\n"
                + m;
        }

        if (lower.contains("cannot be null")) {

            return "Ada data wajib yang belum diisi.\n" + m;
        }

        return m;
    }

    // ================= DATE =================

    public static String today() {

        return LocalDate.now().toString();
    }

    // ================= LOOKUP =================

    public static void selectLookup(
        JComboBox<LookupItem> combo,
        int id
    ) {

        for (int i = 0; i < combo.getItemCount(); i++) {

            LookupItem item = combo.getItemAt(i);

            if (item != null && item.getId() == id) {

                combo.setSelectedIndex(i);

                return;
            }
        }
    }
}
