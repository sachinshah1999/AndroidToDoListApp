package ss01703.cw1.com1032;

/**
 * This activity is used to handle the storing, retrieving, and updating of data, associated with each task.
 */

/**--All the imports--**/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**--All the fields--**/
    private static final String TABLE_NAME = "tasks_table"; //Table name
    private static final String COL0 = "ID"; //Column 0 is the ID of the task
    private static final String COL1 = "Name"; //Column 1 is the name of the task
    private static final String COL2 = "Date";// Column 2 is the due date of the task
    private static final String COL3 = "Time";// Column 3 is the due time of the task
    private static final String TAG = "DatabaseHelper"; //TAG use in Log.d

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    /**
     * Table is created in the onCreate method.
     * COL 0 which is the ID of each task is the primary key.
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
        + COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL1 + " TEXT,"
                + COL2 + " TEXT," + COL3 + " TEXT" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method to return all the data from the table.
     * @return
     */

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    /**
     * Method adds the details of a task in the appropriate columns.
     * @param task
     */

    public boolean addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL1, task.getName()); //Task name
        values.put(COL2, task.getDueDate()); //Task due-date
        values.put(COL3, task.getDueTime()); //Task due-time

        /**
         * Logs to indicate if the data has been inserted into the table.
         */

        Log.d(TAG, "addData : Adding " + task.getName() + " to " + TABLE_NAME);
        Log.d(TAG, "addData : Adding " + task.getDueDate() + " to " + TABLE_NAME);
        Log.d(TAG, "addData : Adding " + task.getDueTime() + " to " + TABLE_NAME);

        /**
         * Error checking to ensure the data has indeed been inserted into the table.
         */

        long result = db.insert(TABLE_NAME, null, values);
        db.close(); //close db connection.

        return result != -1;
    }

    /**
     * Method to delete a task from the table, by deleting the row.
     * @param id - ID of the task, used to identify the row
     * @param name - The name of the task, used to identify the row.
     */

    public void deleteTask(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL0 + " = '" + id + "'" +
                " AND " + COL1 + " = '" + name + "'";

        /**
         * Logs to indicate if the task has been deleted.
         */

        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to update the name of a task.
     * @param newName - New name of the task
     * @param id - ID of the task, used to identify the row
     * @param oldName - Old name of the task, used to identify the row
     */


    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL1 +
                " = '" + newName + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL1 + " = '" + oldName + "'";

        /**
         * Logs to indicate if the task name has been updated.
         */

        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: setting name to " + newName);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to update the due date of a task.
     * @param newDate - New due date of the task
     * @param id - ID of the task, used to identify the row.
     * @param oldDate - Old due date of the task, used to identify the row.
     */

    public void updateDate(String newDate, int id, String oldDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newDate + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL2 + " = '" + oldDate + "'";

        /**
         * Logs to indicate if the task due date has been updated.
         */

        Log.d(TAG, "updateDate: query: " + query);
        Log.d(TAG, "updateDate: setting date to " + newDate);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to update the due time of the task
     * @param newTime - New due time of the task
     * @param id - ID of the task, used to identify the row.
     * @param oldTime - Old due time of the task, used to identify the row.
     */

    public void updateTime(String newTime, int id, String oldTime){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newTime + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL3 + " = '" + oldTime + "'";

        /**
         * Logs to indicate if the task due time has been updated.
         */
        Log.d(TAG, "updateTime: query: " + query);
        Log.d(TAG, "updateTime: setting time to " + newTime);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to remove the due date of a task.
     * @param id - ID of the task, used to identify the row.
     * @param date - due date of the task, used to identify the row.
     */

    public void removeDate(int id, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + "" + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL2 + " = '" + date + "'";

        /**
         * Log to indicate if the task due date has been removed.
         */
        Log.d(TAG, "DeleteTime: query: " + query);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to remove the due time of the task.
     * @param id - ID of the task, used to identify the row.
     * @param time - Due time of the task, used to identify the row.
     */

    public void removeTime(int id, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + "" + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL3 + " = '" + time + "'";
        /**
         * Log to indicate if the task due time has been removed.
         */

        Log.d(TAG, "DeleteTime: query: " + query);

        db.execSQL(query);
    }

    /**
     * Method to add a due date to a task.
     * @param id - The ID of the task.
     * @param name - The name of the task.
     * @param date - The due date of the task.
     */

    public void addDate(int id, String name, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + date + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL1 + " = '" + name + "'";

        /**
         * Logs to indicate if the task due date has been added.
         */

        Log.d(TAG, "AddDate: query: " + query);
        Log.d(TAG, "AddDate: setting date to " + date);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to add a due time to a task.
     * @param id - The ID of the task.
     * @param name - The name of the task.
     * @param time - The due time of the task.
     */

    public void addTime(int id, String name, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + time + "' WHERE " + COL0 + "= '" + id + "'" +
                " AND " + COL1 + " = '" + name + "'";

        /**
         * Logs to indicate if the task due time has been added.
         */
        Log.d(TAG, "AddTime: query: " + query);
        Log.d(TAG, "AddTime: setting time to " + time);

        db.execSQL(query);
        db.close();
    }

    /**
     * Method to get all the tasks in the table, and store them in an array-list.
     * @return - Returns the list of tasks, which will be used in MainActivity2 to populate the list-view.
     */

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME; //Get all the data from the table.

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDueDate(cursor.getString(2));
                task.setDueTime(cursor.getString(3));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return taskList;

    }

}
