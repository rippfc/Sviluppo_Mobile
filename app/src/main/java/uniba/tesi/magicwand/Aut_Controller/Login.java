package uniba.tesi.magicwand.Aut_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InetAddress;
import java.util.Locale;

import uniba.tesi.magicwand.Utils.LocaleManager;
import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;

public class Login extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = Login.class.getName();

    private TextView txToRegister;
    private TextView txRememberPass;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    private ImageView flagGb;
    private ImageView flagIt;
    private TextView visitor;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d(TAG,FirebaseDatabase.getInstance().toString());
        }catch (Exception e){
            Log.d(TAG,"SetPresistenceEnabled:Fail"+FirebaseDatabase.getInstance().toString());
            e.printStackTrace();
        }
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);//Todo:abilitare offline
        setContentView(R.layout.activity_login);

        initUI();


        if (mAuth.getCurrentUser() != null) {

            if(mAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }else {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }

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
                Log.d(TAG, "login properties");
                if (isNetworkConnected()) {
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString();

                    if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                        inputEmail.setError(getString(R.string.errorEmail));
                        return;
                    } else if (password.isEmpty() || password.length() < 6) {
                        Toast.makeText(Login.this, R.string.errorPassword, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        loginProgressBar.setVisibility(View.VISIBLE);
                        loginUser(email, password);
                    }

                } else {
                    Toast.makeText(Login.this, "connessine assente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Visitor.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                                loginProgressBar.setVisibility(View.GONE);
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
        loginProgressBar=(ProgressBar)findViewById(R.id.progressBarLg);
        flagGb=(ImageView)findViewById(R.id.imageButtonGb);
        flagIt=(ImageView)findViewById(R.id.imageButtonIt);
        visitor=(TextView)findViewById(R.id.tx_visitor);
        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        checkFlag();
    }

    private void checkFlag() {
       // Toast.makeText(this, Locale.getDefault().getLanguage(), Toast.LENGTH_SHORT).show();
        if (Locale.getDefault().getLanguage().equals("en")){
            flagGb.setVisibility(View.GONE);
            flagIt.setVisibility(View.VISIBLE);
        }else{
            flagIt.setVisibility(View.GONE);
            flagGb.setVisibility(View.VISIBLE);}
    }


    public void changeLanguage(View view) {
        switch (view.getId()){
            case R.id.imageButtonIt:
                setNewLocale(this, LocaleManager.ITALIAN);
                break;
            case R.id.imageButtonGb:
                setNewLocale(this, LocaleManager.ENGLISH);
                break;
            default:
                break;
        }
    }

    private void setNewLocale(Context mContext, String language) {
        LocaleManager.setNewLocale(this, language);
        recreate();
   }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
