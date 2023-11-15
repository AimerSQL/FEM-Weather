package es.upm.miw.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import es.upm.miw.weather.realtime.RealtimeDbWeatherActivity;
import es.upm.miw.weather.realtime.WeatherHistory;

public class MainActivity extends AppCompatActivity {

    static final String API_KEY = "0d61ff1e71c1a598c043c09ff365d30a";
    static final String LOG_TAG = "MiW";
    private static final int RC_SIGN_IN = 2019;

    private RequestQueue colaPeticiones;
    private EditText etLatitude;
    private EditText etLongitude;
    private TextView tvResultado;
    private Gson gson;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = this::signIn;

        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        tvResultado = findViewById(R.id.tvResultado);
        gson = new Gson();

        // access Volley instance and get request queue
        SingleVolley volley = SingleVolley.getInstance(getApplicationContext());
        colaPeticiones = volley.getRequestQueue();

        findViewById(R.id.signOutButton).setOnClickListener(view -> signOut());
        findViewById(R.id.historyButton).setOnClickListener(view -> {
            startActivity(new Intent(this, RealtimeDbWeatherActivity.class));
        });

        findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            String latitude = etLatitude.getText().toString();
            String longitude = etLongitude.getText().toString();

            String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat=" + latitude +
                    "&lon=" + longitude + "&appid=" + API_KEY;

            hacerPeticionCentros(apiUrl);
        });
    }

    public void hacerPeticionCentros(String urlRecurso) {
        if (!isValidLatitudeLongitude()) {
            // Show error message or take other actions
            return;
        }

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                urlRecurso,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String respuestaJSON = jsonObject.toString();

                        try {
                            Weather weather = gson.fromJson(jsonObject.toString(), Weather.class);
                            Log.i(LOG_TAG, "Weather: " + weather);

                            if (weather != null) {
                                tvResultado.setText(weather.toString());
                                saveNewWeatherToFirebase(weather);
                            } else {
                                Log.e(LOG_TAG, "Weather is null");
                            }
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(LOG_TAG, volleyError.toString());
                    }
                }
        );
        encolarPeticion(peticion);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (colaPeticiones != null) {
            colaPeticiones.cancelAll(LOG_TAG);
        }
    }

    public void encolarPeticion(JsonObjectRequest peticion) {
        if (peticion != null) {
            peticion.setTag(LOG_TAG);
            peticion.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            3,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            colaPeticiones.add(peticion);
        }
    }

    private void signIn(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Collections.singletonList(
                                    new AuthUI.IdpConfig.EmailBuilder().build()
                            ))
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Log.i(LOG_TAG, "User: " + user.getEmail() + " logged in");
        }
    }

    private void signOut() {
        mFirebaseAuth.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void saveNewWeatherToFirebase(Weather weather) {
        WeatherHistory weatherHistory = new WeatherHistory(
                weather.getLon(),
                weather.getLat(),
                weather.getTimezone(),
                mFirebaseAuth.getCurrentUser().getUid(),
                weather.getCurrent()
        );
        Log.i(LOG_TAG, weatherHistory.toString());
        RealtimeDbWeatherActivity.addRealtimeDB(weatherHistory);
    }

    private boolean isValidLatitudeLongitude() {
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();

        if (latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, "introducir latitud o longitud", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);

            if (lat < -90 || lat > 90) {
                Toast.makeText(this, "latitud invalida", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (lon < -180 || lon > 180) {
                Toast.makeText(this, "longitud invalida", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "formato incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
