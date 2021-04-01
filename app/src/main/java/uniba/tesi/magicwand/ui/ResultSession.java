package uniba.tesi.magicwand.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import uniba.tesi.magicwand.model.Player;
import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.utility.LocaleManager;
import uniba.tesi.magicwand.utility.ResultAdapter;

public class ResultSession extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = ResultSession.class.getName();

    ArrayList<Player> result= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ResultAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mClose;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_session);
        result=getIntent().getExtras().getParcelableArrayList("risultati");
        mClose=findViewById(R.id.btn_close);

        buildRecyclerView();

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ResultAdapter(result, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}