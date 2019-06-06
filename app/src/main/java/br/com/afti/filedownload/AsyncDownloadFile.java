package br.com.afti.filedownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class AsyncDownloadFile  extends AsyncTask <String, String, String> {
    private Context mContext;

    ProgressDialog mProgress;
    private TaskCompleted mCallback;

    public AsyncDownloadFile(Context context){
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Baixando por favor espere...");
        mProgress.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgress.setMessage(values[0]);
    }

    @Override
    protected String doInBackground(String... values) {
        String sResult="";
        try {

            String sFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/instinctcoder/downloadfiles";
            String localFilename = sFolder  + "/imagem1.jpg";
            File img = new File(localFilename);
            // Create directories, make sure exists
            new File(sFolder ).mkdirs();

            try {
                URL imageUrl = null;
                imageUrl = new URL(values[0]);

                HttpsURLConnection urlConnection = (HttpsURLConnection)imageUrl.openConnection();
                urlConnection.connect();
                int file_size = urlConnection.getContentLength()/1000;

                InputStream in =new BufferedInputStream(imageUrl.openStream(), 8192);
                int total = 0;
                OutputStream out = new BufferedOutputStream(new FileOutputStream(img));
                byte data[] = new byte[1024];
                for (int b; (b = in.read(data)) != -1;) {
                    total += b/1000;
                    out.write(data, 0, b);
                    publishProgress(Integer.toString(total) + " de " + Integer.toString(file_size) + " KB Baixados.");
                }

                out.close();
                in.close();
                sResult =localFilename;
            } catch (MalformedURLException e) {
                img = null;
                Log.e("saveImage",e.getMessage());
            } catch (IOException e) {
                img = null;
                Log.e("saveImage",e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sResult;

    }

    @Override
    protected void onPostExecute(String results) {
        mProgress.dismiss();
        mCallback.onTaskComplete(results);
    }
}
