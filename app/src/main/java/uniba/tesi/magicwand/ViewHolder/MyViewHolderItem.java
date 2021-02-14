package uniba.tesi.magicwand.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uniba.tesi.magicwand.R;

public class MyViewHolderItem extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView nameSession;
    public Button button;
    public ImageView icon;

    public MyViewHolderItem(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.list_root);
        nameSession = (TextView) itemView.findViewById(R.id.percorso);
        button=(Button) itemView.findViewById(R.id.bt_apri_percorso);
        icon=(ImageView)itemView.findViewById(R.id.icon_percorso);
    }

    public void setNameSession(String string) {
        nameSession.setText(string);
    }



}
