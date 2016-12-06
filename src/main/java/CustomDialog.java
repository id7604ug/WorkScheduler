import org.freixas.jcalendar.JCalendar;

import javax.swing.*;
import java.awt.*;

class CustomDialog{

    /*
        Much of this code was found on stack overflow.
        http://stackoverflow.com/questions/12234850/customize-joptionpane-dialog
        User: nIcE cOw was very helpful in customizing a dialog.
     */
    JPanel panel = new JPanel();
    JCalendar calendar = new JCalendar();
    JComboBox timeDue = new JComboBox();
//    JComboBox endTime = new JComboBox();
    JComboBox timeDueAP = new JComboBox();
//    JComboBox endTimeAP = new JComboBox();
    Label stLabel = new Label();
//    Label etLabel = new Label();
    String timeText;

    public CustomDialog() {
        displayGUI();
    }
    public void displayGUI() {
        JOptionPane.showConfirmDialog(null, getPanel(), "Date and Time : ", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    public JPanel getPanel() {
        // todo Make this look better
        // http://docs.oracle.com/javase/tutorial/uiswing/layout/none.html
        panel.add(calendar);
        timeCBXSetup(panel);
        return panel;
    }

    private void timeCBXSetup(JPanel panel) {
        // Populate each JComboBox
        timeDueAP.addItem("AM");
        timeDueAP.addItem("PM");
//        endTimeAP.addItem("AM");
//        endTimeAP.addItem("PM");
        timeDue.addItem(12 + ":00");
        for (int i = 1; i < 12; i++) {
            timeDue.addItem(i + ":00");
//            endTime.addItem(i);
        }
        timeDue.setSelectedIndex(0);
//        endTime.setSelectedIndex(11);

        // Add each component to the panel
        stLabel.setText("Time Due:");
        panel.add(stLabel);
        panel.add(timeDueAP);
        panel.add(timeDue);

//        etLabel.setText("End Time:");
//        panel.add(etLabel);
//        panel.add(endTimeAP);
//        panel.add(endTime);

    }

    public JCalendar getCalendar(){
        return calendar;
    }

    public String getTime (){ // Getter for returning the selected time from the dialog
        timeText = timeDue.getSelectedItem().toString();
        return null; // todo Return correct information
    }

}