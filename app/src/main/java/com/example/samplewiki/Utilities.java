package com.example.samplewiki;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samplewiki.activity.MainActivity;

/**
 * Created by Mita on 8/19/2017.
 */

public class Utilities {
    private static Toast toast;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showToast(Context context, String str) {
        if (context != null) {
            if(toast != null)
                toast.cancel();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //   Inflate the Layout
            View layout = inflater.inflate(R.layout.toast_layout, null);
            TextView toasttext = (TextView) layout.findViewById(R.id.toast_text);
            LinearLayout linearlayouttoast = (LinearLayout) layout.findViewById(R.id.linearlayout_toast);
            linearlayouttoast.getBackground().setAlpha(230);
            toasttext.setText(str);
            toast = new Toast(context);
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
            toast.show();
        }
    }
}
