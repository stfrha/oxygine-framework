package org.oxygine.lib;

import android.content.Intent;
import android.os.Bundle;
import org.libsdl.app.SDLActivity;
import org.oxygine.lib.extension.ActivityObservable;
import org.oxygine.lib.extension.ActivityObserver;

// Below is added by frha from https://stackoverflow.com/questions/41059909/hiding-the-navigation-bar-in-pure-android-native
import android.view.View;

/**
 * OxygineActivity
 */
public class OxygineActivity extends SDLActivity {
    protected static OxygineActivity instance;
    protected ActivityObservable _observable;
    public OxygineActivity() {

        _observable = new ActivityObservable(this);
    }

    public static native void nativeOxygineInit(OxygineActivity activity, Class c);

    public void addObserver(ActivityObserver l) {
        _observable.addObserver(l);
    }

    public ActivityObserver findClass(Class c) {
        return _observable.findClass(c);
    }


    // Below is added by frha from https://stackoverflow.com/questions/41059909/hiding-the-navigation-bar-in-pure-android-native
    void setImmersiveSticky() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Below is added by frha from https://stackoverflow.com/questions/41059909/hiding-the-navigation-bar-in-pure-android-native
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 19) {
            setImmersiveSticky();

            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            setImmersiveSticky();
                        }
                    });
        }

        super.onCreate(savedInstanceState);
        instance = this;
        Utils._context = this;
        nativeOxygineInit(this, getClass());

        _observable.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _observable.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _observable.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        _observable.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        _observable.onStart();
    }

    @Override
    public void onResume() {

        // Below is added by frha from https://stackoverflow.com/questions/41059909/hiding-the-navigation-bar-in-pure-android-native
        //Hide toolbar
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 11 && SDK_INT < 14) {
            getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        } else if (SDK_INT >= 14 && SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else if (SDK_INT >= 19) {
            setImmersiveSticky();
        }

        super.onResume();
        _observable.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        _observable.onPause();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        _observable.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (_observable.onBackPressed())
            super.onBackPressed();
    }
}
