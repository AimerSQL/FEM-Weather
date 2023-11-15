package es.upm.miw.weather.realtime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.google.firebase.database.FirebaseDatabase;

import es.upm.miw.weather.R;
import es.upm.miw.weather.WeatherHolder;

@SuppressLint("RestrictedApi")
public class RealtimeDbWeatherActivity extends AppCompatActivity {
    @NonNull
    public static Query sWeatherQuery =
            FirebaseDatabase.getInstance("https://proyecto1-1539936240177-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("weather").limitToLast(50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_weather);

    }


    public static void addRealtimeDB(WeatherHistory weatherHistory) {
        sWeatherQuery.getRef().push().setValue(weatherHistory)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("HistoryActivity", "Weather added successfully");
                    } else {
                        Log.e("HistoryActivity", "Failed to add weather", task.getException());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();
        RecyclerView recyclerView =  findViewById(R.id.rvHistoryWeather);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<WeatherHistory> options =
                new FirebaseRecyclerOptions.Builder<WeatherHistory>()
                        .setQuery(sWeatherQuery, WeatherHistory.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<WeatherHistory, WeatherHolder>(options) {
            @Override
            public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new WeatherHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_history_item, parent, false));
            }
            @Override
            protected void onBindViewHolder(@NonNull WeatherHolder holder, int position, @NonNull WeatherHistory weatherHistory) {
                holder.bind(weatherHistory);
            }
        };
    }

}