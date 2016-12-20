package yulia_tue.androidvideodownload;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Yulia on 20-Dec-16.
 */
public class Parser extends AsyncTask<String, String, String> {

    TextView textView;

    public Parser(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... strings) {
        String foundLinks = "";
        String siteUrl = strings[0];
        Document doc = null;
        try {
            doc = Jsoup.connect(siteUrl).get();

            Elements links = doc.select("a[title=\"gorillavid.in\"]");

            for (Element link : links) {
                foundLinks = foundLinks + link.attr("href") + " " ;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundLinks;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.textView.setText(result);
    }

}
