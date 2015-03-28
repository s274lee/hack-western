package hackwestern.hackwestern;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Listview extends ActionBarActivity {

    /** Called when the user clicks the list item **/
    public void showMessage(View view) {
        // I give the intent to a different activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        final ListView listview = (ListView) findViewById(R.id.Listview);
        //String[] values = getNames();
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        final ArrayList<String> list = new ArrayList<String>();
        //list.add("Create Textloc");
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
            int position, long id) {
                /*
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
                 */

                // I give the intent to a different activity
                Intent intent = new Intent(Listview.this, MainActivity.class);
                String message = Integer.toString(position);
                //intent.putExtra(EXTRA_MESSAGE, message );
                startActivity(intent);

            }

        });

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }
        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

/*
    // Produces the ArrayList<String> made of existing Textloc names
    public String[] getNames() {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        SQLiteDatabase db = exec.getReadableDatabase();

        String[] results = {
                SQLContract.MessageTable.COLUMN_RECIPIENT
        };

        Cursor cur = db.query(
                SQLContract.MessageTable.TABLE_NAME,
                results,
                null,
                null,
                null,
                null,
                null
        );

        cur.moveToFirst();

        // TODO: change this to handle multiple entries from the database
        String name = cur.getString(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_RECIPIENT));
        ArrayList<String> results = new ArrayList<String>();
        results.add(name);
        return results;
    }
*/
}