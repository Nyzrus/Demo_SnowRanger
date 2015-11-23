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

    Intent cameraIntent,submitIntent;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView gpsString;

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        submitIntent = new Intent(this, Submit.class);

        gpsString = (TextView) findViewById(R.id.gpsString);

        //locationButton = (Button) findViewById(R.id.locationButton);
        //locationDisplay = (TextView) findViewById(R.id.locationTV);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gpsString.setText("Latitude: " + location.getLatitude()
                                    + "\n Longitude: " + location.getLongitude());
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

        //configureButton();


    }

    /*private void configureButton(){
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("adsadsadsadsadas");
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                //locationDisplay.append("check");
            }
        });
    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    public void slideThrough(View v){
        startActivity(cameraIntent);
    }

    public void takePicture(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "test.jpg");
        Uri fileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0){
            switch(resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                        Toast.makeText(this, "successfully saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
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
