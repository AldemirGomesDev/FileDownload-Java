package br.com.afti.filedownload;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import br.com.afti.util.Dialogs;

public class MainActivity extends AppCompatActivity implements TaskCompleted{

    private static final String TAG = "FileDownload";
    private ImageView imgView;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCount = (Button) findViewById(R.id.btnDownload);

        btnCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.etURL);
                new AsyncDownloadFile(MainActivity.this).execute("https://api.androidhive.info/progressdialog/hive.jpg");

            }
        });
        verificarPermissoes();
    }
    

    private void verificarPermissoes() {
        if (!hasPermissions(this, PERMISSIONS)) {
            Log.d(TAG, "Checando permissões");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        } else {
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onTaskComplete(final String result) {
        if (!result.equals("")){ //successfully downloaded
            imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageBitmap(BitmapFactory.decodeFile(result));
            //mAttacher = new PhotoViewAttacher(imgView);

            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_view_image, null);
                    PhotoView photoView = mView.findViewById(R.id.PhotoViewImage);
                    photoView.setImageBitmap(BitmapFactory.decodeFile(result));
                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });

            //mAttacher.update();
            //Dialogs.dialogWarning(this, "Imagem baixada com sucesso!");
        }else{
            Dialogs.dialogError(this, "Falha ao baixar imagem!");
        }
    }


}
