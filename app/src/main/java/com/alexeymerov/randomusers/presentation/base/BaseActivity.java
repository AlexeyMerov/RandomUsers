package com.alexeymerov.randomusers.presentation.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alexeymerov.randomusers.App;
import com.alexeymerov.randomusers.R;
import com.alexeymerov.randomusers.di.component.ViewModuleComponent;

import java.util.Locale;

import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        createDaggerDependencies();
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "uk");
        changeLang(language);
    }

    @SuppressLint("ApplySharedPref")
    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase("")) return;
        Locale locale = lang.equals("uk") ? new Locale("uk", "UA") : new Locale("ru", "RU");
        saveLocale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void createDaggerDependencies() {
        injectDependency(((App) getApplication()).getPresentersComponent());
    }

    public abstract void injectDependency(ViewModuleComponent presentersComponent);

    protected void initializeToolbar(String title) {
        initializeToolbar(title, false);
    }

    protected void initializeToolbar(String title, boolean showBackButton) {
        initializeToolbar(title, showBackButton, -1);
    }

    protected void initializeToolbar(String title, boolean showBackButton, @DrawableRes int backIcon) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (!TextUtils.isEmpty(title)) getSupportActionBar().setTitle(title);
                if (showBackButton) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    if (backIcon != -1) getSupportActionBar().setHomeAsUpIndicator(backIcon);
                }
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    public Context getContext() {
        return this;
    }

    @Nullable
    protected Toolbar getToolbar() {
        return toolbar;
    }


    protected void setToolbarTitle(CharSequence title) {
        setToolbarTitle(title.toString());
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            View newTouchedView = findViewAt((ViewGroup) v.getRootView(), (int) event.getRawX(), (int) event.getRawY());
            if (newTouchedView instanceof EditText) return super.dispatchTouchEvent(event);

            Rect outRect = new Rect();
            v.getGlobalVisibleRect(outRect);
            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) hideKeyboard();
        }

        return super.dispatchTouchEvent(event);
    }

    private View findViewAt(ViewGroup viewGroup, int x, int y) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                View foundView = findViewAt((ViewGroup) child, x, y);
                if (foundView != null && foundView.isShown()) return foundView;
            } else {
                int[] location = new int[2];
                child.getLocationOnScreen(location);
                Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(), location[1] + child.getHeight());
                if (rect.contains(x, y)) return child;
            }
        }
        return null;
    }

    protected void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
//            DLog.e(e);
        }
    }
}
