/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package journo;

/**
 *
 * @author Kryz
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;

public class AdminView extends JFrame {
    private DefaultTableModel tableModel;
    private JTable jTableEntries;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnDelete;
    
    public AdminView() {
        initComponents();
        loadEntries(); // Load all entries initially
    }
    
    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
        jTableEntries = new JTable();
        btnEdit = new JButton();
        btnSave = new JButton();
        btnDelete = new JButton();
        txtSearch = new JTextField();
        btnSearch = new JButton();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Title", "Content", "Author", "Timestamp" }
        );
        jTableEntries.setModel(tableModel);
        jScrollPane1.setViewportView(jTableEntries);
        
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        
        btnSave.setText("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        
        btnSearch.setText("Search");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnEdit)
                    .addComponent(btnSave)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void loadEntries() {
        tableModel.setRowCount(0); // Clear existing rows
        String sqlQuery = "SELECT j.journal_id, j.title, j.content, u.username, j.date_created FROM journals j JOIN users u ON j.user_id = u.user_id"; // Get all journals
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("journal_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("username"),
                    rs.getTimestamp("date_created")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchEntries(String query) {
        tableModel.setRowCount(0); // Clear existing rows
        String sqlQuery = "SELECT j.journal_id, j.title, j.content, u.username, j.date_created FROM journals j JOIN users u ON j.user_id = u.user_id WHERE j.title LIKE ? OR j.content LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
            
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("journal_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("username"),
                    rs.getTimestamp("date_created")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void btnSearchActionPerformed(ActionEvent evt) {
        String searchQuery = txtSearch.getText().trim();
        if (!searchQuery.isEmpty()) {
            searchEntries(searchQuery);
        } else {
            loadEntries(); // Reload all entries if search query is empty
        }
    }
    
    private void btnEditActionPerformed(ActionEvent evt) {
        int selectedRow = jTableEntries.getSelectedRow();
        if (selectedRow >= 0) {
            int journalId = (int) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String content = (String) tableModel.getValueAt(selectedRow, 2);
            String author = (String) tableModel.getValueAt(selectedRow, 3);
            
            // Open an editor dialog or form to edit the selected journal
            EditJournalDialog dialog = new EditJournalDialog(this, true, journalId, title, content, author);
            dialog.setVisible(true);
            loadEntries(); // Refresh the entries after the dialog is closed
        } else {
            JOptionPane.showMessageDialog(this, "Please select a journal to edit.", "Edit Journal", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void btnSaveActionPerformed(ActionEvent evt) {
        // Save changes made in the editor dialog/form
        // This implementation depends on the details of your EditJournalDialog class
    }
    
    private void btnDeleteActionPerformed(ActionEvent evt) {
        int selectedRow = jTableEntries.getSelectedRow();
        if (selectedRow >= 0) {
            int journalId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this journal?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteJournal(journalId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a journal to delete.", "Delete Journal", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteJournal(int journalId) {
        String sqlQuery = "DELETE FROM Journals WHERE journal_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
            
            stmt.setInt(1, journalId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Journal deleted successfully.");
                loadEntries(); // Reload entries after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete journal.", "Delete Journal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminView().setVisible(true));
    }
}
