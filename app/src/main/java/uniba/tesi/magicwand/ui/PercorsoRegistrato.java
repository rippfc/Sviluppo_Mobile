package uniba.tesi.magicwand.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uniba.tesi.magicwand.Model.Question;
import uniba.tesi.magicwand.R;

public class PercorsoRegistrato extends Fragment {
    //Getting Firebase Instance
    private FirebaseDatabase database;
    //Getting Database Reference
    private DatabaseReference databaseReference;

    private FirebaseAuth auth;

    private ListView listView;

    private TestAdapter testAdapter;
    ArrayList<Question> tests=new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_percorso_registrato, container, false);

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        listView=(ListView)root.findViewById(R.id.id_list);
        testAdapter=new TestAdapter(getContext(),tests);
        listView.setAdapter(testAdapter);
        auth = FirebaseAuth.getInstance();
        getItem();

        return root;
    }

    private void getItem() {
        String nameUser= auth.getCurrentUser().getDisplayName();
        //addListenerForSingleValueEvent
        databaseReference.child(nameUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Question question1=new Question();
                    question1.setSeason(snapshot.getKey());
                    tests.add(question1);
                }
                testAdapter.dataList=tests;
                testAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Lettura fallita: " ,databaseError.getMessage());
            }
        });
    }



    class TestAdapter extends ArrayAdapter<Question> implements Filterable {
        private Context mContext;
        ArrayList<Question> dataList;
        public TestAdapter( Context context,ArrayList<Question> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);



            ((TextView)listItem.findViewById(R.id.percorso))
                    .setText(dataList.get(position).getSeason());

            ((Button)listItem.findViewById(R.id.bt_apri_percorso)).setText("Apri");


            return listItem;
        }
    }
}