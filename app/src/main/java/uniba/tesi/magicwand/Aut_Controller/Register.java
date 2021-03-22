package uniba.tesi.magicwand.Aut_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import uniba.tesi.magicwand.R;

public class Register extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = Register.class.getName();

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputName;
    private EditText repeatPassword;
    private Button btRegister;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();//per referenziare


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome=inputName.getText().toString().trim();
                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString();
                String ripetiPass=repeatPassword.getText().toString();

                //TODO:da sistemare
                if(nome.isEmpty()||nome.length()<3) {
                    inputName.setError(getString(R.string.errorName));
                    return;
                }else if(email.isEmpty()||!(email.contains("@"))||!(email.contains("."))){
                    inputEmail.setError(getString(R.string.errorEmail));
                }else if(password.isEmpty()||password.length()<6){
                    Toast.makeText(Register.this, R.string.errorPassword, Toast.LENGTH_SHORT).show();
                }else if (!ripetiPass.equals(password)){
                    Toast.makeText(Register.this, R.string.errorPassword, Toast.LENGTH_SHORT).show();
                }else{
                    createFirebaseUser(email,password,nome);
                }

            }
        });
    }

    private void initUI() {
        inputName=(EditText)findViewById(R.id.txNome);
        inputEmail=(EditText)findViewById(R.id.txEmail);
        inputPassword=(EditText)findViewById(R.id.txPassword);
        repeatPassword=(EditText)findViewById(R.id.txRipetiPassword);
        btRegister=(Button)findViewById(R.id.btn_regiter);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void createFirebaseUser(String email, String password, final String nome){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mAuth.getCurrentUser().sendEmailVerification();
                            setNome(nome);
                            Toast.makeText(Register.this, R.string.checkEmail,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this,Login.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, R.string.errorNewUser,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void setNome(String nome){//per salvare il nome firebase
        FirebaseUser user= mAuth.getCurrentUser();

        UserProfileChangeRequest changeRequest=new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "nome caricato");
                }else{
                    Log.d(TAG, "nome non caricato");
                }
            }
        });
    }


    }