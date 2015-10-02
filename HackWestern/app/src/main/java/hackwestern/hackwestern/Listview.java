package hackwestern.hackwestern;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Listview extends ActionBarActivity {

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        View buttonView = findViewById(R.id.createtextloc);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Give the intent to the textloc form activity
                Intent intent = new Intent(Listview.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        buildDatabase();
        sendMessages();

        listview = (ListView) findViewById(R.id.Listview);

        ArrayList<String> messages = getNames();

        listview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
            int position, long id) {

                // Give the intent to the textloc form activity
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
            }
        });

    }

    public void buildDatabase() {
        try {
            DbExecutor exec = new DbExecutor(getApplicationContext());

            Toast.makeText(getApplicationContext(), "Database built!", Toast.LENGTH_LONG).show();

        }
        catch(SQLException ex) {
            String message = ex.getMessage();
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }



    // Produces the ArrayList<String> made of existing Textloc names
    public ArrayList<String> getNames() {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        SQLiteDatabase db = exec.getReadableDatabase();

        String[] results = {
                SQLContract.MessageTable.COLUMN_SENT_FLAG,
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
        ArrayList<String> messages = new ArrayList<String>();
        while(cur.moveToNext()) {

            String recipient = cur.getString(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_RECIPIENT));
            int flag = cur.getInt(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_SENT_FLAG));
            if (flag == 0) {
                messages.add( "Pending: " + recipient);

            } else {
                messages.add("Sent: " + recipient);
            }

        }
      return messages;

    }

    public void sendMessages() {

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            LocationListener ls = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                   Double longitude = location.getLongitude();
                   Double latitude = location.getLatitude();

                    ArrayList<Message> messages = getMessages();

                    for(int i=0;i<messages.size();i++) {
                        Message m = messages.get(i);
                        if(Math.abs(m.latitude - latitude) + Math.abs(m.longitude - longitude) <= 3) {
                            setMessageToSent(m.id);
                            sendSms(m.phoneNumber, m.messageText);
                            Toast.makeText(getApplicationContext(),"Message sent to"+m.recipient,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Toast.makeText(getApplicationContext(),"stat change",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(getApplicationContext(),"ProviderEnabled",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(getApplicationContext(),"ProviderDisabled",
                            Toast.LENGTH_LONG).show();
                }
            };

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,ls);

    }

    public ArrayList<Message> getMessages() {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        SQLiteDatabase db = exec.getReadableDatabase();

        String[] results = {
                SQLContract.MessageTable._ID,
                SQLContract.MessageTable.COLUMN_RECIPIENT,
                SQLContract.MessageTable.COLUMN_PHONE_NUMBER,
                SQLContract.MessageTable.COLUMN_MESSAGE,
                SQLContract.MessageTable.COLUMN_LONGITUDE,
                SQLContract.MessageTable.COLUMN_LATITUDE
        };

        Cursor cur = db.query(
                SQLContract.MessageTable.TABLE_NAME,
                results,
                SQLContract.MessageTable.COLUMN_SENT_FLAG +"=0",
                null,
                null,
                null,
                null
        );

        ArrayList<Message> messages = new ArrayList<Message>();

        cur.moveToFirst();

        for (int i = 0; i< cur.getCount(); i++) {
            Message m = new Message();
            m.id = cur.getInt(cur.getColumnIndex(SQLContract.MessageTable._ID));
            m.recipient = cur.getString(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_RECIPIENT));
            m.phoneNumber = cur.getString(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_PHONE_NUMBER));
            m.messageText = cur.getString(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_MESSAGE));
            m.longitude = cur.getDouble(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_LONGITUDE));
            m.latitude = cur.getDouble(cur.getColumnIndex(SQLContract.MessageTable.COLUMN_LATITUDE));
            messages.add(m);
            cur.moveToNext();
        }
        return messages;
    }

    private void setMessageToSent(int messageId) {
        DbExecutor exec = new DbExecutor(getApplicationContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = exec.getWritableDatabase();

        Date currentDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String creationDateStamp = format.format(currentDate);
        String[] value = { String.valueOf(messageId) };

        ContentValues values = new ContentValues();
        values.put(SQLContract.MessageTable.COLUMN_SENT_FLAG,1);
        values.put(SQLContract.MessageTable.COLUMN_TIME_SENT,creationDateStamp);
        db.update(SQLContract.MessageTable.TABLE_NAME, values, SQLContract.MessageTable._ID +"=?", value);

    }

    private void sendSms(String phone, String message) {
       SmsManager smsManager = SmsManager.getDefault();
       smsManager.sendTextMessage(phone, null, message, null,null);
       Toast.makeText(getApplicationContext(), "Congrats, message sent to "+phone,Toast.LENGTH_LONG).show();
    }
}