package uniba.tesi.magicwand.Aut_Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.Utils.LocaleManager;

public class Visitor extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = Visitor.class.getName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
    }
}