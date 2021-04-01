package uniba.tesi.magicwand.utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import uniba.tesi.magicwand.R;

public class DialogResetPassword extends Dialog implements
        android.view.View.OnClickListener {
    private Button btnResetPassword;
    private TextView inputEmail;
    private FirebaseAuth mAuth;
    private String email;

    public DialogResetPassword(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_dialog);
        inputEmail =  findViewById(R.id.textView4);
        btnResetPassword =  findViewById(R.id.btn_reset);
        mAuth = FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail();
        inputEmail.setText(email);
        btnResetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), R.string.resetEmail, Toast.LENGTH_SHORT).show();
                    }
                });
        dismiss();
    }
}
