package snowranger.winterhack.cfb.demo_snowranger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;


public class Splash extends ActionBarActivity {

    //
    Intent cameraIntent,submitIntent;


    //For GPS coordinating
    private LocationManager locationManager;
    private LocationListener locationListener;

    //to hold the gps coordinates
    private TextView gpsString;

    //to store the image File and pass it between activities
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Intent to start the Activity for Submitting
        submitIntent = new Intent(this, Submit.class);

        gpsString = (TextView) findViewById(R.id.gpsString);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Loaction Listener/Manager handles GPS changes and recording
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gpsString.setText("Latitude: " + location.getLatitude()
                                    + "\n Longitude: " + location.getLongitude());
                submitIntent.putExtra("latitude", location.getLatitude());
                submitIntent.putExtra("longitude", location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };



    }

    //Default method
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    //Default method
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

    //Opens new intent for use of camera
    //Starts new activity after camera is selected
    public void takePicture(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "test.jpg");
        Uri fileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        startActivityForResult(intent, 0);
    }

    //Pending result code received, opens to Submit.java and saves the image file at a local space
    //Also gives absolute path to said image file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0){
            switch(resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                        Toast.makeText(this, "successfully saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        submitIntent.putExtra("imagePath", imageFile.getAbsolutePath());
                        startActivity(submitIntent);
                    }else{
                        Toast.makeText(this, "no success", Toast.LENGTH_LONG).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:

                    break;
                default:

                    break;
            }
        }
    }
}
