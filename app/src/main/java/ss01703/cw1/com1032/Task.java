package ss01703.cw1.com1032;

/**
 * This is the task class.
 * Where task objects are defined.
 *
 */

public class Task {

    private int id; //ID of the task
    private String name; //Name of the task
    private String dueDate; //Due date of the task
    private String dueTime; //Due time of the task

    /**--Default and parameterised constructors--**/

    public Task() {
    }

    public Task(String name){
        this.name = name;
    }


    public Task (String name, String dueDate, String dueTime){
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    /**--Getters--**/

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public String getDueTime() {
        return this.dueTime;
    }

    /**--Setters--**/

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public void setId(int id){
        this.id = id;
    }
}
