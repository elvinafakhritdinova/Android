package com.kursach.WeightControl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNewNote extends AppCompatActivity {
    Button addBtn, deleteBtn;
    EditText editWeight;
    TextView editDate;
    SQLiteDatabase db;
    Helper databaseHelper;
    CalendarView calendarView;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        addBtn = findViewById(R.id.butAdd);
        deleteBtn = findViewById(R.id.butDel);

        calendarView=findViewById(R.id.calendarView);
        editDate = findViewById(R.id.editDate);
        editWeight = findViewById(R.id.editWeight);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate;
                if(mDay<10&&mMonth<10){
                    selectedDate = new StringBuilder().append(mYear).append("-0").
                            append(mMonth + 1).append("-0").append(mDay).toString();
                }
                else if(mDay<10){
                    selectedDate = new StringBuilder().append(mYear).append("-").
                            append(mMonth + 1).append("-0").append(mDay).toString();
                }
                else if(mMonth<10){
                    selectedDate = new StringBuilder().append(mYear).append("-0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
                else {
                    selectedDate = new StringBuilder().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
               editDate.setText(selectedDate);
            }
        });
        Log.i("mylog", String.valueOf(calendarView.getDate()));
        id = getIntent().getLongExtra("id", 0);
        if (id > 0){
            addBtn.setVisibility(View.INVISIBLE);
            editDate.setText(getIntent().getStringExtra("date"));
            editWeight.setText(getIntent().getStringExtra("weight"));
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        databaseHelper = new Helper(this);
        db = databaseHelper.getReadableDatabase();
    }
    public void addLessonToDB(View view) {
        ContentValues cv = new ContentValues();
        Log.i("mylog", editDate.getText().toString());
        if(!(editDate.getText().toString().equals(""))&&!(editWeight.getText().toString().equals(""))) {
            //Log.i("mylog", String.valueOf(Helper.getNoteByDateCursor(db,editDate.getText().toString())));// {
                cv.put(Helper.COLUMN_DATE, editDate.getText().toString());
                cv.put(Helper.COLUMN_WEIGHT, editWeight.getText().toString());
                db.insert(Helper.TABLE_NOTES, null, cv);
                setResult(RESULT_OK, new Intent());
                finish();
            //}
             //else Toast.makeText(getApplicationContext(), "Данные веса в этот день уже существуют",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(), "Ошибка!Вы не ввели все данные",Toast.LENGTH_SHORT).show();
    }

    public void deleteLessonFromDB(View view) {
        db.delete(Helper.TABLE_NOTES, Helper.COLUMN_ID + "=" + id, null);
        setResult(RESULT_OK, new Intent());
        finish();
    }

    public void backInMain(View view) {
        finish();
    }
}