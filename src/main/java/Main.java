/**
 *
 */
public class Main {
    public static void main(String args[]){
        try {
            DatabaseEditor mainDB = new DatabaseEditor("test.db");// todo change to final db name
            mainDB.createDatabase(); // Create the database if it doesn't exist
            mainDB.createTables(); // Create the tables if they don't exist

            ScheduleForm gui = new ScheduleForm(mainDB);
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("The SQLite java class was not found in the project directory.");
        }
    }
}
