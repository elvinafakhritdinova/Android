package com.kursach.WeightControl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    RadioGroup radioGroup;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    public static final String APP_PREFERENCES_NAME = "Nickname"; // имя
    public static final String APP_PREFERENCES_AGE = "Age"; // возраст
    public static final String APP_PREFERENCES_HEIGHT = "Height"; // рост
    public static final String APP_PREFERENCES_WEIGHT = "Weight"; // вес
    public static final String APP_PREFERENCES_MALE = "Male"; // вес
    SharedPreferences mSettings;
    EditText editNickname;
    EditText editAge;
    EditText editHeight;
    EditText editWeight;
    RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = findViewById(R.id.radiogroup);
        r1=findViewById(R.id.radioButton);
        r2=findViewById(R.id.radioButton2);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        setSettings();

        LoadPreferences();
    }
    public void setSettings(){
        editNickname = findViewById(R.id.editNickname);
        editHeight=findViewById(R.id.editHeight);
        editAge=findViewById(R.id.editAge);
        editWeight=findViewById(R.id.editWeight);

        if (mSettings.contains(APP_PREFERENCES_NAME)) {
            editNickname.setText(mSettings.getString(APP_PREFERENCES_NAME, ""));
        }
        if (mSettings.contains(APP_PREFERENCES_AGE)) {
            editAge.setText(mSettings.getString(APP_PREFERENCES_AGE, ""));
        }
        if (mSettings.contains(APP_PREFERENCES_HEIGHT)) {
            editHeight.setText(mSettings.getString(APP_PREFERENCES_HEIGHT, ""));
        }
        if (mSettings.contains(APP_PREFERENCES_WEIGHT)) {
            editWeight.setText(mSettings.getString(APP_PREFERENCES_WEIGHT, ""));
        }
    }
    int checkedIndex;
    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
            checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
            Log.i("mylog", String.valueOf(checkedIndex));
            
            SavePreferences(KEY_RADIOBUTTON_INDEX, checkedIndex);
        }
    };

    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(KEY_RADIOBUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }


    public void inMain(View view) {
        saveSettings();
        Intent result = new Intent(this, MainActivity.class);
        //result.putExtra("name", editNickname.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
    public void saveSettings(){
        String strNickName = editNickname.getText().toString();
        String strAge = editAge.getText().toString();
        String strWeight = editWeight.getText().toString();
        String strHeight = editHeight.getText().toString();
        String male= String.valueOf(checkedIndex);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, strNickName);
        editor.putString(APP_PREFERENCES_AGE, strAge);
        editor.putString(APP_PREFERENCES_WEIGHT, strWeight);
        editor.putString(APP_PREFERENCES_HEIGHT, strHeight);
        editor.putString(APP_PREFERENCES_MALE, male);
        editor.apply();
    }

    public void backInMain(View view) {
        saveSettings();
        finish();
    }
}