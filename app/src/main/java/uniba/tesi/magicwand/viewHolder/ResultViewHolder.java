package uniba.tesi.magicwand.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uniba.tesi.magicwand.R;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;

    public ResultViewHolder(View itemView) {
        super(itemView);
        mTextView1 = itemView.findViewById(R.id.textViewid);
        mTextView2 = itemView.findViewById(R.id.textViewscore);
        mTextView3 = itemView.findViewById(R.id.textViewwrong);
    }


}