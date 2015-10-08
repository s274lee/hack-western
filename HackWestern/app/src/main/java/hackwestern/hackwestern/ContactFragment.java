package hackwestern.hackwestern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract;

import android.widget.AdapterView;
import android.widget.ListView;

public class ContactFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    public final static String Name = "Name";
    public final static String phone_number = "Phone Number";

    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    // Define global mutable variables
    // Define a ListView object
    ListView mContactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    // Empty public constructor, required by the system
    public ContactFragment() {}

    // A UI Fragment must inflate its View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_contact,
                container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Log.v("test","@@@@@@@ In the Fragment");
        // Gets the ListView from the View list of the parent activity
        mContactsList =
                (ListView) getActivity().findViewById(R.id.contact_listView);


//        Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                FROM_COLUMNS,
                TO_IDS,
                0);

        // Initializes the loader
        getLoaderManager().initLoader(0, null, this);

        // Sets the adapter for the ListView
        mContactsList.setAdapter(mCursorAdapter);
//        Log.v("test", android.R.id.text1);


        // Set the item click listener to be the current fragment.
        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
//                Context context = getActivity();
//                TextView c = (TextView) view.findViewById(R.id.text);
//                String playerChanged = c.getText().toString();

                ContentResolver cr = getActivity().getContentResolver();
                Cursor cursor2  = (Cursor)parent.getItemAtPosition(position);
//                String name = cursor2.getString(0);
                String name = cursor2.getString(cursor2.getColumnIndex("display_name"));
                String phoneNumber = new String();
                String _id = cursor2.getString(cursor2.getColumnIndex("_id"));
                String[] args = {_id};
                Log.v("NO ", name);

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            args, null);
                    while (pCur.moveToNext()) {
                        phoneNumber = pCur.getString(pCur.getColumnIndex("data1"));
                 }
                    pCur.close();


               // String args[] = { _id };


//                Cursor c = getActivity().getContentResolver().query(mContactUri,
//                        new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
//                        ContactsContract.Contacts.DISPLAY_NAME + " LIKE ? ",
//                        args,
//                        null);
//                intent.putExtra("Name", )
//                Log.v("test", data);
                // Give the intent to the mainform activity
                Log.v("NO ", phoneNumber);
                Intent intent = new Intent(ContactFragment.this.getActivity(), MessageActivity.class);
                intent.putExtra(Name, name);
                intent.putExtra(phone_number, phoneNumber);
                Log.v("NO ", name);
                Log.v("NO ", phoneNumber);
                startActivityForResult(intent, 1);

                Log.v("Test", _id);
            }

        });

    }

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME
            };

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    // Defines the text expression
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?") +
                    " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = ? ";

    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = {
            mSearchString,
            "1"
    };

    @Override
    public void onItemClick(
            AdapterView<?> parent, View item, int position, long rowID) {
        // Get the Cursor
//        Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
//        // Move to the selected contact
//        cursor.moveToPosition(position);
//        // Get the _ID value
//        mContactId = cursor.getLong(CONTACT_ID_INDEX);
//        // Get the selected LOOKUP KEY
//        mContactKey = cursor.getString(LOOKUP_KEY_INDEX);
//        // Create the contact's content Uri
//        mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);
//
//        Context context = getActivity();
//        CharSequence text = "Hello toast!";
//        int duration = Toast.LENGTH_SHORT;
//
        /*
         * You can use mContactUri as the content URI for retrieving
         * the details for a contact.
         */
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        mSelectionArgs[0] = "%";
        // Starts the query
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null
        );
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Put the result Cursor in the adapter for the ListView

        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor

        mCursorAdapter.swapCursor(null);

    }

}