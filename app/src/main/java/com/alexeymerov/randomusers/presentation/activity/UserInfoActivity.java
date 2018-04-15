package com.alexeymerov.randomusers.presentation.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.MenuItem;
import android.widget.TextView;

import com.alexeymerov.randomusers.R;
import com.alexeymerov.randomusers.data.db.entity.UserEntity;
import com.alexeymerov.randomusers.di.component.ViewModuleComponent;
import com.alexeymerov.randomusers.presentation.base.BaseActivity;
import com.alexeymerov.randomusers.viewmodel.UserViewModel;
import com.alexeymerov.randomusers.viewmodel.factory.UserViewModelFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class UserInfoActivity extends BaseActivity implements OnMapReadyCallback {

    private static String EXTRA_USER_ID = "extra_user_id";

    @BindView(R.id.testTextView)
    TextView testTextView;

    @Inject
    UserViewModelFactory viewModelFactory;

    private UserViewModel mUserViewModel;
    private GoogleMap mGoogleMap;
    private long mUserId;

    public static Intent newInstance(Context context, long userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        unbinder = ButterKnife.bind(this);
        initializeToolbar(null, true);
        mUserId = getIntent().getLongExtra(EXTRA_USER_ID, 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);

        initViewModel();
    }

    private void initViewModel() {
        mUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        mUserViewModel.getUser(mUserId)
                .subscribe(userEntity -> {
                    setToolbarTitle(userEntity.getName());

                    testTextView.setText(String.format("%s\n%s\n%s\n%s, %s, %s\n%s",
                            userEntity.getName(),
                            userEntity.getEmail(),
                            userEntity.getPhoneNumber(),
                            userEntity.getAddress().getCity(),
                            userEntity.getAddress().getStreet(),
                            userEntity.getAddress().getSuite(),
                            userEntity.getAddress().getZipcode()
                    ));
                });
    }

    private void setUserMarker(UserEntity userEntity) {
        Single.just(userEntity)
                .flatMap(user -> {
                    double lat = user.getAddress().getGeoEntity().getLat();
                    double lng = user.getAddress().getGeoEntity().getLng();
                    LatLng userLocation = new LatLng(lat, lng);

                    MarkerOptions markerOptions = new MarkerOptions().position(userLocation)
                            .title(user.getName());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(userLocation)
//                            .zoom(200)
                            .build();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                    return Single.just(new Pair<>(markerOptions, cameraUpdate));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    if (pair.first != null) mGoogleMap.addMarker(pair.first);
                    if (pair.second != null) mGoogleMap.moveCamera(pair.second);
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mUserViewModel.getUser(mUserId).subscribe(this::setUserMarker);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in_short, R.anim.slide_out_to_right_with_fade);
    }

    @Override
    public void injectDependency(ViewModuleComponent presentersComponent) {
        presentersComponent.inject(this);
    }
}
