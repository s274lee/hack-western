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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // Set the text view as the activity layout
        setContentView(R.layout.activity_main);
        scheduleMessageListener();

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
            }
        });
    }

    private void saveMessage() {
        repName = (TextView) findViewById(R.id.repName);
        repNumber = (TextView) findViewById(R.id.repNumber);
        editTextSms = (EditText) findViewById(R.id.editTextSms);
        //TODO: get updated longitude and latitude values at this point

        try {

            DbExecutor exec = new DbExecutor(getApplicationContext());

            // Gets the data repository in write mode
            SQLiteDatabase db = exec.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            String recipientName = repName.getText().toString();
            String recipientNumber = repNumber.getText().toString();
            String message = editTextSms.getText().toString();

            Date currentDate = new Date();
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String creationDateStamp = format.format(currentDate);

            ContentValues values = new ContentValues();
            values.put(SQLContract.MessageTable.COLUMN_RECIPIENT, "Hamrit");
            values.put(SQLContract.MessageTable.COLUMN_PHONE_NUMBER, "6475237244");
            values.put(SQLContract.MessageTable.COLUMN_MESSAGE, message);
            values.put(SQLContract.MessageTable.COLUMN_LATITUDE, 45); //TODO: get actual longitude and latitude values
            values.put(SQLContract.MessageTable.COLUMN_LONGITUDE, 55);
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
