package ss01703.cw1.com1032;

/**
 * This class defines the custom adapter, for the list-view in the MainActivity2.
 */

/**--All the imports--**/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

import ss01703.cw1.com1032.Task;

public class TaskAdapter extends ArrayAdapter<Task> implements Filterable {

    /**--All the fields--**/
    ArrayList<Task> tasks = new ArrayList<>(); //Array list of tasks.
    CustomerFilter filter; //Custom filter to implement our search (tasks by name) function.
    ArrayList<Task> filterList; //Array list that contains tasks sorted by the search filter.

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, R.layout.row_layout, tasks);
        this.tasks = tasks;
        this.filterList = tasks;
    }

    /**
     * Override the getView method.
     * @param position - Position item clicked in the list
     * @param convertView - Reference to object that was clicked (i.e, In our case the task object was clicked )
     * @param parent - Reference to the parent list view
     * @return - Row that was updated.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Create an inflater
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /**
         * Inflate a row view with all UI elements for a row
         * Make use of our custom row layout file row_layout.
         */

        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        /**
         * Populate each element of the view.
         * get the reference to the different parts of the view so that their elements can be changed.
         */

        TextView taskName = rowView.findViewById(R.id.taskName);
        taskName.setText(tasks.get(position).getName());

        TextView dueDate = rowView.findViewById(R.id.dueDate);
        dueDate.setText(tasks.get(position).getDueDate());

        TextView dueTime = rowView.findViewById(R.id.dueTime);
        dueTime.setText(tasks.get(position).getDueTime());

        return rowView;
    }

    /**--All methods used to implement the search (tasks by name) function--**/


    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomerFilter();
        }
        return filter;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.indexOf(getItem(position));
    }

    class CustomerFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();

            if(charSequence != null && charSequence.length() > 0){
                charSequence = charSequence.toString().toUpperCase();

                ArrayList<Task> filters = new ArrayList<Task>();

                for(int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).getName().toUpperCase().contains(charSequence)){
                        Task task = new Task(filterList.get(i).getName(), filterList.get(i).getDueDate(),
                                filterList.get(i).getDueTime());
                        filters.add(task);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            tasks = (ArrayList<Task>) results.values;
            notifyDataSetChanged();
        }
    }
}
