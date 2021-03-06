package com.devel.ccqf.ccqfmisson.Pub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devel.ccqf.ccqfmisson.R;

/**
 * Created by jo on 5/31/16.
 */
public class DialogRep extends Dialog {
    private Commanditaire com = null;
    private ImageView pubLayout;

    public DialogRep(Context c){
        super(c);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pub);
        setCancelable(false);
        pubLayout = (ImageView)findViewById(R.id.pubLayout);

        final Button btnDismiss = (Button)findViewById(R.id.btnDialogPubDismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(com != null){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(com.getUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getContext().startActivity(intent);
                }
            }
        });

    }

    public void dialogPub(Context c){
        show();
    }

    public void setPub(Commanditaire pub){
        com = pub;

        pubLayout.setImageDrawable(Drawable.createFromPath(pub.getFilePath()));
    }

}
