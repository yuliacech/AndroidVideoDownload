package yulia_tue.androidvideodownload;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Yulia on 20-Dec-16.
 */
public class Parser extends AsyncTask<String, String, String[]> {

    TextView status;
    TextView links;
    Button apiButton;

    public Parser(TextView status, TextView links, Button apiButton) {
        this.status = status;
        this.links = links;
        this.apiButton = apiButton;

    }

    @Override
    protected String[] doInBackground(String... strings) {

        String siteUrl = strings[0];
        Document doc = null;
        String[] foundLinks = null;
        try {
            doc = Jsoup.connect(siteUrl).get();

            Elements as = doc.select("a[title=\"gorillavid.in\"]");
            foundLinks = new String[as.size()];
            int index = 0;
            for (Element a : as) {
                foundLinks[index] = a.attr("href");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundLinks;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        this.status.setText("Found " + result.length + " links ");
        this.links.setText(result[0]);
        this.apiButton.setEnabled(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.status.setText("Parsing the page...");
    }
}
