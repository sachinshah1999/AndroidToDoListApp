package ss01703.cw1.com1032;

/**
 * This activity is used to update the name, due time and due name of an activity.
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
import es.dmoral.toasty.Toasty;

public class EditTask extends AppCompatActivity {

    /**--All the fields--**/
    private DatabaseHelper mDatabaseHelper;

    private Switch mSwitch; //Inflating the switch

    private Calendar calendar; //Calender object

    private EditText mEditTextDate; //Due date of the task
    private EditText mEditTextTime; //Due time of the task
    private EditText mEditTextName; //Name of the task

    private TextView mTextViewAt; //At sign
    private TextView mTextViewDayReminder; // Day reminder text
    private TextView mTextViewTimeReminder; // Time reminder text

    private String selectedName; //Name of the task in the list-view
    private String selectedDate; //The due date of the task in the list-view
    private String selectedTime; //The due time of the task in the list-view
    private String currentTime; //The current time
    private String currentDate = DateFormat.getDateInstance().format(new Date()); // The current date

    private int selectedID; //ID of the task

    private boolean switchStatus; //Whether the task initially had a due date and time (i.e switch on or off)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**--All fields/objects are inflated here--**/

        mDatabaseHelper = new DatabaseHelper(EditTask.this);

        calendar = Calendar.getInstance();

        mEditTextName = findViewById(R.id.task_name_edit_text);
        mEditTextTime = findViewById(R.id.time_edit_text);
        mEditTextDate = findViewById(R.id.date_edit_text);

        mTextViewAt = findViewById(R.id.at_sign);
        mTextViewDayReminder = findViewById(R.id.reminder_day_text_view);
        mTextViewTimeReminder = findViewById(R.id.reminder_time_text_view);

        mSwitch = findViewById(R.id.switch_button);

        FloatingActionButton setTask = findViewById(R.id.fab);

        /**--All information received from MainActivity2--**/

        Intent receivedIntent = getIntent();

        selectedID = receivedIntent.getIntExtra("id", -1); //ID of the selected task
        selectedName = receivedIntent.getStringExtra("name"); //Name of the selected task
        selectedDate = receivedIntent.getStringExtra("date"); //Due date of the selected task
        selectedTime = receivedIntent.getStringExtra("time"); //Due time of the selected task

        /**--All fields/object details are set here--**/

        mEditTextName.setText(selectedName); //Set the task name edit text field to selectedName
        mEditTextName.setSelection(mEditTextName.getText().length()); //Set the cursor to the last character of the task name

        mEditTextDate.setGravity(Gravity.CENTER_HORIZONTAL); //Center date

        mEditTextTime.setGravity(Gravity.CENTER_HORIZONTAL); //Center time

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //Define the time display format
        currentTime = sdf.format(new Date());

        /**--All conditions are set here--**/

        /**
         * This tests whether the task had a due date and time.
         */

        switchStatus = !(selectedDate == null && selectedTime == null);

        /**
         * If the task did have a due date
         * Set the edit text field of date to selectedDate,
         * else set the field to the current date.
         */

        if(switchStatus){
            mEditTextDate.setText(selectedDate);
            mTextViewDayReminder.setText("Reminder set for " + selectedDate + ", ");
        } else if (!switchStatus || mEditTextDate.getText().toString() == "") {
            mEditTextDate.setText(currentDate);
            mTextViewDayReminder.setText("Reminder set for " + currentDate + ", ");
        }

        /**
         * If the task did have a due time
         * Set the edit text field of time to selectedTime,
         * else set the field to the current time.
         */

        if(switchStatus){
            mEditTextTime.setText(selectedTime);
            mTextViewTimeReminder.setText(selectedTime);
        } else if(!switchStatus || mEditTextTime.getText().toString() == "") {
            mEditTextTime.setText(currentTime);
            mTextViewTimeReminder.setText(currentTime);
        }

        /**
         * If the task has a due date and time,
         * set the switch to be checked initially, and ensure the necessary input elements are visible.
         * else if the task did not have a due date and time,
         * set the switch to be not checked, and  ensure the necessary input elements remain hidden.
         */

        if(switchStatus){
            mSwitch.setChecked(true);
            mEditTextDate.setVisibility(View.VISIBLE);
            mTextViewAt.setVisibility(View.VISIBLE);
            mEditTextTime.setVisibility(View.VISIBLE);
            mTextViewDayReminder.setVisibility(View.VISIBLE);
            mTextViewTimeReminder.setVisibility(View.VISIBLE);
        } else {
            mSwitch.setChecked(false);
            mEditTextDate.setVisibility(View.INVISIBLE);
            mTextViewAt.setVisibility(View.INVISIBLE);
            mEditTextTime.setVisibility(View.INVISIBLE);
            mTextViewDayReminder.setVisibility(View.INVISIBLE);
            mTextViewTimeReminder.setVisibility(View.INVISIBLE);
        }

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
         * Open date picker dialog when the date edit text is selected.
         * The user will pick the due date of the task, and this date will be displayed in the edit text.
         * This will be used to update/add the due date of a task.
         */

        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDate = new DatePickerDialog(EditTask.this, R.style.DialogTheme,
                        date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //Ensure we cannot pick a past date.
                mDate.show(); //Show the dialog when clicked.
            }
        });

        /**
         * Open time picker dialog when the time edit text is selected.
         * The user will pick the due time of the task, and this time will be displayed in the edit text.
         * This will be used to update/add the due time of a task.
         */

        mEditTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EditTask.this, R.style.DialogTheme, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

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
         * When the floating button (setTask) is pressed, changes in name, due time and due date are detected,
         * and this is updated according in the MainActivity2.
         */

        setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditTask.this, MainActivity.class);

                String name = mEditTextName.getText().toString(); //Current name of the task
                String date = mEditTextDate.getText().toString(); //Current due date of the task
                String time = mEditTextTime.getText().toString(); //Current due time of the task

                /**
                 * If the task initially had a (due date & time) and if the switch has been checked,
                 * then that can only mean we are updating the due date and time of the task.
                 * Call the updateDate and updateTime methods from the DatabaseHelper class.
                 * Also as long as the name of the task isn't empty, the MainActivity2 will be started.
                 */

                if((switchStatus && mSwitch.isChecked())){
                    mDatabaseHelper.updateDate(date, selectedID, selectedDate);
                    mDatabaseHelper.updateTime(time, selectedID, selectedTime);
                } else if (!name.equals("")){
                    Toasty.info(EditTask.this, "Task updated", Toast.LENGTH_SHORT, true).show();
                    startActivity(intent);
                }

                /**
                 * If the name of the task is ever empty,
                 * Use a toast to inform the user,
                 * and don't start the MainActivity2 yet.
                 * else if the name of the task is not empty, then update it, and
                 * start the MainActivity2.
                 */

                if (name.equals("")) {
                    Toasty.warning(EditTask.this, "You must give the task a name",
                            Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.info(EditTask.this, "Task updated", Toast.LENGTH_SHORT, true).show();
                    mDatabaseHelper.updateName(name, selectedID, selectedName);
                    startActivity(intent);
                }

                /**
                 * If the switch is checked & the task did not initially have a due date and time & name of task is not empty
                 * then that can only mean we are adding a due date and time to a task.
                 * Call the addDate and addTime methods from the DatabaseHelper class.
                 */

                if(mSwitch.isChecked() && !switchStatus && !name.equals("")){
                    mDatabaseHelper.addDate(selectedID, name, date);
                    mDatabaseHelper.addTime(selectedID, name, time);
                    Toasty.info(EditTask.this, "Date and time added", Toast.LENGTH_SHORT).show();
                }

                /**
                 * If the switch is not checked and the task initially had a due date and time,
                 * then that can only mean we ar removing the due date and time of a task.
                 * Call the removeData and removeTime methods from the DatabaseHelper class.
                 */

                if(!mSwitch.isChecked() && switchStatus) {
                    mDatabaseHelper.removeDate(selectedID, date);
                    mDatabaseHelper.removeTime(selectedID, time);
                    Toasty.info(EditTask.this, "Date and time removed", Toast.LENGTH_SHORT).show();
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
     * Method to close the EditTask activity.
     * @param view
     */

    public void closeAddTask(View view) {
        finish();
    }
}
