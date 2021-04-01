package uniba.tesi.magicwand.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uniba.tesi.magicwand.model.InfoPath;
import uniba.tesi.magicwand.model.Player;
import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.viewHolder.MyViewHolderPath;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSecond#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSecond extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = FragmentSecond.class.getName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private TextView mTextPath;
    private TextView mTextData;
    private TextView mLat;
    private TextView mLon;
    private TextView mCity;

    public FragmentSecond() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSecond.
     */
    public static FragmentSecond newInstance(String param1, String param2) {
        FragmentSecond fragment = new FragmentSecond();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second, container, false);
        mTextPath= view.findViewById(R.id.id_pat_visitor);
        mTextPath.setText(": "+mParam2);
        mTextData= view.findViewById(R.id.date2);
        mLat= view.findViewById(R.id.id_lat2);
        mLon= view.findViewById(R.id.id_lon2);
        mCity= view.findViewById(R.id.id_city);
        getData();
        mRecyclerView= view.findViewById(R.id.id_recycler_path_item2);
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        fetch(view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    private void fetch(View view) {
        Query query = FirebaseDatabase.getInstance().getReference("Completed").child(mParam2).child("players");
        FirebaseRecyclerOptions<Player> options= new FirebaseRecyclerOptions.Builder<Player>()
                .setQuery(query, new SnapshotParser<Player>() {
                    @NonNull
                    @Override
                    public Player parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Player player = snapshot.getValue(Player.class);
                        return player;
                    }
                }).build();

        mAdapter= new FirebaseRecyclerAdapter<Player, MyViewHolderPath>(options) {

            @NonNull
            @Override
            public MyViewHolderPath onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item,parent,false);
                return new MyViewHolderPath(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderPath holder, int position, @NonNull Player model) {
                final int[] colors = getResources().getIntArray(R.array.paint);
                holder.mid.setText(getResources().getString(R.string.player)+" "+String.valueOf(model.getId()+1));
                holder.mid.setTextColor(colors[position]);
                holder.score.setText(String.valueOf(model.getScore()));
                holder.wrong.setText(String.valueOf(model.getWrong()));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }



    private void getData() {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference().child(mParam1).child(mParam2);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                InfoPath infoPath= snapshot.getValue(InfoPath.class);
                mTextData.setText(": "+infoPath.getData());
                mLat.setText(": "+infoPath.getLat());
                mLon.setText(String.valueOf(infoPath.getLon()));
                mCity.setText(": "+infoPath.getCity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, error.toException());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}