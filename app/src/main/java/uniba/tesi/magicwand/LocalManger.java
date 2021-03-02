package uniba.tesi.magicwand;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Locale;


//todo: nel caso valuta di usare le shared preferences
public class LocalManger {

    public static Context setLocale(Context context, String language){
            Locale locale=new Locale(language);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            return context;
    }

}
