package com.alexeymerov.randomusers.presentation.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alexeymerov.randomusers.R;
import com.alexeymerov.randomusers.di.component.ViewModuleComponent;
import com.alexeymerov.randomusers.presentation.base.BaseActivity;
import com.alexeymerov.randomusers.viewmodel.UserViewModel;
import com.alexeymerov.randomusers.viewmodel.factory.UserViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;

    @Inject
    UserViewModelFactory viewModelFactory;
    @BindView(R.id.loginButton)
    View loginButton;
    private UserViewModel mUserViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        unbinder = ButterKnife.bind(this);

        mUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            initGoogleLogin();
            return;
        }
        mUserViewModel.saveUser(account);
        startActivity(UsersListActivity.newInstance(this));
    }

    private void initGoogleLogin() {
        if (mGoogleSignInOptions == null) {
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);
            loginButton.setOnClickListener(view -> signIn());
        }
    }

    @Override
    public void injectDependency(ViewModuleComponent presentersComponent) {
        presentersComponent.inject(this);
    }
}
