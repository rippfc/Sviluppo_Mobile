package uniba.tesi.magicwand.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uniba.tesi.magicwand.Aut_Controller.Login;
import uniba.tesi.magicwand.Create_Quiz.CreateNewSession;
import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.Model.Question;
import uniba.tesi.magicwand.R;

public class Play extends AppCompatActivity {

    /**
     * Debug tag
     */
    public static final String TAG = Play.class.getName();

    private String mSession;
    private String mUser;

    private TextView mPlayer;
    private TextView mPoint;
    private TextView mQuestion;
    private TextView mId;
    private TextView mScore;
    private Chronometer mTime;//chronometer
    private Button mBt_a;
    private Button mBt_b;
    private Button mBt_c;
    private Button mBt_d;

    private int correct=0;
    private int wrong=0;
    private int total=0;
    private int computerCount=0;
    private int totalPlayer;

    //Getting Reference to Root Node
    DatabaseReference myRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        alertNumberPlayer();
        initUI();




    }

    private void initUI() {
        mSession=getIntent().getStringExtra("Session");
        mUser=getIntent().getStringExtra("User");
        setTitle(mSession);

        mPlayer = (TextView)findViewById(R.id.id_player);
        mTime = (Chronometer)findViewById(R.id.text_time);
        mPoint = (TextView)findViewById(R.id.points);
        mQuestion = (TextView)findViewById(R.id.domanda);
        mId = (TextView)findViewById(R.id.quest_num);
        mBt_a = (Button) findViewById(R.id.buttonA);
        mBt_b = (Button)findViewById(R.id.buttonB);
        mBt_c = (Button)findViewById(R.id.buttonC);
        mBt_d = (Button)findViewById(R.id.buttonD);
        mScore=(TextView)findViewById(R.id.points);


        //chronometer
        mTime.setFormat("%s");
        mTime.setBase(SystemClock.elapsedRealtime());
        mTime.start();

        //Log.i(TAG,mTime.getText().toString());

        /*updateQuestion();*/
       setup_questions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.visit_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Log.d(TAG, "Clicked on refresh!");// refresch senza ricaricare tutto
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                return true;
            case R.id.finish:
                alertCloseSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* private void updateQuestion() {
        ArrayList<Question> arrayList = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference().child(mUser).child(mSession).child("Questions");



    }*/

    private void setup_questions() {
        myRef= FirebaseDatabase.getInstance().getReference().child(mUser).child(mSession).child("Questions").child(String.valueOf(computerCount));
        computerCount++;
        total++;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final Question quest= snapshot.getValue(Question.class);
                mQuestion.setText(quest.getQuestion()+"?");
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
                            Toast.makeText(getApplicationContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getApplicationContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBt_b.getText().toString().equals("B")){
                            Toast.makeText(getApplicationContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getApplicationContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(quest.getAnswer().equals("C")){
                            Toast.makeText(getApplicationContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getApplicationContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
                            wrong++;
                            setup_questions();
                        }
                    }
                });

                mBt_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBt_d.getText().toString().equals("D")){
                            Toast.makeText(getApplicationContext(), "Risposta Corretta", Toast.LENGTH_SHORT).show();
                            correct++;
                            setup_questions();
                        }else{
                            Toast.makeText(getApplicationContext(), "Risposta sbagliata", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        alertCloseSession();
    }

    private void alertCloseSession() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setCancelable(false)
                .setMessage("Vuoi terminare la sessione?")
                .setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void alertNumberPlayer() {//todo:controllo sui numeri inseriti e correggi tutti i dialog in questo modo
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputText.setText("1");
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setCancelable(false)
                .setMessage("Quanti giocatori partecipano alla sessione?")
                .setIcon(R.drawable.ic_menu_percorso)
                .setView(inputText);

        builder.setPositiveButton(R.string.btPositive,null);
        builder.setNegativeButton(R.string.btAnnulla,null);


        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //sovrascrive il metodo clik
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=inputText.getText().toString().trim();
                try {
                    if(input.isEmpty()){
                        inputText.setError(getString(R.string.errorInputNumber));
                    }else{
                        totalPlayer=Integer.parseInt(input);

                        Toast.makeText(getApplicationContext(), "int="+totalPlayer+"string text:"+input, Toast.LENGTH_SHORT).show();
                        mPlayer.setText(input);
                        alertDialog.dismiss();
                    }
                }catch (NumberFormatException e){
                    inputText.setError(getString(R.string.errorInputNumber));
                }
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }
}