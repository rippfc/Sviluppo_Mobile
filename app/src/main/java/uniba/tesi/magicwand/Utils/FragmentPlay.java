package uniba.tesi.magicwand.Utils;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uniba.tesi.magicwand.Model.Question;
import uniba.tesi.magicwand.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlay#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentPlay extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mPlayer, mPoint, mQuestion, mId, mScore;
    private Chronometer mTime;//chronometer
    private Button mBt_a,mBt_b,mBt_c,mBt_d;

    private int correct=0;
    private int wrong=0;
    private int total=0;
    private int computerCount=0;

    ArrayList<Question> questionArrayList= new ArrayList<>();


    //Getting Reference to Root Node
    DatabaseReference myRef;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPlay.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPlay newInstance(String param1, String param2) {
        FragmentPlay fragment = new FragmentPlay();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPlay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    //    Log.i("info",mParam1+mParam2);
        View view= inflater.inflate(R.layout.fragment_play, container, false);

        mPlayer = view.findViewById(R.id.id_player);
        mTime = view.findViewById(R.id.text_time);
        mPoint = view.findViewById(R.id.points);
        mQuestion = view.findViewById(R.id.domanda);
        mId = view.findViewById(R.id.quest_num);
        mBt_a = view.findViewById(R.id.buttonA);
        mBt_b = view.findViewById(R.id.buttonB);
        mBt_c = view.findViewById(R.id.buttonC);
        mBt_d = view.findViewById(R.id.buttonD);
        mScore=view.findViewById(R.id.points);

        //chronometer
        mTime.setFormat("%s");
        mTime.setBase(SystemClock.elapsedRealtime());
        mTime.start();
        Log.i("time start",mTime.getText().toString());








        //updateQuestion();
        setup_questions();

        return view;
    }

    private void setup_questions() {

        myRef= FirebaseDatabase.getInstance().getReference().child(mParam1).child(mParam2).child("Questions").child(String.valueOf(computerCount));
        computerCount++;
        total++;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final Question quest= snapshot.getValue(Question.class);
                    mQuestion.setText(quest.getQuestion());
                    mBt_a.setText(quest.getOpt_a());
                    mBt_b.setText(quest.getOpt_b());
                    mBt_c.setText(quest.getOpt_c());
                    mBt_d.setText(quest.getOpt_d());
                    mId.setText(String.valueOf(quest.getId()));
                    mScore.setText(String.valueOf(correct));


               // Log.i("mbtc","Risposta"+quest.getAnswer() + "\nA:"+mBt_a.getText()+ "   B:"+mBt_b.getText()+ "   C:"+mBt_c.getText()+ "   D:"+mBt_d.getText());


                mBt_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(quest.getAnswer().equals("A")){
                            Toast.makeText(getContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBt_b.getText().toString().equals("B")){
                            Toast.makeText(getContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(quest.getAnswer().equals("C")){
                            Toast.makeText(getContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBt_d.getText().toString().equals("D")){
                            Toast.makeText(getContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

  /*  private void updateQuestion() {
        myRef =FirebaseDatabase.getInstance().getReference().child(mParam1).child(mParam2).child("Questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Question quest= data.getValue(Question.class);
                  questionArrayList.add(quest);



                    Log.i("Prova", String.valueOf(questionArrayList.size()));
                }

                user();


            }

            private void user() {
                for (int i=0; i<questionArrayList.size();i++){
                   Log.i("arra",questionArrayList.size()+"\n"+questionArrayList.get(i).getQuestion()+"\n"+questionArrayList.get(i).getOpt_a()+"\n" );
//devo mettere un adapter come test_quiz/Result
                    mQuestion.setText(questionArrayList.get(i).getQuestion());
                    mBt_a.setText(questionArrayList.get(i).getOpt_a());
                    mBt_b.setText(questionArrayList.get(i).getOpt_b());
                    mBt_c.setText(questionArrayList.get(i).getOpt_c());
                    mBt_d.setText(questionArrayList.get(i).getOpt_d());


                    mBt_c.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mBt_c.getText().equals("C")){
                                Toast.makeText(getContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                                correct++;
                                user();
                            }else{
                                Toast.makeText(getContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                                wrong++;
                                user();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG", "onCancelled: Error: " + error.getMessage());
            }
        });


    }*/
}