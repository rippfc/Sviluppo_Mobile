package uniba.tesi.magicwand.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.preference.PreferenceManager;


import java.util.Locale;

public class LocaleManager {
    /**
     * Debug tag
     */
    public static final String TAG = LocationManager.class.getName();

    public static final String ENGLISH = "en";
    public static final String ITALIAN = "it";

    /**
     * SharedPreferences Key
     */
    private static final String LANGUAGE_KEY = "language_key";

    /**
     * set current pref locale
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     */
    public static Context setNewLocale(Context mContext, String language) {
        setLanguagePref(mContext, language);
        return updateResources(mContext, language);
    }

    /**
     * Get saved Locale from SharedPreferences
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mPreferences.getString(LANGUAGE_KEY,Locale.getDefault().getLanguage() );
    }

    /**
     * set pref key
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).apply();
    }

    /**
     * update resource
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());

        return context;
    }


}