package uniba.tesi.magicwand.Create_Quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;

public class CreateNewSession extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_session);
        setTitle(getIntent().getStringExtra("Title"));
        //showAlertDialog();//alertDialog new
    }



    @Override
    public void onBackPressed() {//TODO: nel caso vedi di sistemare
        Intent intent=new Intent(CreateNewSession.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
/*
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage("Inserisci il nome della sessione");
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(inputText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("CANCELLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(CreateNewSession.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=inputText.getText().toString().trim();
                if(input.isEmpty()){
                    inputText.setError("inserisci un input valido");
                }else{
                    setTitle(input);
                    alertDialog.dismiss();
                }
            }
        });

    }*/


}