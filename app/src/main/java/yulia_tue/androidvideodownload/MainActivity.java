package yulia_tue.androidvideodownload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText urlInput = (EditText) findViewById(R.id.url_input);
        final TextView statusText = (TextView) findViewById(R.id.status_text);
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
                CharSequence downloadLink = linksText.getText();
                String link = downloadLink.toString();
                link = link.substring(link.length() - 44);
                Log.d(TAG, "link: " + link);
                String decodedLink = new String(Base64.decode(link, Base64.DEFAULT));
                linksText.setText(decodedLink);
                Log.d(TAG, "decoded link: " + decodedLink);
                APIHandler apiHandler = new APIHandler(statusText, downloadText, downloadButton);
                apiHandler.execute(decodedLink);

            }
        });

    }
}
