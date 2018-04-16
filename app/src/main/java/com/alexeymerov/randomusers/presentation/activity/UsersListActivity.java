package com.alexeymerov.randomusers.presentation.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexeymerov.randomusers.R;
import com.alexeymerov.randomusers.data.db.entity.UserEntity;
import com.alexeymerov.randomusers.di.component.ViewModuleComponent;
import com.alexeymerov.randomusers.presentation.adapter.UserListAdapter;
import com.alexeymerov.randomusers.presentation.base.BaseActivity;
import com.alexeymerov.randomusers.viewmodel.UserViewModel;
import com.alexeymerov.randomusers.viewmodel.factory.UserViewModelFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("CheckResult")
public class UsersListActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    UserViewModelFactory viewModelFactory;
    @BindView(R.id.usersRecycler)
    RecyclerView usersRecycler;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private UserViewModel mUserViewModel;
    private ImageView mAvatarImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private GoogleApiClient mGoogleApiClient;

    private UserListAdapter mAdapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, UsersListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        unbinder = ButterKnife.bind(this);

        initializeToolbar(getString(R.string.users_list_title), true, R.drawable.ic_menu);
        initViews();
        initViewModel();
    }

    private void initViewModel() {
        progressBar.setVisibility(View.VISIBLE);
        mUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        LiveData<List<UserEntity>> userList = mUserViewModel.getUserList();
        if (userList.getValue() != null) setData(userList.getValue());
        userList.observe(this, this::setData);
        mUserViewModel.getUser().subscribe(userEntity -> {
            Glide.with(this)
                    .load(userEntity.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mAvatarImageView);
            mNameTextView.setText(userEntity.getName());
            mEmailTextView.setText(userEntity.getEmail());
        });
    }

    private void initViews() {
        View headerView = navigationView.getHeaderView(0);
        mAvatarImageView = headerView.findViewById(R.id.avatarImageView);
        mNameTextView = headerView.findViewById(R.id.nameTextView);
        mEmailTextView = headerView.findViewById(R.id.emailTextView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setData(List<UserEntity> userEntities) {
        if (userEntities.isEmpty()) return;
        if (mAdapter == null) initAdapter();
        mAdapter.setItems(userEntities);
        progressBar.setVisibility(View.GONE);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setMeasurementCacheEnabled(true);
        layoutManager.setItemPrefetchEnabled(true);

        mAdapter = new UserListAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.setOnItemClick(userId -> startActivity(UserInfoActivity.newInstance(this, userId)));

        usersRecycler.setItemViewCacheSize(10);
        usersRecycler.setDrawingCacheEnabled(true);
        usersRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        usersRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        usersRecycler.setLayoutManager(layoutManager);
        usersRecycler.setAdapter(mAdapter);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, android.R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in_short, R.anim.slide_out_to_right);
    }

    @Override
    public void injectDependency(ViewModuleComponent presentersComponent) {
        presentersComponent.inject(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.logout:
                prepareToLogout();
                break;
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private void prepareToLogout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                logout();
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        });

        mGoogleApiClient.connect();
    }

    private void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> {
                    mUserViewModel.clearAllData();
                    startActivity(AuthActivity.newInstance(this));
                    finish();
                });
    }
}
