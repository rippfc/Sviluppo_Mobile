package uniba.tesi.magicwand.Aut_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uniba.tesi.magicwand.R;

public class Register extends AppCompatActivity {

    private EditText inputEmail,inputPassword,inputName,repeatPassword;
    private Button btRegister;
    private TextView txToLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();//per referenziare
        
        mAuth = FirebaseAuth.getInstance();


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome=inputName.getText().toString().trim();
                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString();
                String ripetiPass=repeatPassword.getText().toString();

                //TODO:da sistemare
                if(nome.isEmpty()||nome.length()<3) {
                    inputName.setError("minimo 3 caratteri");
                    Toast.makeText(getApplicationContext(), "Nome non Valido", Toast.LENGTH_LONG).show();
                    return;
                }else if(email.isEmpty()||!(email.contains("@"))){
                    inputEmail.setError("Email non Valida");
                   // Toast.makeText(getApplicationContext(),"Email non Valida",Toast.LENGTH_LONG).show();
                }else if(password.isEmpty()||password.length()<6){
                    inputPassword.setError("Password non Valida");
                    //Toast.makeText(getApplicationContext(),"Password non Valida",Toast.LENGTH_LONG).show();
                }else if (!ripetiPass.equals(password)){
                    repeatPassword.setError("Password non Valida");
                }else{
                    createFirebaseUser(email,password,nome);
                }

            }
        });

        txToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    private void initUI() {
        inputName=(EditText)findViewById(R.id.txNome);
        inputEmail=(EditText)findViewById(R.id.txEmail);
        inputPassword=(EditText)findViewById(R.id.txPassword);
        repeatPassword=(EditText)findViewById(R.id.txRipetiPassword);
        btRegister=(Button)findViewById(R.id.btn_regiter);
        txToLogin=(TextView)findViewById(R.id.text_To_Login);
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
                            Toast.makeText(Register.this, "Email di verifica inviata. Controlla la tua email",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this,Login.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Registrazione fallita",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }