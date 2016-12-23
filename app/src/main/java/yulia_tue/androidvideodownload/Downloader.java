package yulia_tue.androidvideodownload;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Yulia on 21-Dec-16.
 */
public class Downloader extends AsyncTask<String, String, String> {
    private static final String TAG = "Downloader";

    private TextView status;
    public Downloader(TextView status) {
        this.status = status;
    }
    @Override
    protected String doInBackground(String... strings) {
        String filename = "";
        int count;
        try {
            String title = strings[1];
            String format = strings[2];

            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, title + "." + format);

            URL url = new URL(strings[0]);

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            filename = file.getAbsolutePath();
            Log.d(TAG, "Downloading to " + filename);
            // Output stream to write file

            OutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return filename;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.status.setText("Downloaded to " + result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.status.setText("Starting download...");
    }
}
