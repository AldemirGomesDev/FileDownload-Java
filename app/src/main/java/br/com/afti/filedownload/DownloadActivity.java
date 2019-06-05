package br.com.afti.filedownload;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = "FileDownload";

    //BroadcastReceiver que será invocado ao terminar o download
    final BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction() )){
                Log.w(TAG, "onReceive: Abrindo o arquivo "  );
                openFile();
            }
        }
    };

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        // registramos nosso BroadcastReceiver
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        iniciarDownload();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onComplete);
        super.onDestroy();
    }
    /*
     *Abre o arquivo que realizamos o download
     */
    private void openFile(){
        try{
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, "downloadedfile.jpg");

            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/br.com.afti.filedownload");
            startActivity(install);

        }catch (Exception e){
            Log.e(TAG, "openFile: " + e.getMessage() );
            e.printStackTrace();
        }

    }

    public void iniciarDownload(){
        Uri uri = Uri.parse("http://api.androidhive.info/progressdialog/hive.jpg");

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        downloadManager.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downlodad")
                .setDescription("Realizando o download.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "downloadedfile.jpg"));
    }
}
