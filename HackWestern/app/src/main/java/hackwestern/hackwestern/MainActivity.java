package hackwestern.hackwestern;

import android.content.Intent;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.SQLException;

public class MainActivity extends ActionBarActivity {

    Button buttonSend;
    EditText editTextSms;
    EditText editTextPhone;
    Button contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // Set the text view as the activity layout
        setContentView(R.layout.activity_main);

//        double lat = getLatitude();
//
//        Toast.makeText(getApplicationContext(), String.valueOf(lat),
//                   Toast.LENGTH_LONG).show();
        buttonClick();

        getLocation();

//        try {
//            DbExecutor exec = new DbExecutor(getApplicationContext());
//
//            // Gets the data repository in write mode
//            SQLiteDatabase db = exec.getWritableDatabase();
//
//// Create a new map of values, where column names are the keys
//            ContentValues values = new ContentValues();
//            values.put(SQLContract.MessageTable.COLUMN_RECIPIENT, "Sharon Lee");
//            values.put(SQLContract.MessageTable.COLUMN_PHONE_NUMBER, "6475237244");
//            values.put(SQLContract.MessageTable.COLUMN_LATITUDE, 45);
//            values.put(SQLContract.MessageTable.COLUMN_LONGITUDE, 55);
//            values.put(SQLContract.MessageTable.COLUMN_SENT_FLAG, 0);
//            values.put(SQLContract.MessageTable.COLUMN_TIME_CREATED,"null");
//            values.put(SQLContract.MessageTable.COLUMN_TIME_SENT,"null");
//
//
//// Insert the new row, returning the primary key value of the new row
//            long newRowId;
//            newRowId = db.insert(
//                    SQLContract.MessageTable.TABLE_NAME,
//                    null,
//                    values);
//
//
//
//
//            Toast.makeText(getApplicationContext(), "works?",
//                    Toast.LENGTH_LONG).show();
//
//
//        }
//        catch(SQLException ex) {
//            String message = ex.getMessage();
//            Toast.makeText(getApplicationContext(), ex.getMessage(),
//                    Toast.LENGTH_LONG).show();
//        }
//
        contactButtonClick();

    }


    public double getLatitude() {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        SQLiteDatabase db = exec.getReadableDatabase();

        String[] results = {
                SQLContract.MessageTable.COLUMN_LATITUDE
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

        double lat = cur.getDouble(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_LATITUDE));

        return lat;
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

    public void buttonClick() {
//        buttonSend = (Button) findViewById(R.id.buttonSend);
//        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
//        editTextSms = (EditText) findViewById(R.id.editTextSms);
//
//        buttonSend.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phone = editTextPhone.getText().toString();
//                String message = editTextSms.getText().toString();
//
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(phone, null, message, null,null);
//                Toast.makeText(getApplicationContext(), "Congrats, message send!",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void contactButtonClick() {
        contactButton = (Button) findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ContactActivity.class);
                startActivity(intent);
            }
        });
    }


    public void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener ls = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();

                Toast.makeText(getApplicationContext(),"Location detected. Longitude:"+longitude+" Latitude:"+latitude,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getApplicationContext(),"stat change",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(),"enabled",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(),"disabled",
                        Toast.LENGTH_LONG).show();
            }
        };

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,ls);
    }
}
