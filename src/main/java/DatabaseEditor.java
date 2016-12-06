import org.freixas.jcalendar.JCalendar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


/**
 *  Class to modify the database for the program
 */

public class DatabaseEditor {
    public static String databaseLocation;

    DatabaseEditor(String databaseName) throws ClassNotFoundException{
        databaseLocation = "jdbc:sqlite:src/main/resources/" + databaseName;
        Class.forName("org.sqlite.JDBC"); // Loads the class
    }

    // http://www.sqlitetutorial.net/sqlite-java/create-database/
    public void createDatabase(){ // Method to create the database
        File f = new File(databaseLocation);
        if (!f.exists()) {
            String url = databaseLocation;
            Connection c = null;
            try {
                c = DriverManager.getConnection(url);
                if (c != null) {
                    DatabaseMetaData meta = c.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (c != null) {
                        c.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("There was an issue finding the database");
                    System.out.println(ex.getMessage());
                }
            }
        } else {
            System.out.println("Database file exists. No need to create another.");
        }
        System.out.println("Opened database successfully");
    } // Method to create the database

    // http://www.sqlitetutorial.net/sqlite-create-table/
    public void createTables(){ // Method to create the tables if they don't exists in the database
        String url = databaseLocation; // Connection string
        // SQL statement for creating a new table
        String sql1 = "CREATE TABLE IF NOT EXISTS ScheduleTable(\n"
                + "	Event_ID integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	Class_Name text NOT NULL,\n"
                + " Time_Due text NOT NULL,\n"
                + " Event_Description text NOT NULL\n"
                + ");";
        String sql2 = "CREATE TABLE IF NOT EXISTS FilesTable(\n"
                + " Event_ID integer,\n"
                + " File_Name text,\n"
                + " File_Type text\n"
                + " File_Location text,\n"
                + " PRIMARY KEY(Event_ID, File_Name)"
                + ");";
        Connection c = null; // Create connection
        try{
            c = DriverManager.getConnection(url);
            Statement stmt = c.createStatement();
            stmt.execute(sql1); // Create ScheduleTable
            stmt.execute(sql2); // Create FilesTable
            System.out.println("Tables created successfully.");
            c.close(); // Close connection
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("There was an issue creating the databases.");
        } catch (NullPointerException ex){
            System.out.println(ex.getMessage());
            System.out.println("SQL statement couldn't be created.");
        }
    } // Method to create the tables if they don't exists in the database

    // http://www.sqlitetutorial.net/sqlite-insert/
    public void addEvent(String className, Object[] timeDue, String eventDescription){ // Method to add an event to the db
        // gets the date from the JCalendar stored in the list of objects
        DateTime dt = new DateTime(((JCalendar) timeDue[0]).getDate()) // Set date
                .withMillisOfDay(0) // Set Milliseconds to 0
                .withHourOfDay((Integer) timeDue[1]); // Set hour of day

        if (((String) timeDue[2]).equalsIgnoreCase("pm")){ // If PM is selected
            dt = dt.hourOfDay().addToCopy(12); // Add 12 hours
        }
        System.out.println(dt.toString()); // Test
//        dt.hourOfDay().addToCopy((Integer) timeDue[1]); // Add
        String url = databaseLocation;
        try{
            Connection c = DriverManager.getConnection(url);
            PreparedStatement prepSQL = c.prepareStatement("INSERT INTO ScheduleTable (Class_Name, Time_Due, Event_Description) VALUES (?, ?, ?);");
            prepSQL.setString(1, className); // Class name - String
            prepSQL.setString(2, dt.toString()); //  Date - UTC - Joda-Time
            prepSQL.setString(3, eventDescription); // Description of event - String
            prepSQL.execute();
            c.close(); // Close connection
            System.out.println("Row was added successfully");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("There was an issue adding the information to the database.");
        }
    } // Method to add an event to the db

    // http://www.sqlitetutorial.net/sqlite-delete/
    public void deleteEvent(int eventID){ // Method to delete an event using it's Event_ID
        String url = databaseLocation;
        Connection c = null;
        try{
            c = DriverManager.getConnection(url);
            String sql = "DELETE FROM ScheduleTable WHERE Event_ID = " + eventID + ";"; // todo Prepared statement
            Statement stmt = c.createStatement();
            stmt.execute(sql);
            c.close(); // Close connection
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("There was an issue finding or deleting your event.");
        }
    } // Method to delete an event using it's Event_ID

    public void updateEvent(int eventID){ // Method to update an event in the database

    }

    public void readEvent(int eventID) {
        String eventIDString = Integer.toString(eventID);

        String url = databaseLocation;
        Connection c = null;
        try{
            c = DriverManager.getConnection(url);
            PreparedStatement prepSQL = c.prepareStatement("SELECT * FROM ScheduleTable WHERE Event_ID=?");
            prepSQL.setString(1, eventIDString); // Set value of Event_ID to query for
            ResultSet rs = prepSQL.executeQuery(); // Execute the query
            if (rs.next()){ // In case a row is not found
                System.out.println("Here is the class name for Event_ID " + eventIDString + ": " + rs.getString(2)); // Pulls each column of information from the
            } else {
                System.out.println("No events were found for Event_ID: " + eventIDString);
            }
            c.close(); // Close connection
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void findByClassName(String className){ // Method to find events by class name

    }

    public void findByDueDate(DateTime dueDate){ // Method to find events by due date

    }

    public void showWholeDatabase(){
        String url = databaseLocation;
        Connection c = null;
        try{
            c = DriverManager.getConnection(url);
            PreparedStatement prepSQL = c.prepareStatement("SELECT * FROM ScheduleTable");
            ResultSet rs = prepSQL.executeQuery(); // Execute the query
            while (rs.next()){ // Just in case a row is not found

                System.out.println("Here is the class name for Event_ID " + rs.getString(1) +  ": " + rs.getString(2) + " Time:" + DateTime.parse(rs.getString(4))); // Pulls each column of information from the
            }
            c.close(); // Close connection
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
