package uniba.tesi.magicwand.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uniba.tesi.magicwand.R;

public class MyViewHolderPath extends RecyclerView.ViewHolder {

    public LinearLayout root;
    public TextView mid;
    public TextView score;
    public TextView wrong;


    public MyViewHolderPath(@NonNull View itemView) {
        super(itemView);
        root=itemView.findViewById(R.id.listplayer);
        mid=(TextView)itemView.findViewById(R.id.textViewid);
        score=(TextView)itemView.findViewById(R.id.textViewscore);
        wrong=(TextView)itemView.findViewById(R.id.textViewwrong);
    }
}
