package ss01703.cw1.com1032;

/**
 * This activity is used to handle adding of tasks.
 */

/**--All the imports--**/
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty; //Toasty library import


public class AddTask extends AppCompatActivity  {

    /**--All the fields--**/
    private DatabaseHelper mDatabaseHelper; //Database object

    private Switch mSwitch; //Inflating the switch

    private Calendar calendar; //Calender object

    private EditText mEditTextDate; //Due date of the task
    private EditText mEditTextTime; //Due time of the task
    private EditText mEditTextName; //Name of the task

    private TextView mTextViewAt; //At sign
    private TextView mTextViewDayReminder; // Text at the bottom to summarize the due date of the task.
    private TextView mTextViewTimeReminder; // Text at the bottom to summarize the due time of the task.

    private String currentDate = DateFormat.getDateInstance().format(new Date()); //The current date
    private String currentTime; //The current time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**--All fields/objects are inflated here--**/

        mDatabaseHelper = new DatabaseHelper(AddTask.this); //Initialising the DataBaseHelper object

        calendar = Calendar.getInstance();

        mEditTextName = findViewById(R.id.task_name_edit_text);
        mEditTextDate = findViewById(R.id.date_edit_text);
        mEditTextTime = findViewById(R.id.time_edit_text);

        mTextViewAt = findViewById(R.id.at_sign);
        mTextViewDayReminder = findViewById(R.id.reminder_day_text_view);
        mTextViewTimeReminder = findViewById(R.id.reminder_time_text_view);

        mSwitch = findViewById(R.id.switch_button);

        FloatingActionButton setTask = findViewById(R.id.fab);


        /**--All fields/object details are set here--**/

        mEditTextDate.setGravity(Gravity.CENTER_HORIZONTAL); //Date is centered
        mEditTextDate.setText(R.string.today); //Initially set the date for today

        mTextViewDayReminder.setText("Reminder set for " + currentDate + ", "); //Set reminder text for today

        mEditTextTime.setGravity(Gravity.CENTER_HORIZONTAL); //Time is centered
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //Define the time display format
        currentTime = sdf.format(new Date());
        mEditTextTime.setText(currentTime);//Set the time initially to the current time.

        mTextViewTimeReminder.setText(currentTime); //Set time reminder for current time.


        /**--DatePickerDialog & TimePickerDialog are defined here--**/

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year); //Year picked
                calendar.set(Calendar.MONTH, monthOfYear); //Month picked
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); //Day picked
                updateDateLabel(); //Call to method, to change date display format.
                mTextViewDayReminder.setText("Reminder set for " + mEditTextDate.getText() + ", "); //update date reminder text
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                calendar.set(Calendar.HOUR_OF_DAY, selectedHour); //Hour picked
                calendar.set(Calendar.MINUTE, selectedMinute); //Minute picked

                /**
                 * Method adds a 0 if the selected minute is less than 10
                 * Defined purely to make the time look better
                 */
                if(selectedMinute < 10){
                    mEditTextTime.setText( selectedHour + ":0" +  selectedMinute);
                } else {
                    mEditTextTime.setText( selectedHour + ":" +  selectedMinute);
                }
                mTextViewTimeReminder.setText(mEditTextTime.getText()); //update time reminder text.
            }
        };


        /**--All Listeners are defined here--**/


        /**
         * If the user wants to set a reminder for the task, they can flip a switch,
         * and the necessary input elements will appear.
         */

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    mEditTextDate.setVisibility(View.VISIBLE);
                    mTextViewAt.setVisibility(View.VISIBLE);
                    mEditTextTime.setVisibility(View.VISIBLE);
                    mTextViewDayReminder.setVisibility(View.VISIBLE);
                    mTextViewTimeReminder.setVisibility(View.VISIBLE);
                } else {
                    mEditTextDate.setVisibility(View.INVISIBLE);
                    mTextViewAt.setVisibility(View.INVISIBLE);
                    mEditTextTime.setVisibility(View.INVISIBLE);
                    mTextViewDayReminder.setVisibility(View.INVISIBLE);
                    mTextViewTimeReminder.setVisibility(View.INVISIBLE);
                }
            }
        });

        /**
         * Open date picker dialog when the date edit text is selected.
         * The user will pick the due date of the task, and this date will be displayed in the edit text.
         */

        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDate = new DatePickerDialog(AddTask.this, R.style.DialogTheme,
                        date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //Ensure we cannot pick a past date.
                mDate.show(); //Show the dialog when clicked
            }
        });

        /**
         * Open time picker dialog when the time edit text is selected.
         * The user will pick the due time of the task, and this time will be displayed in the edit text.
         */

        mEditTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddTask.this, R.style.DialogTheme, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        /**
         * When the floating button (setTask) is clicked the task is created.
         * This task is then displayed in the list-view in the MainActivity2.
         */

        setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = mEditTextName.getText().toString(); //The name of the task
                Intent createTask = new Intent(AddTask.this, MainActivity.class);
                if (mEditTextName.length() == 0){ //Task name cannot be left blank
                    Toasty.warning(AddTask.this, "You must give the task a name",
                            Toast.LENGTH_SHORT, true).show();
                } else {
                    if(mSwitch.isChecked() == true) { //Check whether there is a due date and time.
                        if (mEditTextDate.getText().toString().equals("Today")) {
                            addTask(new Task(taskName, currentDate, mEditTextTime.getText().toString()));
                        } else {
                            addTask(new Task(taskName, mEditTextDate.getText().toString(),
                                    mEditTextTime.getText().toString()));
                        }
                    } else {
                        addTask(new Task(taskName)); //Task where there is no due date and time.
                    }
                    startActivity(createTask); //Go back to the MainActivity2.
                }
            }
        });
    }

    /**
     * Method to change the format of the date displayed.
     * i.e. 12 Mar 2018
     */

    private void updateDateLabel() {
        String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        mEditTextDate.setText(currentDate);
    }

    /**
     * Method to close the AddTask activity.
     * @param view
     */

    public void closeAddTask(View view) {
        finish();
    }

    /**
     * Method to add a task to our database.
     * @param task
     *  The task that is going to be added.
     */

    public void addTask(Task task){
        boolean insertData = mDatabaseHelper.addTask(task); //Call addTask from DateBaseHelper object.

        /**
         * Error checking to ensure the data has been inserted successfully.
         */

        if(insertData){
            Toasty.success(AddTask.this, "Task created", Toast.LENGTH_SHORT, true).show();
        } else {
            //toastMessage("Unable to create task, please try again");
            Toasty.error(AddTask.this, "Unable to create task, please try again", Toast.LENGTH_SHORT,
                    true).show();
        }
    }
}
