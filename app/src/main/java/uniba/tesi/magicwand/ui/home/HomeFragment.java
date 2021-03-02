package uniba.tesi.magicwand.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import uniba.tesi.magicwand.Aut_Controller.Login;
import uniba.tesi.magicwand.R;

public class HomeFragment extends Fragment {
    /**
     * Debug tag
     */
    public static final String TAG = HomeFragment.class.getName();

   /* private HomeViewModel homeViewModel;*/
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        return root;
    }
}