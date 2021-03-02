package uniba.tesi.magicwand.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

import uniba.tesi.magicwand.Utils.FragmentShowSession;
import uniba.tesi.magicwand.Utils.Play;
import uniba.tesi.magicwand.ViewHolder.MyViewHolderItem;
import uniba.tesi.magicwand.R;

public class PercorsoRegistrato extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = PercorsoRegistrato.class.getName();

    private String auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private TextView textView;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_percorso_registrato, container, false);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        textView = (TextView)root.findViewById(R.id.ptext);
        auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        recyclerView=(RecyclerView)root.findViewById(R.id.list);
        linearLayoutManager=new LinearLayoutManager(getContext());
        fetch(root);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void fetch(final View view) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(auth);

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
                //TODO: INSERISCI UN TIMER CHE DOPO 5 SEC VISUALIZZA NESSUN PERCORSO PRESENTE
                textView.setVisibility(View.GONE);
                holder.setNameSession(model);
                holder.nameSession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), model, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialogButtonClicked(model);

                    }
                });
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo: mod1
                        Bundle bundle = new Bundle();
                        bundle.putString("Session",model);
                        bundle.putString("User",auth);
                        Navigation.findNavController(view).navigate(R.id.action_nav_percorso_registrato_to_fragmentShowSession,bundle);
                        /*Intent intent= new Intent(getContext(), Play.class);
                        intent.putExtra("Session",model);
                        intent.putExtra("User",auth);
                        startActivity(intent);*/
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
                .setMessage(R.string.delete_path)
                .setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(auth);
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