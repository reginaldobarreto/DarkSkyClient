package br.com.barrsoft.darkskyclient.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import br.com.barrsoft.darkskyclient.R;

public class UserAlertBuilder {

    public static void Alert(final Activity activity){

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setIcon(R.drawable.ic_action_warning);
        alert.setTitle(R.string.alertTitle);
        alert.setMessage(R.string.alertMessage);
        alert.setPositiveButton(R.string.alertPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        alert.show();
    }
}
