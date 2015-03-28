package hackwestern.hackwestern;

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

public class MainActivity extends ActionBarActivity {

    Button buttonSend;
    EditText editTextSms;
    EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonClick();

        getLocation();
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
        buttonSend = (Button) findViewById(R.id.buttonSend);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextSms = (EditText) findViewById(R.id.editTextSms);

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String message = editTextSms.getText().toString();

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, null,null);
                Toast.makeText(getApplicationContext(), "Congrats, message send!",
                        Toast.LENGTH_LONG).show();
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
