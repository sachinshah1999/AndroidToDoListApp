package ss01703.cw1.com1032;

/**
 * This activity displays all the tasks and relevant functionality.
 */

/**--All the imports--**/
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty; //Allows for custom toast messages.

public class MainActivity extends AppCompatActivity {

    /**--All the fields--**/
    private DatabaseHelper mDatabaseHelper; //Database object

    private static final String TAG = "MainActivity"; //Used in Log.d

    private ListView taskList; //The list-view

    private TaskAdapter taskAdapter; //Task adapter.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**--All fields/objects are inflated here--**/

        FloatingActionButton addTaskButton = findViewById(R.id.fab);

        mDatabaseHelper = new DatabaseHelper(this);

        taskList = findViewById(R.id.taskList);

        /**--All fields/object details are set here--**/

        getSupportActionBar().setTitle(R.string.actionBarTitle); //Set the title of the action bar

        /**--All Listeners are defined here--**/

        /**
         * When a task is clicked on, the EditTask activity is started.
         * This activity allows the user to change the name, add, remove or change, due date and time.
         */

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /**
                 * We need to get the details of the task we clicked on,
                 * so we can reflect these details in the appropriate places in EditTask.
                 */
                Task task = taskAdapter.getItem(i);
                String taskName = task.getName();
                String dueDate = task.getDueDate();
                String dueTime = task.getDueTime();
                int taskID = task.getId();

                Log.d(TAG, "onItemClick: You Clicked On: " + taskName); //Log to indicate that you have clicked on the task

                /**
                 * The code below first determine whether the task exists or not.
                 * A log is made made of the task ID.
                 * An Intent is declared so we can switch to EditTask activity.
                 * Details of the task, which are to be passed are added through the putExtra method.
                 * If the task had previously had its due date & time removed, pass (String) null for getDueDate() & getDueTime()
                 * else if the task initially had no due date & time, i.e. null, pass them on anyway.
                 * else if the task initially had a due date & time, pass the values.
                 * THEN start the EditTask activity.
                 * ELSE the task doesn't exists, and display the appropriate toast.
                 */

                if(taskID > -1){ //If task exists
                    Log.d(TAG, "onItemClick: The ID is " + taskID); //Log to indicate the ID of the task clicked
                    Intent editTask = new Intent(MainActivity.this, EditTask.class);
                        editTask.putExtra("id", taskID);
                        editTask.putExtra("name", taskName);
                        if(dueDate != null && dueDate.equals("") && dueDate != null && dueTime.equals("")){
                            editTask.putExtra("date", (String)null);
                            editTask.putExtra("time", (String)null);
                        } else if(dueDate == null) {
                            editTask.putExtra("date", dueDate);
                            editTask.putExtra("time", dueTime);
                        } else {
                            editTask.putExtra("date", dueDate);
                            editTask.putExtra("time", dueTime);
                        }
                        startActivity(editTask);
                } else {
                    Toasty.error(MainActivity.this, "No ID associated with that task", Toast.LENGTH_SHORT,
                            true).show();
                }
            }
        });

        /**
         * If a task is clicked for long enough then the task is deleted.
         */

        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                /**
                 * Get the name and ID of the task you are clicking on.
                 */

                Task task = taskAdapter.getItem(i);
                String taskName = task.getName();
                int taskID = task.getId();

                /**
                 * Call the deleteTask method from the DatabaseHelper class.
                 */

                mDatabaseHelper.deleteTask(taskID, taskName);

                /**
                 * Once deleted from the table,
                 * delete from the list-view and reflect the change by calling
                 * notifyDataSetChanged(), on the taskAdapter.
                 */

                taskAdapter.remove(task);
                taskAdapter.notifyDataSetChanged();

                /**
                 * Display a toast, so the user is informed that the task has been deleted.
                 */

                Toasty.error(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT, true).show();
                return false;
            }
        });

        /**
         * When the floating button (addTaskButton) is clicked on, the AddTask activity is started.
         * Where the user can create new tasks.
         */

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createTask = new Intent(MainActivity.this, AddTask.class);
                startActivity(createTask);
            }
        });

        populateListView(); //Method declared below
    }

    /**
     * This method is called above.
     * Used to populate the list view with tasks using a custom taskAdapter.
     * Calls the getAllTasks() method from the DatabaseHelper class.
     * Which returns an array list of task objects.
     */

    private void populateListView() {
        taskAdapter = new TaskAdapter(this, mDatabaseHelper.getAllTasks());
        taskList.setAdapter(taskAdapter);
    }

    /**
     * Method implements the search feature in the action bar.
     * Used to search for tasks, by name.
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                taskAdapter.getFilter().filter(s); //Call the overridden getFilter() method.
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
    }

