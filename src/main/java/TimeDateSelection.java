import org.freixas.jcalendar.JCalendar;

import javax.swing.*;

/**
 * Created by Altei on 11/28/2016.
 */
public class TimeDateSelection extends JFrame {
    private JPanel tdRootPanel;
    private JCalendar calDateSelect;
    private JComboBox cbxStartTime;
    private JComboBox cbsEndTime;
    private JComboBox cbxAmPm2;
    private JComboBox cbxAmPm1;

    TimeDateSelection(){
        setContentPane(tdRootPanel);
        pack(); // Pack gui contents
        setTitle("Work Scheduler"); // Set title
        setVisible(true); // Make window visible
    }
}
