package br.com.afti.filedownload;

import android.graphics.BitmapFactory;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements TaskCompleted {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btnCount = (Button) findViewById(R.id.btnDownload);

        btnCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.etURL);
                new AsyncDownloadFile(Main2Activity.this).execute("http://api.androidhive.info/progressdialog/hive.jpg");

            }
        });
    }

    @Override
    public void onTaskComplete(String result) {
        Log.w("FileDownload", "onTaskComplete:" + result );
        if (!result.equals("")){ //successfully downloaded
            ImageView imgView;
            imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageBitmap(BitmapFactory.decodeFile(result));
        }else{
            Toast.makeText(this,"Fail to download image",Toast.LENGTH_LONG).show();
        }
    }
}
