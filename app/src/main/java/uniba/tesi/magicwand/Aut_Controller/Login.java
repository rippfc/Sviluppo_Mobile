
package uniba.tesi.magicwand.Aut_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;

public class Login extends AppCompatActivity {

    private TextView txToRegister,txRememberPass;
    private EditText inputEmail,inputPassword;
    private Button btnLogin;

    private ProgressBar loginProgressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            if(mAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }else {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }

        initUI();



        txToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        txRememberPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ResetPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString();

                if(email.isEmpty()||!email.contains("@")){
                    inputEmail.setError(getString(R.string.errorEmail));
                   return;
                }else if(password.isEmpty()||password.length()<6){
                    inputPassword.setError(getString(R.string.errorPassword));
                    return;
                }else{
                    loginUser(email,password);
                }

            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginProgressBar.setVisibility(View.VISIBLE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, R.string.errorLogin,Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(Login.this, MainActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }else {
                                Toast.makeText(Login.this, R.string.errorCheck,Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                loginProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }

    private void initUI(){
        inputEmail=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.password);
        btnLogin=(Button)findViewById(R.id.btn_login);
        txToRegister=(TextView) findViewById(R.id.text_To_Register);
        txRememberPass=(TextView)findViewById(R.id.text_To_reset);
        loginProgressBar=(ProgressBar)findViewById(R.id.progressBar);
    }


}
