package uniba.tesi.magicwand.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import uniba.tesi.magicwand.Model.Player;
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
    private ImageView image;
    private TextView mPoint;
    private TextView mQuestion;
    private TextView mId;
    private TextView mScore;
    private TextView mParticipant;
    private Button mBt_a;
    private Button mBt_b;
    private Button mBt_c;
    private Button mBt_d;
    private Chronometer mTime;
    private LottieAnimationView lottieAnimationView;


    private ArrayList<Question> questionsArray = new ArrayList<>();
    private DatabaseReference mDatabase;
    private int quesNum;
    private int numberOfPlayer;
    private int currentPlayer;

    private ArrayList<Player> playersArray;//Todo da cancellare??
    DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm:ss");
    String date = df.format(Calendar.getInstance().getTime());

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mSession=getIntent().getStringExtra("Session");
        mUser=getIntent().getStringExtra("User");
        setTitle(mSession);

        image=(ImageView)findViewById(R.id.icon_player);
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
        mParticipant=(TextView)findViewById(R.id.participant);
        lottieAnimationView=(LottieAnimationView)findViewById(R.id.animation_lt);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        playersArray= new ArrayList<>();
        quesNum=0;
        currentPlayer=0;

        alertNumberPlayer();
        getQuestions(mDatabase);

    }

    private void alertNumberPlayer() {//todo:controllo sui numeri inseriti e correggi tutti i dialog in questo modo
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputText.setHint(R.string.min_max);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setCancelable(false)
                .setMessage(R.string.many_player)
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
                    numberOfPlayer =Integer.parseInt(input);
                    if(numberOfPlayer <1||numberOfPlayer >10){
                        inputText.setError(getString(R.string.errorInputNumber));
                    }else{
                        setPlayer(numberOfPlayer);
                        alertDialog.dismiss();
                        mTime.setBase(SystemClock.elapsedRealtime());
                        mTime.setVisibility(View.VISIBLE);
                        mTime.start();
                        lottieAnimationView.setVisibility(View.VISIBLE);
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


    private void getQuestions(DatabaseReference databaseReference) {
        questionsArray.clear();
        databaseReference.child(mUser).child(mSession).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    Question question = data.getValue(Question.class);
                    questionsArray.add(question);
                }
                Log.d(TAG,String.valueOf(questionsArray.size()));
                setQuestion();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "onCancelled: Error: " + error.getMessage());
            }
        });
    }

    private void setQuestion() {
        mQuestion.setText(questionsArray.get(quesNum).getQuestion());
        mBt_a.setText(questionsArray.get(quesNum).getOpt_a());
        mBt_b.setText(questionsArray.get(quesNum).getOpt_b());
        mBt_c.setText(questionsArray.get(quesNum).getOpt_c());
        mBt_d.setText(questionsArray.get(quesNum).getOpt_d());
        mId.setText(String.valueOf(quesNum+1)+"/"+String.valueOf(questionsArray.size()));
    }

    private void setPlayer(int input) {
        Log.d(TAG, "patecipanti: "+ input);//plurals
        String players = getResources().getQuantityString(R.plurals.number_of_player,input,input);
        mParticipant.setText(players);
        playAnim(mPlayer,0,5);

        for (int i=0; i<input;i++){//Todo da cancellare??
            playersArray.add(new Player(i,0,0));//Todo da cancellare??
            Log.i(TAG, "size"+String.valueOf(playersArray.size())+" get id\t"+playersArray.get(i).getId());//Todo da cancellare??
        }
    }

    public void btPress(View view) {
        String select="";
        boolean check;
        switch (view.getId()){
            case R.id.buttonA:
                select="A";
                break;
            case R.id.buttonB:
                select="B";
                break;
            case R.id.buttonC:
                select="C";
                break;
            case R.id.buttonD:
                select="D";
                break;
        }
        if (select.equals(questionsArray.get(quesNum).getAnswer())){
            Toast.makeText(this, "Risposta corretta", Toast.LENGTH_SHORT).show();
            check=true;
        }else{
            Toast.makeText(this, "Risposta sbagliata", Toast.LENGTH_SHORT).show();
            check=false;
        }
        checkPlayer(currentPlayer,check);
    }

    private void checkPlayer(int cp, boolean check) {
        if (check) {
            playersArray.get(cp).setScore(playersArray.get(cp).getScore() + 1);
            //changeQuestions();
        }else {
            playersArray.get(cp).setWrong(playersArray.get(cp).getWrong() + 1);
        }
        currentPlayer++;
        if (cp == numberOfPlayer - 1) {
            currentPlayer = 0;
            changeQuestions();
        }
        mPoint.setText(String.valueOf(playersArray.get(currentPlayer).getScore()));
        playAnim(mPlayer,0,5);
        playAnim(image,0,6);
        Log.d(TAG, "current " + cp
                + " numberplayer " + numberOfPlayer
                + " value of player" + String.valueOf(playersArray.get(cp).getId())
                + "\tcorrette\t" + String.valueOf(playersArray.get(cp).getScore())
                + "\terrori\t" + String.valueOf(playersArray.get(cp).getWrong()));
    }

    private void changeQuestions() {
        if( quesNum < questionsArray.size() - 1){
            quesNum++;
            Log.i(TAG,quesNum+"\tarray\t"+questionsArray.size());
            playAnim(mQuestion,0,0);
            playAnim(mBt_a,0,1);
            playAnim(mBt_b,0,2);
            playAnim(mBt_c,0,3);
            playAnim(mBt_d,0,4);
            mId.setText(String.valueOf(quesNum + 1) + "/" + String.valueOf(questionsArray.size()));
        }else {
            Player max= Collections.max(playersArray);
            Log.d(TAG,"\nvalue win"+String.valueOf(max.getId()));
            mTime.stop();
            Toast.makeText(this, "il vincitore è:"+String.valueOf(max.getId()), Toast.LENGTH_SHORT).show();
            saveResult(playersArray);
            Intent intent=new Intent(this,ResultSession.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("risultati", playersArray);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    private void saveResult(ArrayList<Player> players) {
        //TODO:aggiungi il luogo dove si è svolto
        Map<String,Object> stringObjectMap= new HashMap<>();
        stringObjectMap.put("data",date);
        stringObjectMap.put("players",players);
        mDatabase.child("Completed").child(mSession).setValue(stringObjectMap);
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
                recreate();
                return true;
            case R.id.finish:
                alertCloseSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        alertCloseSession();
    }

    private void alertCloseSession() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setCancelable(false)
                .setMessage(R.string.end_session)
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

    private void playAnim(final View view,final int value, final int numView){
        Log.d(TAG,"Starting animation");
        final int[] colors = getApplicationContext().getResources().getIntArray(R.array.paint);
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (numView) {
                                case 0:
                                    ((TextView) view).setText(questionsArray.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button) view).setText(questionsArray.get(quesNum).getOpt_a());
                                    break;
                                case 2:
                                    ((Button) view).setText(questionsArray.get(quesNum).getOpt_b());
                                    break;
                                case 3:
                                    ((Button) view).setText(questionsArray.get(quesNum).getOpt_c());
                                    break;
                                case 4:
                                    ((Button) view).setText(questionsArray.get(quesNum).getOpt_d());
                                    break;
                                case 5:
                                    ((TextView) view).setText(getString(R.string.player)+" "+String.valueOf(playersArray.get(currentPlayer).getId()+1));
                                    ((TextView)view).setTextColor(colors[currentPlayer]);
                                    image.setColorFilter(colors[currentPlayer]);
                                    break;
                                case 6:
                                    ((ImageView)view).setColorFilter(colors[currentPlayer]);
                                    break;
                            }
                            playAnim(view,1, numView);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}