package uniba.tesi.magicwand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import uniba.tesi.magicwand.Aut_Controller.Login;
import uniba.tesi.magicwand.Create_Quiz.CreateNewSession;
import uniba.tesi.magicwand.Utils.DialogResetPassword;
import uniba.tesi.magicwand.Utils.LocaleManager;
import uniba.tesi.magicwand.ui.Play;

public class MainActivity extends AppCompatActivity {
    /**
     * Debug tag
     */
    public static final String TAG = MainActivity.class.getName();

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private TextView user,status;
    private ImageView image;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private View header;
    private DrawerLayout drawer;
    private NavController navController;

    private FusedLocationProviderClient mFusedLocationClient;
    private String latitude;
    private String longitudine;
    private int PERMISSION_ID = 1;


    public static String CURRENT_SESSION;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleManager.setLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        auth= FirebaseAuth.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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




        getLastLocation();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//azione pulsante
                switch (navController.getCurrentDestination().getId()){
                    case R.id.fragmentShowSession:
                        Toast.makeText(MainActivity.this, latitude+"\t"+longitudine, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(MainActivity.this, Play.class);
                        intent.putExtra("Session",CURRENT_SESSION);
                        intent.putExtra("User",auth.getCurrentUser().getDisplayName());
                        intent.putExtra("Latitude",latitude);
                        intent.putExtra("Longitudine",longitudine);
                        startActivity(intent);
                        break;
                    default:
                         showAlertDialog();
                }
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "Inflating menu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String[] listItems = new String[] { getResources().getString(R.string.lingua_inglese), getResources().getString(R.string.lingua_italiana)};
        if (item.getItemId()== R.id.action_settings){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                            setNewLocale(MainActivity.this, LocaleManager.ENGLISH);
                        break;
                    case 1:
                            setNewLocale(MainActivity.this, LocaleManager.ITALIAN);
                        break;
                }
            }
        });
        builder.setNeutralButton(R.string.btAnnulla, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public FloatingActionButton getFloatingActionButton(){
        return fab;
    }

    private void setTextUser(TextView txS, TextView txU, ImageView im) {//verifica lo stato dell'utente e la connessione al dispositivo
        txS.setText(auth.getCurrentUser().getDisplayName().toUpperCase());//mostra il nome con cui ci si è registrato
        //txU.setText(auth.getCurrentUser().getEmail());  mostra l'email
        im.setImageResource(R.drawable.ic_connection_fail);//setta l'immagine di connessione al dispositivo

    }

    private void setNewLocale(Context mContext, String language) {
        if (Locale.getDefault().getLanguage().equals(language)){//todo: verifica nel metodo
            Toast.makeText(MainActivity.this, R.string.language_select, Toast.LENGTH_SHORT).show();
        }else{
            LocaleManager.setNewLocale(this, language);
            recreate();
        }
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


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                Log.d(TAG, "location is enable");
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude=String.valueOf(location.getLatitude());
                            longitudine=String.valueOf(location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, R.string.turn_on, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        Log.d(TAG, "Initializing LocationRequest" );
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000); // 10 seconds
        mLocationRequest.setFastestInterval(5 * 1000); // 5 seconds

        Log.d(TAG, "setting LocationRequest");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude=String.valueOf(mLastLocation.getLatitude());
            longitudine=String.valueOf(mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.d(TAG,"method to request for permissions");
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

}