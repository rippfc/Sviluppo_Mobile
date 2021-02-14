package uniba.tesi.magicwand.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uniba.tesi.magicwand.R;
import uniba.tesi.magicwand.ui.home.HomeViewModel;


public class SessioniCompletate extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = SessioniCompletate.class.getName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sessioni_completate, container, false);
        //final TextView textView = root.findViewById(R.id.text_percorso);
        return root;
    }
}