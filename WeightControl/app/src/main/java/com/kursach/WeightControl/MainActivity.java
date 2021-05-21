package com.kursach.WeightControl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    String name;
    Double age;
    Double height;
    Double weight;
    Double active_weight;
    Double voda;
    Double male_id;
    TextView recomendation,telo;
    Helper databaseHelper;
    SQLiteDatabase db;
    LineGraphSeries<DataPoint> series;
    GraphView graph;
    Button but7,but30,butAll;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recomendation = findViewById(R.id.recom);
        telo = findViewById(R.id.telo);
        but7=findViewById(R.id.days7);
        but30=findViewById(R.id.days30);
        butAll=findViewById(R.id.daysAll);
        butAll.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background1));
        createGraph("all");
        installSettingsInMainActivity();
    }
    private void createGraph(String day) {
        try {
            graph = (GraphView) findViewById(R.id.graph);
            series = new LineGraphSeries<DataPoint>();
            graph.removeSeries(series);
            databaseHelper = new Helper(this);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = Helper.getAllNotesCursor(db);
            switch (day) {
                case ("all"):
                    cursor = Helper.getAllNotesCursor(db);
                    break;
                case ("7"):
                    cursor = Helper.getAllNotesCursorUp7(db);
                    break;
                case ("30"):
                    cursor = Helper.getAllNotesCursorUp30(db);
                    break;
                default:
                    break;
            }
            int idColumnIndex = cursor.getColumnIndex(Helper.COLUMN_ID);
            int dateColumnIndex = cursor.getColumnIndex(Helper.COLUMN_DATE);
            int weightColumnIndex = cursor.getColumnIndex(Helper.COLUMN_WEIGHT);
            // Проходим через все ряды
            ArrayList<Double> t = new ArrayList();
            ArrayList<Date> dates = new ArrayList<>();
            int i = 0;
            Double currentWeight = weight;
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                currentWeight = cursor.getDouble(weightColumnIndex);
                t.add(currentWeight);
                i++;
            }
            switch (day) {
                case ("all"):
                    break;
                case ("7"):
                    Collections.reverse(t);
                    break;
                case ("30"):
                    Collections.reverse(t);
                    break;
                default:
                    break;
            }
                active_weight = t.get(t.size() - 1);
            for (int j = 0; j < t.size(); j++) {
                series.appendData(new DataPoint(j, (Double) t.get(j)), true, t.size());
            }
            graph.addSeries(series);
            //graph.getViewport().setScrollable(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMaxY(Collections.max(t) + 5);
            graph.getViewport().setMinY(Collections.min(t) - 5);
            graph.getViewport().setMaxX(i - 1);
            graph.getViewport().setMinX(0);
            //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
            //graph.getViewport().setXAxisBoundsManual(false);
            //graph.getViewport().setScalable(true);
            //graph.getViewport().setScalableY(true);

            series.setColor(Color.rgb(255, 0, 0));
            series.setThickness(6);
            //series.setDrawBackground(true);
            //series.setBackgroundColor(Color.argb(60, 95, 226, 156));
            series.setDrawDataPoints(true);
            series.getDataPointsRadius();
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    String msg = "" + dataPoint.getY();
                    Toast mytoast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.TOP, 0, 0);  // for top
                    mytoast.show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Добавьте записи о изменении веса", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        graph.removeSeries(series);
        Log.i("mylog", "onActivityResult");
        Log.i("mylog", String.valueOf(requestCode));
        Log.i("mylog", String.valueOf(resultCode));
        if (requestCode == 1) {
            graph.removeSeries(series);
            createGraph("all");
            installSettingsInMainActivity();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void installSettingsInMainActivity() {
        mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        try {
            if (mSettings.contains("Nickname")) {
                name = mSettings.getString("Nickname", "");
            }
            if (mSettings.contains("Age")) {
                age = Double.parseDouble(mSettings.getString("Age", ""));
            }
            if (mSettings.contains("Height")) {
                height = Double.parseDouble(mSettings.getString("Height", ""));
            }
            if (mSettings.contains("Weight")) {
                weight = Double.parseDouble(mSettings.getString("Weight", ""));
            }
            if (mSettings.contains("Male")) {
                male_id = Double.parseDouble(mSettings.getString("Male", ""));
            }
            //Toast.makeText(getApplicationContext(), String.valueOf(KKal(170.0,60.0,1.0,20.0)),Toast.LENGTH_SHORT).show();
            try {
                voda = active_weight * 0.03;
            }catch (Exception e){
                active_weight=weight;
                voda = active_weight * 0.03;
            }
            telo.setText(information(IMT(height, active_weight)));
            recomendation.setText("Индекс массы тела: " + String.format("%.1f", IMT(height, active_weight)) +
                    "\nВ день нужно выпить не менее " + String.format("%.1f", voda) + " литров воды\n" +
                    "Дневная норма составляет " +
                    String.format("%.1f", KKal(height, active_weight, male_id, age))+" Ккал");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Введите все значения в настройках", Toast.LENGTH_SHORT).show();
        }
    }

    //Формула расчета суточных энерготрат на основной обмен с учетом веса, пола, роста и возраста
    public Double KKal(Double rost, Double ves, Double male, Double age) {
        if (male == 0.0) {
            return 66 + 13.7 * ves + 5 * rost - 6.8 * age;
        }
        return 655 + 9.6 * ves + 1.8 * rost - 4.7 * age;
    }

    public double IMT(Double rost, Double ves) {
        return ves / ((rost / 100) * (rost / 100));
    }
    public String information(double imt){
        if(imt<=16)return "Выраженный дефицит массы";
        if(imt<=18.5)return "Недостаточная масса тела";
        if(imt<=25)return "Норма веса";
        if(imt<=30)return "Избыточная масса тела";
        if(imt<=35)return "Ожирение первой степени";
        if(imt<=40)return "Ожирение второй степени";
        if(imt>40)return "Ожирение третьей степени";
        return null;
    }
    public void inSettings(View view) {
        Intent i = new Intent(MainActivity.this, Settings.class);
        startActivityForResult(i, 1);
    }

    public void inAdd(View view) {
        Intent i = new Intent(MainActivity.this, AddNewNote.class);
        startActivityForResult(i, 1);
    }

    public void inAllNotes(View view) {
        Intent i = new Intent(MainActivity.this, AllNotes.class);
        startActivityForResult(i, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void graphView7(View view) {
        graph.removeSeries(series);
        butAll.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        but7.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background1));
        but30.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        createGraph("7");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void graphView30(View view) {
        graph.removeSeries(series);
        butAll.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        but7.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        but30.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background1));
        createGraph("30");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void graphViewAll(View view) {
        graph.removeSeries(series);
        butAll.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background1));
        but7.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        but30.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.background));
        createGraph("all");
    }
}
