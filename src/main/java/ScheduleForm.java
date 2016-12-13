import org.freixas.jcalendar.JCalendar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 */
public class ScheduleForm extends JFrame{
    private JPanel rootPanel;
    private JTabbedPane tPaneEventManager;
    private JPanel tPane2New;
    private JPanel tPane3Update;
    private JPanel tPane1Menu;
    private JList lstFiles;
    private JButton btnFindFiles;
    private JTextField tbxClassName;
    private JTextField tbxAssignmentName;

    private JButton btnChooseDate;
    private JTextField tbxDescription;
    private JButton btnSelectDateTime;
    private JTextArea txtAreaDescription;
    private JButton btnFindEvent;
    private JButton btnSubmitEvent;
    private JButton btnCreateEvent;
    private JLabel lblSelectedTime;
    private JTable tblDeleteEvent;
    private JButton deleteEventButton;
    private JComboBox cbxDeleteSearchChoices;
    private JTextField tbxDeleteSearch;
    private JButton btnDeleteEventSearch;
    private JButton btnDeleteEvent;
    private JTable tblFindEvent;
    private JTextField tbxFindSearch;
    private JButton btnUpdateEvent;
    private JTextField tbxUpdateClassName;
    private JTextField tbxUpdateEventID;
    private JTextField tbxUpdateDueDate;
    private JTextArea txtAreaUpdateDescription;
    private JComboBox cbxFindEventChoices;
    private JButton btnFindEventSearch;
    private JButton btnDeleteToMain;
    private JButton btnFindToMain;
    private JButton btnCreateToMain;

    private DatabaseEditor mainDB; // Variable to store access to the mainDB
    private JCalendar calendar;
    private int hour;
    private String periodOfDay;
    private DefaultTableModel tblDeleteEventModel = new DefaultTableModel(); // Creates table model for delete event table
    private DefaultTableModel tblFindEventModel = new DefaultTableModel(); // Create table model for find event table
    private DateTimeFormatter fmtDate = DateTimeFormat.forPattern("d MMMM, yyyy");
    private DateTimeFormatter fmtTime = DateTimeFormat.forPattern("h:mm");
    private DateTimeFormatter fmtHalfDay = DateTimeFormat.forPattern("a");

