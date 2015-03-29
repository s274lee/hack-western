package hackwestern.hackwestern;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Listview extends ActionBarActivity {

    private ListView listview;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        listview = (ListView) findViewById(R.id.Listview);
        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        */
        list = new ArrayList<String>();
        list.add("Create Textloc");
        ArrayList<String> secondlist = getNames();
        list.addAll(secondlist);
        /*
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        */

//        final StableArrayAdapter adapter = new StableArrayAdapter(this,
//                android.R.layout.simple_list_item_1, list);
//        listview.setAdapter(adapter);

        listview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
            int position, long id) {

                // Give the intent to the textloc form activity
                Intent intent = new Intent(Listview.this, ContactActivity.class);
                startActivity(intent);

            }

        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Probably resumed after adding a Textloc
//        // Must update the list of names and the adapter
//        list = new ArrayList<String>();
//        list.add("Create Textloc");
//        ArrayList<String> secondlist = getNames();
//        list.addAll(secondlist);
//        ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
//    }

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

    // Produces the ArrayList<String> made of existing Textloc names
    public ArrayList<String> getNames() {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        SQLiteDatabase db = exec.getReadableDatabase();

        String[] results = {
                SQLContract.MessageTable.COLUMN_SENT_FLAG
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

        // Create the list to return
        ArrayList<String> namelist = new ArrayList<String>();
        String name = "";

        // Checks if the database table has any rows
//        if (cur.getCount() > 0) {
//            namelist.add("cur has more than 0 rows");
//        } else {
//            namelist.add("cur has 0 rows");
//        }

        // TODO: change this to handle multiple entries from the database
        // Get the sent flag status
        Integer flag = cur.getInt(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_SENT_FLAG));
        if (flag == 0) {
            name += "Pending: ";
        } else if (flag == 1) {
            name += "Sent: ";
        } else {
            name += "Weird: ";
        }

        // Getting the recipient's name
        String[] secondresults = {
                SQLContract.MessageTable.COLUMN_RECIPIENT
        };

        Cursor secondcur = db.query(
                SQLContract.MessageTable.TABLE_NAME,
                secondresults,
                null,
                null,
                null,
                null,
                null
        );

        secondcur.moveToFirst();

        name += secondcur.getString(secondcur.getColumnIndex(SQLContract.MessageTable.COLUMN_RECIPIENT));

        namelist.add(name);
        return namelist;

//        namelist.add("lollilollipop");
//        return namelist;

    }
}