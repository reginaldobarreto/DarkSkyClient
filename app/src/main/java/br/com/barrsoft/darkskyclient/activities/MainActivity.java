package br.com.barrsoft.darkskyclient.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import br.com.barrsoft.darkskyclient.R;
import br.com.barrsoft.darkskyclient.events.ErrorEvent;
import br.com.barrsoft.darkskyclient.events.WeatherEvent;
import br.com.barrsoft.darkskyclient.models.Currently;
import br.com.barrsoft.darkskyclient.services.WeatherServiceProvider;
import br.com.barrsoft.darkskyclient.utils.UserAlertBuilder;
import br.com.barrsoft.darkskyclient.utils.WeatherIconUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 2;
    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.textViewTemp)
    TextView textViewTemp;
    @BindView(R.id.textViewSummary)
    TextView textViewSummary;
    @BindView(R.id.imageViewSummary)
    ImageView imageViewSummary;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String [] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mapeamento
        ButterKnife.bind(this);

        requisicaoPosicionamento();
        requisicaoPosicionamentoCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, permissions,REQUEST_CODE);
            return;

        }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }else{
            UserAlertBuilder.Alert(this);
        }
    }

    private void requisicaoPosicionamento() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(10);
    }

    private void requisicaoPosicionamentoCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location:locationResult.getLocations()) {
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                    requestCurrentWeather(mLatitude,mLongitude);
                }
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeatherEvent weatherEvent){
        Currently currently = weatherEvent.getWeather().getCurrently();
        textViewTemp.setText(String.valueOf(Math.round( (currently.getTemperature() - 32)/1.8  )));
        textViewSummary.setText(currently.getSummary());
        imageViewSummary.setImageResource(WeatherIconUtil.ICONS.get(currently.getIcon()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent){
        Toast.makeText(this, errorEvent.getErrorMessage(),Toast.LENGTH_LONG).show();
    }

    private void requestCurrentWeather(double latitude, double longitude) {
        Log.e(TAG, "requestCurrentWeather");
        WeatherServiceProvider weatherServiceProvider = new WeatherServiceProvider();
        weatherServiceProvider.getWeather(latitude,longitude);
    }


}