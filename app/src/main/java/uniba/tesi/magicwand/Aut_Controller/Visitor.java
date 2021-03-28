package uniba.tesi.magicwand.Aut_Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.Utils.LocaleManager;
import uniba.tesi.magicwand.fragment.FragmentFirst;

public class Visitor extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = Visitor.class.getName();

    FrameLayout frameLayout;
    Button button;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        frameLayout=(FrameLayout)findViewById(R.id.frame_layout);
        button=findViewById(R.id.close_visitor_activity);


        FragmentFirst fragmentFirst= new FragmentFirst();
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragmentFirst);
        fragmentTransaction.commit();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}