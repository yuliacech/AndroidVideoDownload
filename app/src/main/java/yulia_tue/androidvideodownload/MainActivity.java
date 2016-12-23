package yulia_tue.androidvideodownload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 45766;
    private APIHandler apiHandler;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText urlInput = (EditText) findViewById(R.id.url_input);
        statusText = (TextView) findViewById(R.id.status_text);
        final TextView linksText = (TextView) findViewById(R.id.links_text);
        final TextView downloadText = (TextView) findViewById(R.id.download_text);

        final Button apiButton = (Button)findViewById(R.id.api_button);
        apiButton.setEnabled(false);
        final Button downloadButton = (Button)findViewById(R.id.download_button);
        downloadButton.setEnabled(false);
        final Button getButton = (Button) findViewById(R.id.get_button);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siteUrl = urlInput.getText().toString();
                Parser parser = new Parser(statusText, linksText, apiButton);
                parser.execute(siteUrl);

            }
        });

        apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence foundLink = linksText.getText();
                String foundLinkString = foundLink.toString();
                foundLinkString = foundLinkString.substring(foundLinkString.length() - 44);
                Log.d(TAG, "Found link: " + foundLinkString);
                String decodedLink = new String(Base64.decode(foundLinkString, Base64.DEFAULT));
                linksText.setText(decodedLink);
                Log.d(TAG, "decoded link: " + decodedLink);
                apiHandler = new APIHandler(statusText, downloadText, downloadButton);
                apiHandler.execute(decodedLink);

            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {



                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                } else {

                    download();
                }


            }
        });


    }

    private void download() {

        JSONObject jsonResponse = null;
        try {
            String response = apiHandler.get();
            jsonResponse = new JSONObject(response);
            String downloadLink = jsonResponse.getString("download");
            String title = jsonResponse.getString("title");
            String format = jsonResponse.getString("format");
            Log.d(TAG, "Download link: " + downloadLink);
            Log.d(TAG, "Title: " + title);
            Log.d(TAG, "Format: " + format);
            Downloader downloader = new Downloader(statusText);
            downloader.execute(downloadLink, title, format);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    download();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
