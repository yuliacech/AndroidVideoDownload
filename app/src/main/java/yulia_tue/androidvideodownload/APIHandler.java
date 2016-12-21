package yulia_tue.androidvideodownload;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;


/**
 * Created by Yulia on 21-Dec-16.
 */
public class APIHandler extends AsyncTask<String, String, String>{
    private static final String TAG = "APIHandler";

    private TextView status;
    private TextView download;
    private Button downloadButton;

    public APIHandler(TextView status, TextView download, Button downloadButton) {
        this.status = status;
        this.download = download;
        this.downloadButton = downloadButton;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url = null;
        HttpURLConnection connection = null;
        try {
            String urlParameters =
                    "key=" + URLEncoder.encode(BuildConfig.API_KEY, "UTF-8") +
                            "&url=" + URLEncoder.encode(strings[0], "UTF-8") +
                            "&force=" + URLEncoder.encode("download", "UTF-8") +
                            "&secure=" + URLEncoder.encode("true", "UTF-8");

            url = new URL("https://api.video-download.online/v1/download/create");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d(TAG, "response: " + response.toString());

            JSONObject jsonResponse = new JSONObject(response.toString());
            String downloadLink = jsonResponse.getString("download");
            return downloadLink;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.status.setText("APIHandler done ");
        this.download.setText(result);
        this.downloadButton.setEnabled(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.status.setText("Connecting to API...");
    }
}
