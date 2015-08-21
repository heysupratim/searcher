package supratim.com.searcher.utilites;


import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import supratim.com.searcher.constants.Constants;

/**
 * Created by borax12 on 20/08/15.
 */
public class Utilities {

    public static void showToast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Typeface getCustomFontTypeFace(Context context,
                                                 String fontName) {
        HashMap<String, Typeface> customFontCache = Constants.customFontCache;

        if (customFontCache == null) {
            customFontCache = new HashMap<String, Typeface>();
            Constants.customFontCache = customFontCache;
        }

        if (customFontCache.containsKey(fontName)) {
            return customFontCache.get(fontName);
        } else {
            Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + fontName);
            customFontCache.put(fontName, typeFace);
            return typeFace;
        }
    }


//Write this function in your activity -

    public static Properties loadPropties(Context context) throws IOException {
        String[] fileList = { "local.properties" };
        Properties prop = new Properties();
        for (int i = fileList.length - 1; i >= 0; i--) {
            String file = fileList[i];
            try {
                InputStream fileStream = context.getAssets().open(file);
                prop.load(fileStream);
                fileStream.close();
            }  catch (FileNotFoundException e) {
                Log.e("Property file Exception", "Got exception " + e);
            }
        }
        return prop;
    }
}
