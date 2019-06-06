package br.com.afti.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.afti.filedownload.R;

public class Dialogs {

    public static void dialogWarning(Context context, String mensagem) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        try{
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.setContentView(R.layout.dialog_warning);
            dialog.setCancelable(false);
            TextView texto = (TextView) dialog.findViewById(R.id.texto);
            texto.setText(mensagem);
            LinearLayout ll_ok_cancel = (LinearLayout) dialog.findViewById(R.id.ok_btn);
            ll_ok_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void dialogError(Context context, String mensagem) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        try{
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(false);
            TextView texto = (TextView) dialog.findViewById(R.id.texto);
            texto.setText(mensagem);
            LinearLayout ll_ok_cancel = (LinearLayout) dialog.findViewById(R.id.ok_btn);
            ll_ok_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
