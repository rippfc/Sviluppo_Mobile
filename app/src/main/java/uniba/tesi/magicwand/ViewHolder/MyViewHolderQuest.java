package uniba.tesi.magicwand.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uniba.tesi.magicwand.R;

public class MyViewHolderQuest extends RecyclerView.ViewHolder {
    private LinearLayout root;
    private TextView mQuestion;
    private TextView mAns1;
    private TextView mAns2;
    private TextView mAns3;
    private TextView mAns4;
    private TextView mTrue;
    private TextView mid;


    public MyViewHolderQuest(@NonNull View itemView) {
        super(itemView);
        root=itemView.findViewById(R.id.list_root2);
        mid=itemView.findViewById(R.id.text_id);
        mQuestion=itemView.findViewById(R.id.question);
        mAns1=itemView.findViewById(R.id.ans1);
        mAns2=itemView.findViewById(R.id.ans2);
        mAns3=itemView.findViewById(R.id.ans3);
        mAns4=itemView.findViewById(R.id.ans4);
        mTrue=itemView.findViewById(R.id.correct_ans);

    }

    public void setmQuestion(String string) {
        mQuestion.setText(string+"?");
    }

    public void setmAns1(String string) {
        mAns1.setText(string);
    }

    public void setmAns2(String string) {
       mAns2.setText(string);
    }

    public void setmAns3(String string) {
        mAns3.setText(string);
    }

    public void setmAns4(String string) {
        mAns4.setText(string);
    }

    public void setmTrue(String string) {
        mTrue.setText(string);
    }

    public void setMid(String string) {
        mid.setText(string+")");
    }
}
