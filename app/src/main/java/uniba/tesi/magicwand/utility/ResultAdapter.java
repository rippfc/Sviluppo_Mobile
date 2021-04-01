package uniba.tesi.magicwand.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uniba.tesi.magicwand.model.Player;
import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.viewHolder.ResultViewHolder;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
    public ArrayList<Player> playerArrayList;
    private final Context mContext;

    public ResultAdapter(ArrayList<Player> list, Context mContext) {
        playerArrayList = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item,parent,false);
        return new ResultViewHolder(v);
    }
    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        final Player currentItem = playerArrayList.get(position);
        final int[] colors = mContext.getResources().getIntArray(R.array.paint);
        holder.mTextView1.setText(mContext.getString(R.string.player)+" "+String.valueOf(currentItem.getId()+1));
        holder.mTextView1.setTextColor(colors[position]);
        holder.mTextView2.setText(String.valueOf(currentItem.getScore()));
        holder.mTextView3.setText(String.valueOf(currentItem.getWrong()));
    }






}
