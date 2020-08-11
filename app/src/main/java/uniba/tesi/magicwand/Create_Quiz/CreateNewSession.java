package uniba.tesi.magicwand.Create_Quiz;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;

public class CreateNewSession extends AppCompatActivity {

    private TextView nQuestion;
    private TextInputEditText question;
    private EditText newAnswer;
    private ImageView btAddAnswer;
    private Button btSave,btNewQuestion;
    private ListView listView;
    private int currentQuestion=1;
/*
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_session);
        setTitle(getIntent().getStringExtra("Title"));
        nQuestion=(TextView)findViewById(R.id.txQuestionNumber);
        question=(TextInputEditText)findViewById(R.id.questionView);
        newAnswer=findViewById(R.id.add_answer);
        btAddAnswer=(ImageView)findViewById(R.id.bt_new_answer);
        btSave=(Button)findViewById(R.id.btn_save);
        btNewQuestion=(Button)findViewById(R.id.bt_new_question);

        /*listView=(ListView)findViewById(R.id.list_risposte);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);

        View.OnClickListener onClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(newAnswer.getText().toString());
                newAnswer.setText("");
                adapter.notifyDataSetChanged();
            }
        };

        btAddAnswer.setOnClickListener(onClickListener);
        listView.setAdapter(adapter);*/

    }



    @Override
    public void onBackPressed() {//TODO: nel caso vedi di sistemare
        showAlertDialogButtonClicked();
    }



    private void showAlertDialogButtonClicked() {
        // setup the alert builder
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(R.string.tilte2)
                .setMessage(R.string.message2)
                .setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton(R.string.btCancella, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateNewSession.this, MainActivity.class);
                startActivity(intent);
                finish();
                    alertDialog.dismiss();
                }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: salva sessione
            }
        });
    }



}