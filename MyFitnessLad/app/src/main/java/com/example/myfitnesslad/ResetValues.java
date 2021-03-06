package com.example.myfitnesslad;


import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.InputMismatchException;

public class ResetValues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the activity_reset.xml file
        setContentView(R.layout.activity_reset);
    }

    // When the submit button is pressed, this function will apply the user's values into the
    // respective variables
    public void apply(View view){

        // Grab the fields/elements
        EditText heightField = findViewById(R.id.height);
        EditText weightField = findViewById(R.id.weight);
        EditText ageField = findViewById(R.id.age);
        RadioButton male = findViewById(R.id.radioMale);
        RadioButton female = findViewById(R.id.radioFemale);
        RadioButton activityNone = findViewById(R.id.radioNone);
        RadioButton activity1to3 = findViewById(R.id.radio1to3);
        RadioButton activity4to5 = findViewById(R.id.radio4to5);
        RadioButton activity6to7 = findViewById(R.id.radio6to7);

        TextView errorMessage = findViewById(R.id.error);


        // This will take all the values, ensure that they have been entered as positive integers
        // and apply them accordingly.
        // -Data Validation in effect-

        try {
            int heightValue = Integer.parseInt(heightField.getText().toString());
            int ageValue = Integer.parseInt(ageField.getText().toString());
            float weightValue = Float.parseFloat(weightField.getText().toString());

            String heightString = (heightField.getText().toString());
            String weightString = (weightField.getText().toString());
            String ageString = (ageField.getText().toString());
            String genderValue = "";
            String activityLevel = "";

            if (weightValue <= 0 || heightValue <= 0 || ageValue <= 0) {
                throw new InputMismatchException();
            }

            // Checking and setting Gender
            if (male.isChecked()){
                genderValue = "Male";
            }
            else if (female.isChecked()){
                genderValue = "Female";
            }
            else{
                throw new Exception();
            }

            // Checking and setting Activity Level
            if (activityNone.isChecked()){
                activityLevel = "0";
            }
            else if (activity1to3.isChecked()){
                activityLevel = "1";
            }
            else if (activity4to5.isChecked()){
                activityLevel = "2";
            }
            else if (activity6to7.isChecked()){
                activityLevel = "3";
            }
            else{
                throw new Exception();
            }


            MainActivity.informationEntered = true;
            Toast.makeText(this, "Body Parameters Saved", Toast.LENGTH_SHORT).show();

            // Call the SaveData function to save our variables into the "profile.txt"
            SaveData(heightString, weightString, ageString, genderValue, activityLevel);

            // After Saving, Close/End this activity
            finish();

            // Let's the user's know what went wrong.
        } catch (InputMismatchException e){
            errorMessage.setText("ERROR: Height, weight, and age must be greater than 0.");
        } catch (Exception e){
            errorMessage.setText("ERROR: Some fields have either been left blank, or Height and Weight are not whole numbers.");
        }

    }

    // This function will take all the values associated with our previous function and write them
    // to "profile.txt" file. It will also attach a timestamp to it as well.
    void SaveData(String heightValue, String weightValue, String ageValue, String genderValue, String activityLevel) {

        String strDate = "";
        Date date = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = Calendar.getInstance().getTime();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        strDate = dateFormat.format(date);

        FileOutputStream fos = null;

        try {
            fos = openFileOutput("profile.txt", MODE_PRIVATE);
            fos.write((heightValue + ", ").getBytes());
            fos.write((weightValue + ", ").getBytes());
            fos.write((ageValue + ", ").getBytes());
            fos.write((genderValue + ", ").getBytes());
            fos.write((activityLevel + ", ").getBytes());
            fos.write(("true" + ", ").getBytes());
            fos.write((strDate + ", ").getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
