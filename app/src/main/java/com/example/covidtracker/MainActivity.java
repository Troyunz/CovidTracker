package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.Countrydata;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalRecovered, totalActive, totalDeaths, totalTests;
    private TextView todayConfirm, todayDeaths, todayRecovered;
    private List<Countrydata> list;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        init();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<Countrydata>>() {
                    @Override
                    public void onResponse(Call<List<Countrydata>> call, Response<List<Countrydata>> response) {

                        assert response.body() != null;
                        list.addAll(response.body());
                        Toast.makeText(MainActivity.this, "Worked!", Toast.LENGTH_SHORT).show();

                        for(int i=0; i< list.size(); i++){
                            if(list.get(i).getCountry().equals("India")){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int deaths = Integer.parseInt(list.get(i).getDeaths());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeaths.setText(NumberFormat.getInstance().format(deaths));
                                todayDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.brown)));
                                pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.blue)));
                                pieChart.addPieSlice(new PieModel("Deaths", deaths, getResources().getColor(R.color.pink)));
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Countrydata>> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void init() {
        totalConfirm = findViewById(R.id.totalConfirm);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalActive = findViewById(R.id.totalActive);
        totalTests = findViewById(R.id.totalTests);
        todayConfirm = findViewById(R.id.todayConfirm);
        todayDeaths = findViewById(R.id.todayDeaths);
        todayRecovered = findViewById(R.id.todayRecovered);
        pieChart = findViewById(R.id.pieChart);
    }
}