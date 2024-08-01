package journo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Dashboard extends javax.swing.JFrame {
    private UserAuth userAuth;
    private DefaultListModel<String> journalListModel;
    private ArrayList<Integer> journalIds;
    
    public Dashboard(UserAuth userAuth) {
        this.userAuth = userAuth;
        initComponents();
        loadJournals(false); // Load all public journals initially
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblJournalTitle = new javax.swing.JLabel();
        txtJournalTitle = new javax.swing.JTextField();
        lblJournalEntry = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtJournalEntry = new javax.swing.JTextArea();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstJournals = new javax.swing.JList<>();
        chkShowOnlyPersonalEntries = new javax.swing.JCheckBox();
        btnSave = new javax.swing.JButton();
        
        // Custom Components from the existing code
        lblWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnCreateJournal = new javax.swing.JButton();
        btnViewJournals = new javax.swing.JButton();
        btnAccountSettings = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblJournalTitle.setText("Journal Title:");

        lblJournalEntry.setText("Journal Entry:");

        txtJournalEntry.setColumns(20);
        txtJournalEntry.setRows(5);
        jScrollPane2.setViewportView(txtJournalEntry);

        btnEdit.setText("Edit");
        btnEdit.setEnabled(false); // Initially disabled
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setEnabled(false); // Initially disabled
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        journalListModel = new DefaultListModel<>();
        lstJournals.setModel(journalListModel);
        lstJournals.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                lstJournalsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstJournals);

        chkShowOnlyPersonalEntries.setText("Show Only Personal Entries");
        chkShowOnlyPersonalEntries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chkShowOnlyPersonalEntriesActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.setEnabled(false); // Initially disabled
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblWelcome.setText("Welcome, User!");

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnCreateJournal.setText("Create Journal");
        btnCreateJournal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateJournalActionPerformed(evt);
            }
        });

        btnViewJournals.setText("View Journals");
        btnViewJournals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewJournalsActionPerformed(evt);
            }
        });

        btnAccountSettings.setText("Account Settings");
        btnAccountSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblJournalTitle)
                        .addComponent(lblJournalEntry))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                        .addComponent(txtJournalTitle))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(chkShowOnlyPersonalEntries)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(21, 21, 21))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblWelcome)
                        .addComponent(btnLogout)
                        .addComponent(btnCreateJournal)
                        .addComponent(btnViewJournals)
                        .addComponent(btnAccountSettings))
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblJournalTitle)
                        .addComponent(txtJournalTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblJournalEntry)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnDelete)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSave)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(chkShowOnlyPersonalEntries)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(lblWelcome)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnLogout)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnCreateJournal)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnViewJournals)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnAccountSettings)
                    .addContainerGap())
        );

        jTabbedPane1.addTab("View Entry", jPanel1);
        jTabbedPane1.addTab("Write Entry", jPanel2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }

    private void lstJournalsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            int selectedIndex = lstJournals.getSelectedIndex();
            if (selectedIndex != -1) {
                loadJournalEntry(journalIds.get(selectedIndex));
            }
        }
    }

    private void loadJournalEntry(int journalId) {
        String query = "SELECT title, content, user_id FROM Journals WHERE journal_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, journalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtJournalTitle.setText(rs.getString("title"));
                txtJournalEntry.setText(rs.getString("content"));

                boolean isCurrentUserEntry = rs.getInt("user_id") == userAuth.getCurrentUserId();
                btnEdit.setEnabled(isCurrentUserEntry);
                btnDelete.setEnabled(isCurrentUserEntry);
                btnSave.setEnabled(false); // Disable Save until Edit is clicked
                txtJournalTitle.setEditable(false);
                txtJournalEntry.setEditable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadJournals(boolean onlyCurrentUser) {
        journalListModel.clear();
        journalIds = new ArrayList<>();
        String query = "SELECT journal_id, title FROM Journals WHERE is_public = 1";
        if (onlyCurrentUser) {
            query += " AND user_id = ?";
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (onlyCurrentUser) {
                stmt.setInt(1, userAuth.getCurrentUserId());
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                journalIds.add(rs.getInt("journal_id"));
                journalListModel.addElement(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chkShowOnlyPersonalEntriesActionPerformed(java.awt.event.ActionEvent evt) {
        loadJournals(chkShowOnlyPersonalEntries.isSelected());
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        txtJournalTitle.setEditable(true);
        txtJournalEntry.setEditable(true);
        btnSave.setEnabled(true);
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = lstJournals.getSelectedIndex();
        if (selectedIndex != -1) {
            int journalId = journalIds.get(selectedIndex);
            String query = "UPDATE Journals SET title = ?, content = ? WHERE journal_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, txtJournalTitle.getText());
                stmt.setString(2, txtJournalEntry.getText());
                stmt.setInt(3, journalId);
                stmt.executeUpdate();
                loadJournals(chkShowOnlyPersonalEntries.isSelected()); // Refresh the list
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = lstJournals.getSelectedIndex();
        if (selectedIndex != -1) {
            int journalId = journalIds.get(selectedIndex);
            String query = "DELETE FROM Journals WHERE journal_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, journalId);
                stmt.executeUpdate();
                loadJournals(chkShowOnlyPersonalEntries.isSelected()); // Refresh the list
                txtJournalTitle.setText("");
                txtJournalEntry.setText("");
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnSave.setEnabled(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your logout logic here
        JOptionPane.showMessageDialog(this, "Logged out!");
    }

    private void btnCreateJournalActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your create journal logic here
        JOptionPane.showMessageDialog(this, "Create Journal clicked!");
    }

    private void btnViewJournalsActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your view journals logic here
        JOptionPane.showMessageDialog(this, "View Journals clicked!");
    }

    private void btnAccountSettingsActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your account settings logic here
        JOptionPane.showMessageDialog(this, "Account Settings clicked!");
    }

    // Variables declaration
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnCreateJournal;
    private javax.swing.JButton btnViewJournals;
    private javax.swing.JButton btnAccountSettings;
    private javax.swing.JCheckBox chkShowOnlyPersonalEntries;
    private javax.swing.JLabel lblJournalEntry;
    private javax.swing.JLabel lblJournalTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JList<String> lstJournals;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea txtJournalEntry;
    private javax.swing.JTextField txtJournalTitle;
    // End of variables declaration
}
