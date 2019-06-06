package br.com.afti.filedownload;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FileDownload";
    // button to show progress dialog
    Button btnShowProgress;

    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private final int PERMISSAO_REQUEST = 1;

    // File url to download
    private static String file_url = "https://api.androidhive.info/progressdialog/hive.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show progress bar button
        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
        // Image view to show image after downloading
        my_image = (ImageView) findViewById(R.id.my_image);
        /**
         * Show Progress bar click event
         * */
        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task
                new DownloadFileFromURL().execute(file_url);
//                startActivity(new Intent(MainActiv    ity.this, DownloadActivity.class));
//                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        //USUARIA DAR A PERMISSAO PARA LER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

        //USUARIA DAR A PERMISSAO PARA ESCREVER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            StrictMode.ThreadPolicy vPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(vPolicy);

            InputStream inStream = null;
            Bitmap pBitmap = null;
            try {
                Log.w(TAG, "buscando imagem... " + f_url[0]);

                URL url = new URL(f_url[0]);
                HttpsURLConnection pConnection = (HttpsURLConnection)url.openConnection();
//                pConnection.setDoInput(true);
//                pConnection.connect();
                pConnection.setInstanceFollowRedirects(false);
                inStream = pConnection.getInputStream();
                    pBitmap = BitmapFactory.decodeStream(inStream);

                    Log.w(TAG, "HttpURLConnection ok " + pConnection.getResponseMessage());
                if(pConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Log.w(TAG, "HttpURLConnection ok ");

//                    inStream.close();

                }

                getDirFromSDCard();

                File folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/TollCulator");
                if (!folder.exists()) {
                    folder.mkdir();
                    Log.w(TAG, "pasta criada ");
                }

                File file = new File(Environment.getExternalStorageDirectory() + "/imgsApp");
                file.mkdir();
                File ifile= new File(Environment.getExternalStorageDirectory() + "/imgsApp/", "imagem1.jpg");

                FileOutputStream output = new FileOutputStream(ifile);
                pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

                // flushing output
                output.flush();
                output.close();
                inStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            Log.w(TAG, "onPostExecute: " + file_url );
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/downloadedfile-1.jpg";
            // setting downloaded into image view
            Log.w(TAG, "ImagePath: " + imagePath );
            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }

    private File getDirFromSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            File dir = new File(sdcard, "FileDownload" + File.separator + "PASTA_1");
            if (!dir.exists())
                dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }
}
