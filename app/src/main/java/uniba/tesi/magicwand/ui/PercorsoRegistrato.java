package uniba.tesi.magicwand.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uniba.tesi.magicwand.ViewHolder.MyViewHolder;
import uniba.tesi.magicwand.R;

public class PercorsoRegistrato extends Fragment {
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_percorso_registrato, container, false);

        auth = FirebaseAuth.getInstance();

        recyclerView=(RecyclerView)root.findViewById(R.id.list);
        linearLayoutManager=new LinearLayoutManager(getContext());
        fetch();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(auth.getCurrentUser().getDisplayName());

        FirebaseRecyclerOptions<String> option= new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, new SnapshotParser<String>() {
                    @NonNull
                    @Override
                    public String parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getKey();
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<String, MyViewHolder>(option){
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final String model) {
                holder.setNameSession(model);
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialogButtonClicked(model);

                    }
                });
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), model+"one moment for new activity", Toast.LENGTH_SHORT).show();
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

    private void showAlertDialogButtonClicked(final String key) {
        // setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        builder.setTitle(R.string.tilte2)
                .setMessage("Sei sicuro di voler eliminare il percorso?")
                .setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(auth.getCurrentUser().getDisplayName());
                databaseReference.child(key).removeValue();
            }
        })

                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

}