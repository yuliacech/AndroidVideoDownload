package yulia_tue.androidvideodownload;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edtUrl = (EditText) findViewById(R.id.editText);
        Button getButton = (Button) findViewById(R.id.button);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siteUrl = edtUrl.getText().toString();
                Context context = getApplicationContext();
                CharSequence text = "You typed: " + siteUrl;
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
                final TextView textView = (TextView)findViewById(R.id.textView2);
                new Parser(textView).execute(siteUrl);


            }
        });
    }
}
