package uniba.tesi.magicwand.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;


public class Info extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = Info.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);

    }

    @Override
    public void onDetach() {
        ((MainActivity) getActivity()).getFloatingActionButton().show();
        super.onDetach();
    }
}