package uniba.tesi.magicwand.aut_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import uniba.tesi.magicwand.R;

public class ResetPassword extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = ResetPassword.class.getName();

    private Button btnResetPassword;
    private EditText inputEmail;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.email);
        btnResetPassword = findViewById(R.id.btn_reset);
        mAuth = FirebaseAuth.getInstance();


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "reset properties");
                String email = inputEmail.getText().toString().trim();

                if (email.isEmpty()||!email.contains("@")||!email.contains(".")) {
                    inputEmail.setError(getString(R.string.errorEmail));
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, R.string.resetEmail, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPassword.this, R.string.emailCheck, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}