    ScheduleForm(final DatabaseEditor mainDB) {
        this.mainDB = mainDB; // Set this instances mainDB to equal the passed database
        guiSetup(); // Calls method to setup gui


//        File selectedFile; // Create variable to store the file path in
        testInformation(); // todo Populate the fields with test information
//        dbAddEvent("Java", "11/28/2016",  DateTime.now(), "Working on programming"); // todo Add test Event
//        dbReadEvent(-1); // todo Test read event
//        dbReadEvent(2); // todo Test read event
//        dbDeleteEvent("15"); // todo Test delete event

//        mainDB.showWholeDatabase(); // todo Test

        btnFindFiles.addActionListener(new ActionListener() { // Listener for btnFindFiles
            public void actionPerformed(ActionEvent e) {
                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Navigate to your file");
                //In response to a button click:
                int returnVal = fc.showOpenDialog(btnFindFiles); // Returns 0 or 1 depending on if the user chose a file
                // todo Get the selected file and add the name to the file list

                System.out.println("Your returned int: " + returnVal);
                if (returnVal == fc.APPROVE_OPTION){

                }
            }
        });

        btnSelectDateTime.addActionListener(new ActionListener() { // Listener for btnSelectDateTime
            public void actionPerformed(ActionEvent e) {
                showDateSelectDialog();

            }
        }); // Select time
        btnSubmitEvent.addActionListener(new ActionListener() { // Create event Button
            public void actionPerformed(ActionEvent e) {
                if (calendar != null) {
                    Object[] info = new Object[3];
                    info[0] = calendar;
                    info[1] = hour;
                    info[2] = periodOfDay;
                    mainDB.addEvent(tbxClassName.getText(), info, txtAreaDescription.getText()); // todo Add event using user input
//                    tPaneEventManager.setSelectedIndex(0); // Set selected tab to the main menu
                } else {
                    messageBoxDialog("Please select a time using the Select Date and Time button");
                }
            }
        });
        btnCreateEvent.addActionListener(new ActionListener() { // Create Tab button
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(1); // Set selected tab to the create event tab
            }
        });
        btnFindEvent.addActionListener(new ActionListener() { // Update Tab button
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(2); // Set selected tab to the update event tab
            }
        });
        deleteEventButton.addActionListener(new ActionListener() { // Delete Tab button
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(3); // Set selected tab to the delete event tab
            }
        });
        btnDeleteEventSearch.addActionListener(new ActionListener() { // Search button listener
            public void actionPerformed(ActionEvent e) {
                populateTable(mainDB, tbxDeleteSearch, tblDeleteEventModel, tblDeleteEvent, cbxDeleteSearchChoices);
            }
        });
        cbxDeleteSearchChoices.addActionListener(new ActionListener() { // Combo box selection change
            public void actionPerformed(ActionEvent e) {
                comboBoxSelectionUpdate(tblDeleteEventModel, cbxDeleteSearchChoices, tbxDeleteSearch);
            }
        });
        btnDeleteEvent.addActionListener(new ActionListener() { // Delete Button
            public void actionPerformed(ActionEvent e) {
                Object deleteID = tblDeleteEvent.getValueAt(tblDeleteEvent.getSelectedRow(), 0);
                dbDeleteEvent((String) deleteID); // Will be string so I can just cast it
                populateTable(mainDB, tbxDeleteSearch, tblDeleteEventModel, tblDeleteEvent, cbxDeleteSearchChoices);
            }
        });
        btnFindEventSearch.addActionListener(new ActionListener() { // Find Event Button
            public void actionPerformed(ActionEvent e) {
                populateTable(mainDB, tbxFindSearch, tblFindEventModel, tblFindEvent, cbxFindEventChoices);
            }
        });

        // http://stackoverflow.com/questions/9294108/is-there-a-way-to-add-a-row-selected-listener-on-jtable
        tblFindEvent.getSelectionModel().addListSelectionListener(new ListSelectionListener() { // Listens for a change in the table item selection
            // This executes twice?
            public void valueChanged(ListSelectionEvent e) {
//                System.out.println("Change!"); // todo Test
                if (tblFindEvent.getRowCount() > 0) {
                    tbxUpdateEventID.setText((String) tblFindEvent.getModel().getValueAt(tblFindEvent.getSelectedRow(), 0));
                    tbxUpdateClassName.setText((String) tblFindEvent.getModel().getValueAt(tblFindEvent.getSelectedRow(), 1));
                    tbxUpdateDueDate.setText((String) tblFindEvent.getModel().getValueAt(tblFindEvent.getSelectedRow(), 2));
                    txtAreaUpdateDescription.setText((String) tblFindEvent.getModel().getValueAt(tblFindEvent.getSelectedRow(), 3));
                }
            }
        }); // Listens for a change in the table item selection

        btnUpdateEvent.addActionListener(new ActionListener() { // Update event button
            public void actionPerformed(ActionEvent e) {
                if (!tbxUpdateEventID.getText().equalsIgnoreCase("")){
                    System.out.println("Updating event with the id of: " + tbxUpdateEventID.getText());
                    mainDB.updateEvent(tbxUpdateEventID.getText(), tbxUpdateClassName.getText(), tbxUpdateDueDate.getText(), txtAreaUpdateDescription.getText());
                } else {
                    messageBoxDialog("Please select an event to update");
                }
                populateTable(mainDB, tbxFindSearch, tblFindEventModel, tblFindEvent, cbxFindEventChoices);
            }
        }); // Update event button

        cbxFindEventChoices.addActionListener(new ActionListener() { // Listens for a change in the find event combobox
            public void actionPerformed(ActionEvent e) {
                comboBoxSelectionUpdate(tblFindEventModel, cbxFindEventChoices, tbxFindSearch);
            }
        }); // Listens for a change in the find event combobox
        btnCreateToMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(0); // Show main menu
            }
        });
        btnFindToMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(0); // Show main menu
            }
        });
        btnDeleteToMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tPaneEventManager.setSelectedIndex(0); // Show main menu
            }
        });
    }

    private void comboBoxSelectionUpdate(DefaultTableModel table, JComboBox comboBox, JTextField textBox) { // Method to handle the changing of selection in a combobox
        table.setRowCount(0); // Clears the delete event table
        if (comboBox.getSelectedItem().equals("Due Date")){
            showDateSelectDialog();
            DateTime deleteSearchDate = new DateTime(calendar.getDate());
            textBox.setText(deleteSearchDate.toString(fmtDate));
        }
    } // Method to handle the changing of selection in a combobox

    // Method to populate each pages table using the current tabs components
    private void populateTable(DatabaseEditor mainDB, JTextField tbxSearchText, DefaultTableModel tableModel, JTable jTable, JComboBox comboBox) {
        ArrayList<String> resultList;
        tableModel.setRowCount(0); // Clear the delete event table
        if (comboBox.getSelectedItem().equals("Class Name")) {
            resultList = mainDB.findByClassName(tbxSearchText.getText());
        } else {
            resultList = mainDB.findByDueDate(tbxSearchText.getText());
        }
        for (String result : resultList) {
            String[] row = result.split(";");
            DateTime dueDate = new DateTime(row[2]);
            row[2] = dueDate.toString(fmtDate) + " (" + dueDate.toString(fmtTime) + " " + dueDate.toString(fmtHalfDay) + ")";
            tableModel.addRow(row);
        }
        jTable.setModel(tableModel);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Set date column width to fit date
    } // Method to populate each pages table using the current tabs components

    private void guiSetup() { // Method to setup gui
        setContentPane(rootPanel);
        pack(); // Pack gui contents
        setTitle("Assignment Manager"); // Set title
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // End program on close
        setVisible(true); // Make window visible
        cbxDeleteSearchChoices.addItem("Class Name");
        cbxDeleteSearchChoices.addItem("Due Date");
        cbxFindEventChoices.addItem("Class Name");
        cbxFindEventChoices.addItem("Due Date");
        tblDeleteEventModel.addColumn("Event ID");
        tblDeleteEventModel.addColumn("Class Name");
        tblDeleteEventModel.addColumn("Time Due");
        tblDeleteEventModel.addColumn("Description");
        tblFindEventModel.addColumn("Event ID");
        tblFindEventModel.addColumn("Class Name");
        tblFindEventModel.addColumn("Time Due");
        tblFindEventModel.addColumn("Description");
        tblFindEvent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDeleteEvent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    } // Method to set up the gui components

    public void dbDeleteEvent(String eventID){ // Method to delete an event using the Event_ID
        mainDB.deleteEvent(eventID);
    }

    public void dbReadEvent(int eventID){ // Method to read an event from the database using Event_ID
            mainDB.readEvent(eventID);
    }

    public void showDateSelectDialog(){
        CustomDialog dateSelectDialog = new CustomDialog(); // Create dialog for selecting date and time
        calendar = dateSelectDialog.getCalendar();
        hour = dateSelectDialog.timeDue.getSelectedIndex();
        periodOfDay = dateSelectDialog.timeDueAP.getSelectedItem().toString();
        System.out.println(calendar.getDate()); // todo Test Shows the selected date from the JCalendar
        lblSelectedTime.setText(calendar.getDate().toString());
    }

    public void testInformation(){
        tbxClassName.setText("Java");
        tbxAssignmentName.setText("Assignment");
        txtAreaDescription.setText("Description");
    } // Test information to make testing easier

    public void messageBoxDialog(String text){ // Method to show dialog using provided text
        JOptionPane.showMessageDialog(rootPanel, text);
    }



}
