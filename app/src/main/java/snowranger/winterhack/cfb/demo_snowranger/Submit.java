package snowranger.winterhack.cfb.demo_snowranger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class Submit extends ActionBarActivity {

    ImageView resultPhoto;
    Bitmap myBitmap;
    Button submitButton;
    Intent splashIntent, homeIntent;
    String imageFilePath;
    EditText submitET;

    TextView submitTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        homeIntent = new Intent(this, Splash.class);
        submitET = (EditText) findViewById(R.id.submitET);

        submitButton = (Button) findViewById(R.id.submitButton);
        resultPhoto = (ImageView) findViewById(R.id.submitImage);
        submitTV = (TextView) findViewById(R.id.submitTV);

        splashIntent = getIntent();
        imageFilePath = splashIntent.getStringExtra("imagePath");

        myBitmap = BitmapFactory.decodeFile(imageFilePath);
        resultPhoto.setImageBitmap(myBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
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

    public void backHome(View v){

        JSONObject jSonObject = new JSONObject();

        try{
            jSonObject.put("description", submitET.getText());
            jSonObject.put("imageString", convertImagetoJSON(myBitmap));
            Toast.makeText(this, "successfully submitted report", Toast.LENGTH_SHORT).show();

            /*

            Send JSON Object up to servers

             */

            startActivity(homeIntent);
        }catch(JSONException ex){
            Toast.makeText(this, "failed to submit report", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    public String convertImagetoJSON(Bitmap bmap){
        final int compressionQuality = 100;
        String encodedImageString;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.PNG, compressionQuality, byteArrayBitmapStream);
        byte[] bArray = byteArrayBitmapStream.toByteArray();
        encodedImageString = Base64.encodeToString(bArray, Base64.DEFAULT);
        return encodedImageString;
    }
}
