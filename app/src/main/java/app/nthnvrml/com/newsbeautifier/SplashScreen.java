package app.nthnvrml.com.newsbeautifier;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by vermel on 13/03/16.
 */
public class SplashScreen extends Activity {
    private static final int STOPSPLASH = 10;
    private static final long SPLASHTIME = 5000;

    Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    if (isConnected()) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    } else {
                        final AlertDialog.Builder builder=new AlertDialog.Builder(SplashScreen.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.not_connected));
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    }

            }
            super.handleMessage(msg);
        }
    };

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}
