package uniba.tesi.magicwand.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import uniba.tesi.magicwand.BuildConfig;
import uniba.tesi.magicwand.MainActivity;
import uniba.tesi.magicwand.R;


public class Info extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = Info.class.getName();

    private View root;
    private TextView version;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_info, container, false);
        version=(TextView) root.findViewById(R.id.version);
        version.setText(getString(R.string.version)+ BuildConfig.VERSION_NAME);
        button=(Button)root.findViewById(R.id.Button02);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"antonio.ricchetti92@gmail.com"});

                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, getString(R.string.chooseEmail)));
            }
        });
        return root;
    }

    @Override
    public void onDetach() {
        ((MainActivity) getActivity()).getFloatingActionButton().show();
        super.onDetach();
    }


}