package uniba.tesi.magicwand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import uniba.tesi.magicwand.Aut_Controller.Login;
import uniba.tesi.magicwand.Create_Quiz.CreateNewSession;
import uniba.tesi.magicwand.Utils.DialogResetPassword;
import uniba.tesi.magicwand.Utils.Play;

public class MainActivity extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = MainActivity.class.getName();

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private TextView user,status;
    private ImageView image;
    FloatingActionButton fab;
    NavigationView navigationView;
    View header;
    DrawerLayout drawer;
    NavController navController;

   // public static String CURRENT_TAG = TAG;
    public static String CURRENT_SESSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        auth= FirebaseAuth.getInstance();







        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//azione pulsante
                switch (navController.getCurrentDestination().getId()){
                    case R.id.fragmentShowSession:
                        Intent intent= new Intent(MainActivity.this, Play.class);
                        intent.putExtra("Session",CURRENT_SESSION);
                        intent.putExtra("User",auth.getCurrentUser().getDisplayName());
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, auth.getCurrentUser().getDisplayName()+" "+CURRENT_SESSION, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        //Toast.makeText(MainActivity.this, CURRENT_TAG, Toast.LENGTH_SHORT).show();
                        showAlertDialog();
                        //break;
                }


            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);//per settare stato connessine e image
        status=header.findViewById(R.id.txStatus);
        user=header.findViewById(R.id.txUser);
        image=header.findViewById(R.id.imageStatus);
        setTextUser(user,status,image);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_percorso_registrato, R.id.nav_sessioni_completate,R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                switch (id){
                    case R.id.nav_respass:
                        DialogResetPassword alertResetPassword=new DialogResetPassword(MainActivity.this);
                        alertResetPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertResetPassword.show();
                        break;

                    case R.id.nav_info:
                        fab.hide();//nascondere fab
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    default:
                        fab.show();//per mostrare fab
                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(item,navController);
                //This is for closing the drawer after acting on it
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public FloatingActionButton getFloatingActionButton(){
        return fab;
    }

    private void setTextUser(TextView txS, TextView txU, ImageView im) {//verifica lo stato dell'utente e la connessione al dispositivo
        txS.setText(auth.getCurrentUser().getDisplayName().toUpperCase());//mostra il nome con cui ci si è registrato
        //txU.setText(auth.getCurrentUser().getEmail());  mostra l'email
        im.setImageResource(R.drawable.ic_connection_fail);//setta l'immagine di connessione al dispositivo

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showAlertDialog() {
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle(R.string.title)
            .setMessage(R.string.message)
            .setIcon(R.drawable.ic_menu_percorso);


        builder.setPositiveButton(R.string.btPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.btAnnulla, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setView(inputText);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=inputText.getText().toString().trim();
                if(input.isEmpty()){
                    inputText.setError(getString(R.string.errorInput));
                }else{
                    setTitle(input);
                    Intent intent=new Intent(MainActivity.this, CreateNewSession.class);
                    intent.putExtra("Title",input);
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            }
        });

    }



}