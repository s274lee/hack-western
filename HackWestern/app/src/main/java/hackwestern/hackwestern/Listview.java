package hackwestern.hackwestern;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.maps.model.LatLng;

public class Listview extends ActionBarActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {

    private ListView listview;
    private ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "geofences";
    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;
    private SharedPreferences mSharedPreferences;
    // Buttons for kicking off the process of adding or removing geofences.
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;
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

        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        setButtonsEnabledState();
        populateGeofenceList();

        buildGoogleApiClient();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to GoogleApiClient");
    }


    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @SuppressLint("LongLogTag")
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.d(TAG, "Connection suspended");
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
                        if(Math.abs(m.latitude - latitude) + Math.abs(m.longitude - longitude) <= 0.5) {
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
        values.put(SQLContract.MessageTable.COLUMN_SENT_FLAG, 1);
        values.put(SQLContract.MessageTable.COLUMN_TIME_SENT, creationDateStamp);
        db.update(SQLContract.MessageTable.TABLE_NAME, values, SQLContract.MessageTable._ID + "=?", value);

    }

    private void sendSms(String phone, String message) {
       SmsManager smsManager = SmsManager.getDefault();
       smsManager.sendTextMessage(phone, null, message, null, null);
       Toast.makeText(getApplicationContext(), "Congrats, message sent to "+phone,Toast.LENGTH_LONG).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }


    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.commit();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.TORONTO_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build());
        }
    }

    private void setButtonsEnabledState() {
        if (!mGeofenceList.isEmpty()) {
            if (mGeofencesAdded) {
                mAddGeofencesButton.setEnabled(false);
                mRemoveGeofencesButton.setEnabled(true);
            } else {
                mAddGeofencesButton.setEnabled(true);
                mRemoveGeofencesButton.setEnabled(false);
            }
        } else {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }
}