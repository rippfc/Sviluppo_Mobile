package uniba.tesi.magicwand.fragment;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.model.InfoPath;
import uniba.tesi.magicwand.model.Player;
import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.viewHolder.MyViewHolderPath;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShowCompleted#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShowCompleted extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = FragmentShowCompleted.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private TextView mTextData;
    private TextView mLat;
    private TextView mLon;
    private TextView mCity;

    public FragmentShowCompleted() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentShowCompleted.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentShowCompleted newInstance(String param1, String param2) {
        FragmentShowCompleted fragment = new FragmentShowCompleted();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("PathComplete");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mParam1);
        ((MainActivity) getActivity()).getFloatingActionButton().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_show_completed, container, false);
        mTextData=(TextView)view.findViewById(R.id.date);
        mLat=(TextView)view.findViewById(R.id.id_lat);
        mLon=(TextView)view.findViewById(R.id.id_lon);
        mCity=(TextView)view.findViewById(R.id.id_city_name);
        getData();
        recyclerView=(RecyclerView)view.findViewById(R.id.id_recycler_path_item);
        linearLayoutManager=new LinearLayoutManager(getContext());
        fetch(view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void fetch(View view) {
        Query query = FirebaseDatabase.getInstance().getReference("Completed").child(mParam1).child("players");
        FirebaseRecyclerOptions<Player> options= new FirebaseRecyclerOptions.Builder<Player>()
                .setQuery(query, new SnapshotParser<Player>() {
                    @NonNull
                    @Override
                    public Player parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Player player = snapshot.getValue(Player.class);
                        return player;
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<Player, MyViewHolderPath>(options) {

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
        recyclerView.setAdapter(adapter);
        }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void getData() {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("Completed").child(mParam1);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // mTextData.setText(getString(R.string.completed)+": "+snapshot.getValue(String.class));
                InfoPath infoPath= snapshot.getValue(InfoPath.class);
                mTextData.setText(": "+infoPath.getData());
                mLat.setText(": "+infoPath.getLat());
                mLon.setText(String.valueOf(infoPath.getLon()));
                mCity.setText(": "+infoPath.getCity());
                //Log.d("TEST PATH", infoPath.getData()+" "+" "+infoPath.getLat()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, error.toException());
            }
        });
    }

    @Override
    public void onDetach() {
        ((MainActivity) getActivity()).getFloatingActionButton().show();
        super.onDetach();
    }


}