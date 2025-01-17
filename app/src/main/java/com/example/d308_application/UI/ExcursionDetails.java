package com.example.d308_application.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308_application.Database.Repository;
import com.example.d308_application.R;
import com.example.d308_application.entities.Excursion;
import com.example.d308_application.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String title;
    Double price;
    int excursionID;
    int vacaID;
    String startVacationDate;
    String endVacationDate;
    EditText editName;
    EditText editPrice;
    EditText editNote;
    TextView editDate;
    String excursionDate;
    Repository repository;
    Date startStartDate = null;
    Date endEndDate = null;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository=new Repository(getApplication());
        title = getIntent().getStringExtra("name");
        excursionDate = getIntent().getStringExtra("excursionDate");
        editName = findViewById(R.id.excursionTitle);
        editName.setText(title);
        price = getIntent().getDoubleExtra("price", 0.0);
        editPrice = findViewById(R.id.excursionPrice);
        editPrice.setText(Double.toString(price));
        excursionID = getIntent().getIntExtra("id", -1);
        vacaID = getIntent().getIntExtra("vacationID", -1);
        startVacationDate = getIntent().getStringExtra("startVacationDate");
        endVacationDate = getIntent().getStringExtra("endVacationDate");
        editNote=findViewById(R.id.excursionnote);
        editDate=findViewById(R.id.excursiondate);
        editDate.setText(excursionDate);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        ArrayList<Vacation> vacationArrayList= new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<Integer> vacationIdList= new ArrayList<>();
        for(Vacation vacation:vacationArrayList){
            vacationIdList.add(vacation.getVacationID());
        }
        ArrayAdapter<Integer> vacationIdAdapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,vacationIdList);
        Spinner spinner=findViewById(R.id.spinner);
        spinner.setAdapter(vacationIdAdapter);
        spinner.setSelection(vacaID - 1);

        Log.d("DebugTag", "vacationIdList size: " + vacationIdList.size());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DebugTag", "Selected position: " + position + ", List size: " + vacationIdList.size());
                if (position >= 0 && position < vacationIdList.size()) {
                    vacaID = vacationIdList.get(position);
                } else {
                    Log.e("DebugTag", "Invalid position: " + position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelStart();
            }

        };

        if(startVacationDate != null && endVacationDate != null) {
            try {
                startStartDate = sdf.parse(startVacationDate);
                endEndDate = sdf.parse(endVacationDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ExcursionDetails", "Received null for start or end vacation date");
        }

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                String info=editDate.getText().toString();
                if(info.equals(""))info="10/11/24";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH));

                if (startStartDate != null) {
                    datePickerDialog.getDatePicker().setMinDate(startStartDate.getTime());
                }
                if (endEndDate != null) {
                    datePickerDialog.getDatePicker().setMaxDate(endEndDate.getTime());
                }
                datePickerDialog.show();
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== android.R.id.home){
            this.finish();
            return true;}

        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            Intent resultIntent = new Intent();
            if (excursionID == -1) {
                if (repository.getAllExcursions().size() == 0)
                    excursionID = 1;
                else
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacaID, editDate.getText().toString());
                repository.insert(excursion);
            } else {
                excursion = new Excursion(excursionID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacaID, editDate.getText().toString());
                repository.update(excursion);
            }

            //Updated info
            resultIntent.putExtra("updatedExcursionID", excursionID);
            resultIntent.putExtra("updatedExcursionName", editName.getText().toString());
            resultIntent.putExtra("updatedExcursionPrice", Double.parseDouble(editPrice.getText().toString()));
            resultIntent.putExtra("updatedExcursionDate", editDate.getText().toString());
            setResult(RESULT_OK, resultIntent);

            // Return to homepage
            Intent intent = new Intent(ExcursionDetails.this, VacationList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("excursionSavedMessage", "Your excursion has been saved.");
            startActivity(intent);

            this.finish();
        }

        if (item.getItemId()== R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString()+ "EXTRA)_TEXT");
            sendIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString()+ "EXTRA_TITLE");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if(item.getItemId()== R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", "Excursion Title: " + getIntent().getStringExtra("name"));
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();


        Excursion excursion = repository.getExcursionById(excursionID);

        if (excursion != null) {

            excursion.setNote(editNote.getText().toString());

            repository.update(excursion);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Excursion excursion = repository.getExcursionById(excursionID);

        if (excursion != null) {
            editName.setText(excursion.getExcursionName());
            editPrice.setText(String.valueOf(excursion.getPrice()));
            editDate.setText(excursion.getExcursionDate());
            editNote.setText(excursion.getNote());
        }
    }


}



