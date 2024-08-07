/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package journo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout;

/**
 *
 * @author 
 */
public class EditJournalDialog extends javax.swing.JDialog {
    private int journalId;
    private JTextField txtTitle;
    private JTextArea txtContent;
    private JTextField txtAuthor;
    private JButton btnSave;
    private JButton btnCancel;

    public EditJournalDialog(java.awt.Frame parent, boolean modal, int journalId, String title, String content, String author) {
        super(parent, modal);
        this.journalId = journalId;
        initComponents();
        txtTitle.setText(title);
        txtContent.setText(content);
        txtAuthor.setText(author);
        txtAuthor.setEditable(false); // Make author field non-editable
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Title:");
        JLabel lblContent = new JLabel("Content:");
        JLabel lblAuthor = new JLabel("Author:");

        txtTitle = new JTextField();
        txtContent = new JTextArea(5, 20);
        txtAuthor = new JTextField();

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        // Use GroupLayout to position components in a more pleasing layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTitle)
                        .addComponent(lblContent)
                        .addComponent(lblAuthor))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtTitle)
                        .addComponent(txtContent)
                        .addComponent(txtAuthor)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnSave)
                            .addComponent(btnCancel))))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(txtTitle))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContent)
                    .addComponent(txtContent))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAuthor)
                    .addComponent(txtAuthor))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void btnSaveActionPerformed(ActionEvent evt) {
        String newTitle = txtTitle.getText().trim();
        String newContent = txtContent.getText().trim();

        if (!newTitle.isEmpty() && !newContent.isEmpty()) {
            updateJournal(newTitle, newContent);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void updateJournal(String title, String content) {
        String sqlQuery = "UPDATE journals SET title = ?, content = ? WHERE journal_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setInt(3, journalId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Journal updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update journal.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
