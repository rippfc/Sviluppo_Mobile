package uniba.tesi.magicwand.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import uniba.tesi.magicwand.Aut_Controller.Login;
import uniba.tesi.magicwand.Model.Player;
import uniba.tesi.magicwand.R;

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
        mClose=(Button)findViewById(R.id.btn_close);

        buildRecyclerView();

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ResultAdapter(result, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}