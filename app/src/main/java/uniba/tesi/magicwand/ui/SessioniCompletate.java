package uniba.tesi.magicwand.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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


public class SessioniCompletate extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = SessioniCompletate.class.getName();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayout mlinearLayout;
    private ProgressBar progressBar;
    private String reference="Completed";
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_sessioni_completate, container, false);
        mlinearLayout=(LinearLayout)root.findViewById(R.id.linearLayoutPathCompleted);
        progressBar = (ProgressBar)root.findViewById(R.id.progressPathCompleted);
        referenceExists(reference);
        recyclerView=(RecyclerView)root.findViewById(R.id.listPath);
        linearLayoutManager=new LinearLayoutManager(getContext());
        fetch(root);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void referenceExists(String ref){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(ref);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG,"riferimento esiste");
                } else {
                    mlinearLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG,"riferimento non esiste");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetch(final View view) {
        Query query = FirebaseDatabase.getInstance().getReference(reference);

        FirebaseRecyclerOptions<String> option= new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, new SnapshotParser<String>() {
                    @NonNull
                    @Override
                    public String parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getKey();
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<String, MyViewHolderItem>(option){
            @NonNull
            @Override
            public MyViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new MyViewHolderItem(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderItem holder, int position, @NonNull final String model) {
                progressBar.setVisibility(View.GONE);
                mlinearLayout.setVisibility(View.GONE);
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
                        Bundle bundle = new Bundle();
                        bundle.putString("PathComplete",model);
                        Navigation.findNavController(view).navigate(R.id.action_nav_sessioni_completate_to_fragmentShowCompleted,bundle);
                    }
                });
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
}