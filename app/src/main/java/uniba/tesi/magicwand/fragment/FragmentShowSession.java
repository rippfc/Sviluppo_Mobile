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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.model.Question;
import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.viewHolder.MyViewHolderQuest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShowSession#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShowSession extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = FragmentShowSession.class.getName();


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "param1";
    private static final String ARG_SESSION = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    View view;

   // private FloatingActionButton floatingActionButton;


    public FragmentShowSession() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentShowSession.
     */
    public static FragmentShowSession newInstance(String param1, String param2) {
        FragmentShowSession fragment = new FragmentShowSession();
        Bundle args = new Bundle();
        args.putString(ARG_USER, param1);
        args.putString(ARG_SESSION, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mParam2);
        ((MainActivity) getActivity()).getFloatingActionButton().setImageResource(R.drawable.ic_play_arrow_24);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //todo: mod1
            mParam1 = getArguments().getString("User");
            mParam2 = getArguments().getString("Session");

          /*  mParam1 = getArguments().getString(ARG_USER);
            mParam2 = getArguments().getString(ARG_SESSION);
*/

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        MainActivity.CURRENT_SESSION=mParam2;

        view= inflater.inflate(R.layout.fragment_show_session, container, false);
        // Inflate the layout for this fragment
        Log.i(TAG,mParam1+" "+mParam2);
        recyclerView=view.findViewById(R.id.id_recycler_question_item);
        linearLayoutManager=new LinearLayoutManager(getContext());
        fetch();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(mParam1).child(mParam2).child("Questions");

        FirebaseRecyclerOptions<Question>options=new FirebaseRecyclerOptions.Builder<Question>()
                .setQuery(query, new SnapshotParser<Question>() {
                    @NonNull
                    @Override
                    public Question parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Question quest= snapshot.getValue(Question.class);
                        return quest;
                    }
                }).build();

        adapter=new FirebaseRecyclerAdapter<Question, MyViewHolderQuest>(options){

            @NonNull
            @Override
            public MyViewHolderQuest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session,parent,false);
                return new MyViewHolderQuest(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderQuest holder, int position, @NonNull Question model) {
                holder.setmQuestion(model.getQuestion());
                holder.setMid(String.valueOf(position+1));
                holder.setmAns1(model.getOpt_a());
                holder.setmAns2(model.getOpt_b());
                holder.setmAns3(model.getOpt_c());
                holder.setmAns4(model.getOpt_d());
                holder.setmTrue(model.getAnswer());
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

    @Override
    public void onDetach() {
        ((MainActivity) getActivity()).getFloatingActionButton().setImageResource(R.drawable.ic_add_24);
        super.onDetach();
    }


}

