package uniba.tesi.magicwand.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.ViewHolder.MyViewHolderItem;
import uniba.tesi.magicwand.ui.Play;


public class FragmentFirst extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = FragmentFirst.class.getName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private LinearLayout mLinearLayout;
    private ProgressBar mProgressBar;
    private String mReference="Completed";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);

        mLinearLayout=(LinearLayout)view.findViewById(R.id.linear_layout_path_completed_visitor);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_path_completed_visitor);
        referenceExists(mReference);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.list_path_visitor);
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        fetch(view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    private void referenceExists(String ref){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(ref);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetch(final View view) {
        Query query = FirebaseDatabase.getInstance().getReference(mReference);

        FirebaseRecyclerOptions<String> option= new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, new SnapshotParser<String>() {
                    @NonNull
                    @Override
                    public String parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getKey();
                    }
                }).build();

        mAdapter= new FirebaseRecyclerAdapter<String, MyViewHolderItem>(option){
            @NonNull
            @Override
            public MyViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new MyViewHolderItem(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderItem holder, int position, @NonNull final String model) {
                mProgressBar.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.GONE);
                holder.setNameSession(model);
                holder.nameSession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), model, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_ranking));
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentSecond fragmentSecond = new FragmentSecond();
                        fragmentSecond= FragmentSecond.newInstance(mReference,model);
                        FragmentManager fragmentManager= getFragmentManager();
                        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout,fragmentSecond);
                        fragmentTransaction.commit();
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
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