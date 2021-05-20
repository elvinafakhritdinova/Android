package com.kursach.WeightControl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AllNotes extends AppCompatActivity {
    ListView listView;
    Cursor noteCursor;
    MySimpleCursorAdapter noteAdapter;
    Helper databaseHelper;
    SQLiteDatabase db;
    RadioGroup radioGroup;
    RadioButton r1,r2,r3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        Log.i("mylog", "AllNites");
        listView = findViewById(R.id.listview);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        r1 = (RadioButton)findViewById(R.id.notes7);
        r1.setOnClickListener(radioButtonClickListener);
        r2 = (RadioButton)findViewById(R.id.notes30);
        r2.setOnClickListener(radioButtonClickListener);
        r3 = (RadioButton)findViewById(R.id.notesall);
        r3.setOnClickListener(radioButtonClickListener);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, AddNewNote.class);
            i.putExtra("id", id);
            TextView counterText = view.findViewById(R.id.data);
            i.putExtra("date", counterText.getText());
            TextView typeText = view.findViewById(R.id.ves);
            i.putExtra("weight", typeText.getText());
            startActivityForResult(i, 1);
        });
        Log.i("mylog", "AllNites2");
        setDB();

    }
    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.notes7: noteCursor=Helper.getAllNotesCursorUp7(db);
                    break;
                case R.id.notes30: noteCursor=Helper.getAllNotesCursorUp30(db);
                    break;
                case R.id.notesall: noteCursor=Helper.getAllNotesCursor(db);
                    break;
                default:
                    break;
            }
            setListView1();

        }
    };

    private void setListView1() {
        noteAdapter = new MySimpleCursorAdapter(
                this, noteCursor, Helper.headersWithIdAll, Helper.guiListAll,
                0);

        listView.setAdapter(noteAdapter);
    }

    private void setDB() {
        //открыть подключение к бд
        Log.i("mylog", "AllNites3");
        databaseHelper = new Helper(this);
        Log.i("mylog", "AllNites4");
        db = databaseHelper.getReadableDatabase();
        Log.i("mylog", "setlist");
        //обновить list view
        setListView();
    }

    private void setListView() {
        //получить данные в курсор
        noteCursor = Helper.getAllNotesCursorUp(db);
        Log.i("mylog", "lessad");
        //создаем адаптер и помещаем в него курсор
        noteAdapter = new MySimpleCursorAdapter(
                this, noteCursor, Helper.headersWithIdAll, Helper.guiListAll,
                0);
        Log.i("mylog", "setAd");
        //listview
        listView.setAdapter(noteAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        noteCursor.close();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setListView();
            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void backInMain(View view) {
        finish();
    }

    public void inAdd(View view) {
        Intent i = new Intent(AllNotes.this, AddNewNote.class);
        startActivityForResult(i, 1);
    }
}