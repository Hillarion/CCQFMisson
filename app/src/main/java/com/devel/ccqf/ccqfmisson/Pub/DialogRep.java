package com.devel.ccqf.ccqfmisson.Pub;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.devel.ccqf.ccqfmisson.R;

/**
 * Created by jo on 5/31/16.
 */
public class DialogRep extends Dialog implements DialogInterface.OnClickListener {
    private Commenditaire com = null;
    private LinearLayout pubLayout;

    public DialogRep(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pub);
        setCancelable(false);
        pubLayout = (LinearLayout)findViewById(R.id.pubLayout);

        final Button btnDismiss = (Button)findViewById(R.id.btnDialogPubDismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void dialogPub(Context c){

/*        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_pub);

        d.setCancelable(false);
        final Button btnDismiss = (Button)d.findViewById(R.id.btnDialogPubDismiss);
        d.show();*/
        show();

/*        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });*/
    }

    public void setPub(Commenditaire pub){
        com = pub;

//        pubLayout.setBackground(pub.getFilePath());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(com != null){
/*            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.(com.getUrl()), "application/html");;
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            getContext().startActivity(intent);*/
        }
    }
}
