package uniba.tesi.magicwand.create_Quiz;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

import uniba.tesi.magicwand.utility.LocaleManager;
import uniba.tesi.magicwand.model.Question;
import uniba.tesi.magicwand.R;

public class CreateNewSession extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = CreateNewSession.class.getName();

    private EditText question;
    private EditText aText;
    private EditText bText;
    private EditText cText;
    private EditText dText;
    private RadioButton aRadio;
    private RadioButton bRadio;
    private RadioButton cRadio;
    private RadioButton dRadio;
    private TextView questionNumber;
    private Button save;
    private Button addQuestion;
    private int currentQuestion = 1;
    private int precedentQuestion = 1;
    private String nameSession;

    //private Button bt1; set button for read card

    private String file ="file";
    private String selectedOption = "";

    ArrayList<Question> listQuest;
    JSONArray jsonArray;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_session);
        setTitle(getIntent().getStringExtra("Title"));

        iniNewSession();

        setRadioBt();//set radio batton

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkQuestions();

                if(precedentQuestion!=currentQuestion){
                    precedentQuestion++;
                    if(precedentQuestion!=currentQuestion)
                        setAllQuest(precedentQuestion);
                    else{
                        clearAllQuestion();
                        questionNumber.setText(String.valueOf(currentQuestion));
                    }

                }

                if(check){
                    precedentQuestion++;
                    currentQuestion++;
                    questionNumber.setText(String.valueOf(currentQuestion));
                    clearAllQuestion();
                    save.setVisibility(View.VISIBLE);
                }
                   
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkQuestions()){
                    saveQuestion();
                    Toast.makeText(CreateNewSession.this, R.string.saved, Toast.LENGTH_SHORT).show();
                }
                else
                    saveQuestion();
                finish();
            }
        });
    }

    private void iniNewSession() {
        jsonArray = new JSONArray();

        question= findViewById(R.id.questionView);
        questionNumber= findViewById(R.id.txQuestionNumber);
        aText= findViewById(R.id.textViewA);
        bText= findViewById(R.id.textViewB);
        cText= findViewById(R.id.textViewC);
        dText= findViewById(R.id.textViewD);

        aRadio= findViewById(R.id.radioButtonA);
        bRadio= findViewById(R.id.radioButtonB);
        cRadio= findViewById(R.id.radioButtonC);
        dRadio= findViewById(R.id.radioButtonD);

        save= findViewById(R.id.btn_save);
        addQuestion = findViewById(R.id.bt_new_question);
        nameSession =getTitle().toString();

        //  bt1=(findViewById(R.id.button1));

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();

        listQuest = new ArrayList<>();
    }


    private void setRadioBt() {
        aRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "A";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
             //   bt1.setVisibility(View.VISIBLE);
            }
        });
        bRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "B";
                aRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        cRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "C";
                bRadio.setChecked(false);
                aRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        dRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "D";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                aRadio.setChecked(false);
            }
        });
    }


    private void setAllQuest(int position) {
        clearAllQuestion();
        Question question1= new Question();
        question1 = listQuest.get(position-1);
        question.setText(question1.getQuestion());
        aText.setText(question1.getOpt_a());
        bText.setText(question1.getOpt_b());
        cText.setText(question1.getOpt_c());
        dText.setText(question1.getOpt_d());
        switch (question1.getAnswer()){
            case "A":
                aRadio.setChecked(true);
                break;
            case "B":
                bRadio.setChecked(true);
                break;
            case "C":
                cRadio.setChecked(true);
                break;
            case "D":
                dRadio.setChecked(true);
                break;
        }
    }



    private void clearAllQuestion() {
        aRadio.setChecked(false);
        bRadio.setChecked(false);
        cRadio.setChecked(false);
        dRadio.setChecked(false);
        question.setText(null);
        aText.setText(null);
        bText.setText(null);
        cText.setText(null);
        dText.setText(null);
        selectedOption="";
      //  bt1.setVisibility(View.INVISIBLE);
    }

    private boolean checkQuestions() {
        boolean check=false;
        if(TextUtils.isEmpty(question.getText().toString().trim()))
            question.setError(getString(R.string.inserisci_domanda));
        else if (TextUtils.isEmpty(aText.getText().toString().trim()))
            aText.setError(getString(R.string.inserisci_risposta));
        else if (TextUtils.isEmpty(bText.getText().toString().trim()))
            bText.setError(getString(R.string.inserisci_risposta));
        else if (TextUtils.isEmpty(cText.getText().toString().trim()))
            cText.setError(getString(R.string.inserisci_risposta));
        else if (TextUtils.isEmpty(dText.getText().toString().trim()))
            dText.setError(getString(R.string.inserisci_risposta));
        else if (selectedOption.equals(""))
            Toast.makeText(this, R.string.seleziona_risposta, Toast.LENGTH_SHORT).show();
        else {
            Question newQuest =new Question();
            newQuest.setQuestion(question.getText().toString());
            newQuest.setOpt_a(aText.getText().toString());
            newQuest.setOpt_b(aText.getText().toString());
            newQuest.setOpt_c(cText.getText().toString());
            newQuest.setOpt_d(dText.getText().toString());
            newQuest.setAnswer(selectedOption);
            listQuest.add(newQuest);
            check=true;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question",question.getText().toString());
                jsonObject.put("opt_a",aText.getText().toString());
                jsonObject.put("opt_b",bText.getText().toString());
                jsonObject.put("opt_c",cText.getText().toString());
                jsonObject.put("opt_d",dText.getText().toString());
                jsonObject.put("answer",selectedOption);
            }catch (JSONException e){
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);

        }
        return check;
    }



    private void saveQuestion() {
        if(jsonArray.length()!=0){
            final JSONObject tempObject = new JSONObject();
            try {
                tempObject.put("Questions",jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String jsonStr = tempObject.toString();
            Map<String,Object> result= new Gson().fromJson(jsonStr,Map.class);
            file =auth.getCurrentUser().getDisplayName();
            if(!TextUtils.isEmpty(file))
                myRef.child(file).child(nameSession).setValue(result);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_session, menu);
        MenuItem menuItem = menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                showAlertDialogButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onBackPressed() {
        if(precedentQuestion>1) {
            precedentQuestion--;
            setAllQuest(precedentQuestion);
        }else
            showAlertDialogButtonClicked();
    }



    private void showAlertDialogButtonClicked() {
        // setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setMessage(R.string.message2)
                .setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkQuestions();
                        saveQuestion();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.btAnnulla, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }



}