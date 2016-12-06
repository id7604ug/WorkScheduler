
import org.freixas.jcalendar.JCalendar;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 */
public class ScheduleForm extends JFrame{
    private JList lstFiles;
    private JButton btnFindFiles;
    private JTextField tbxClassName;
    private JTextField tbxAssignmentName;
    private JPanel rootPanel;
    private JButton btnChooseDate;
    private JTextField tbxDescription;
    private JButton btnSelectDateTime;
    private JTextArea txtAreaDescription;
    private JButton btnUpdateEvent;
    private JButton btnEnterEvent;
    private DatabaseEditor mainDB = new DatabaseEditor("test.db");// todo change to final db name
    private JCalendar calendar;
    private String hour;
    private String periodOfDay;

    ScheduleForm() throws ClassNotFoundException {
        setContentPane(rootPanel);
        pack(); // Pack gui contents
        setTitle("Work Scheduler"); // Set title
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // End program on close
        setVisible(true); // Make window visible

        File selectedFile; // Create variable to store the file path in
        dbCreate(); // Create database
        dbTableCreate(); // Create table
        testInformation();
//        dbAddEvent("Java", "11/28/2016",  DateTime.now(), "Working on programming"); // Add test Event
//        dbDeleteEvent(10); // Delete event test
        dbReadEvent(-1); // Test read event
        dbReadEvent(2); // Test read event

//        mainDB.showWholeDatabase(); // Test

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
            public void actionPerformed(ActionEvent e) { // todo Retrieve date from the dialog
                showDateSelectDialog();
            }
        });
        btnEnterEvent.addActionListener(new ActionListener() { // Create event Button
            public void actionPerformed(ActionEvent e) {
                Object[] info = new Object[3];
                info[0] = calendar;
                info[1] = hour;
                info[2] = periodOfDay;
                mainDB.addEvent(tbxClassName.getText(), info, txtAreaDescription.getText()); // todo Add event using user input
            }
        });
        btnUpdateEvent.addActionListener(new ActionListener() { // Listener for Update An Event Button
            public void actionPerformed(ActionEvent e) {
                UpdateEventForm updateForm = new UpdateEventForm(); // Create the UpdateEventForm
            }
        });
    }

    public void dbCreate(){ // Method to create the db
        mainDB.createDatabase();
    }

    public void dbTableCreate(){ // Method to create the main table
        mainDB.createTables();
    }

//    public void dbAddEvent(String className, String eventDate, DateTime dueTime, String eventDescription){ // Don't use this method
//
//        mainDB.addEvent(className, eventDate, dueTime, eventDescription); // Send event info to db
//    }

    public void dbDeleteEvent(int eventID){ // Method to delete an event using the Event_ID
        mainDB.deleteEvent(eventID);
    }

    public void dbReadEvent(int eventID){ // Method to read an event from the database using Event_ID
            mainDB.readEvent(eventID);
    }

    public void showDateSelectDialog(){ // todo Return date selected
        CustomDialog dateSelectDialog = new CustomDialog(); // Create dialog for selecting date and time
        calendar = dateSelectDialog.getCalendar();
        hour = dateSelectDialog.timeDue.toString();
        periodOfDay = dateSelectDialog.timeDueAP.toString();
        System.out.println(calendar.getDate()); // Shows the selected date from the JCalendar
    }

    public void testInformation(){
        tbxClassName.setText("Class name the user enters");
        tbxAssignmentName.setText("Assignment name the user enters");
        txtAreaDescription.setText("Description the user enters");
    }

    public void messageBoxDialog(String text){ // Method to show dialog using provided text
        JOptionPane.showMessageDialog(rootPanel, text);
    }

}
