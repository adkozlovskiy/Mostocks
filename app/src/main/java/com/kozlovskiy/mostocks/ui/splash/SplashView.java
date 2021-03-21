package com.kozlovskiy.mostocks.ui.splash;

import android.app.Dialog;

public interface SplashView {

    void startMainActivity(String json);

    void showDialog(Dialog dialog);
}
