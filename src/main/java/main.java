/**
 *
 */
public class main {
    public static void main(String args[]){
        try {
            ScheduleForm gui = new ScheduleForm();
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("The SQLite java class was not found in the project directory.");
        }
    }
}
