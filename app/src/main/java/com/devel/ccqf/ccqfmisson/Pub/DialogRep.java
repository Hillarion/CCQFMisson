package com.devel.ccqf.ccqfmisson.Pub;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.devel.ccqf.ccqfmisson.R;

/**
 * Created by jo on 5/31/16.
 */
public class DialogRep {

    public void dialogPub(Context c){
        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_pub);

        d.setCancelable(false);
        final Button btnDismiss = (Button)d.findViewById(R.id.btnDialogPubDismiss);
        d.show();

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }
}
