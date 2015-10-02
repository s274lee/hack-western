package hackwestern.hackwestern;

import android.content.Intent;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.SQLException;
import java.text.SimpleDateFormat;
import 	java.util.Date;
import java.text.DateFormat;

public class MainActivity extends ActionBarActivity {


    TextView repName; //change to just be the recipient name
    TextView repNumber; //change to just be the recipient number
    EditText editTextSms; //message to be sent
    Button buttonSend; //Schedule message btn
    //TODO: define longitude and latitude elements from the UI
    Button contactButton;
    String name;
    String phone_number;
    String lat;
    String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        name = intent.getStringExtra(ContactFragment.Name);
        phone_number = intent.getStringExtra(ContactFragment.phone_number);

        // Set the text view as the activity layout
        setContentView(R.layout.activity_main);

       // repName = (TextView) findViewById(R.id.repName);
      //  repName.setText(name);
        repNumber = (TextView) findViewById(R.id.repNumber);
        repNumber.setText(phone_number);
        scheduleMessageListener();
        mapButtonClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scheduleMessageListener() {
        buttonSend = (Button) findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessage();
                finish();
            }
        });
    }


//    public void contactButtonClick() {
//        contactButton = (Button) findViewById(R.id.contactButton);
//        contactButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ContactActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    public void mapButtonClick() {
        contactButton = (Button) findViewById(R.id.buttonMap);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapActivity.class);
                startActivityForResult(intent,1);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                lng = data.getStringExtra("longitude");
                lat = data.getStringExtra("latitude");
                // use 'myValue' return value here
            }
        }
    }

    public void saveMessage() {

        editTextSms = (EditText) findViewById(R.id.editTextSms);

        try {
            DbExecutor exec = new DbExecutor(getApplicationContext());

            // Gets the data repository in write mode
            SQLiteDatabase db = exec.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            String recipientName = name;
            String recipientNumber = phone_number;
            String message = editTextSms.getText().toString();

            Date currentDate = new Date();
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String creationDateStamp = format.format(currentDate);

            ContentValues values = new ContentValues();
            values.put(SQLContract.MessageTable.COLUMN_RECIPIENT, recipientName);
            values.put(SQLContract.MessageTable.COLUMN_PHONE_NUMBER, recipientNumber);
            values.put(SQLContract.MessageTable.COLUMN_MESSAGE, message);
            values.put(SQLContract.MessageTable.COLUMN_LATITUDE, lat); //TODO: get actual longitude and latitude values
            values.put(SQLContract.MessageTable.COLUMN_LONGITUDE, lng);
            values.put(SQLContract.MessageTable.COLUMN_SENT_FLAG, 0);
            values.put(SQLContract.MessageTable.COLUMN_TIME_CREATED,creationDateStamp);
            values.put(SQLContract.MessageTable.COLUMN_TIME_SENT,"null");

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    SQLContract.MessageTable.TABLE_NAME,
                    null,
                    values);

            Toast.makeText(getApplicationContext(), "Message successfully scheduled!"+message,
                    Toast.LENGTH_LONG).show();

        }
        catch(SQLException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }


}
