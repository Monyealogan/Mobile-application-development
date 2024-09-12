package com.example.d308_application.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_application.Database.Repository;
import com.example.d308_application.R;
import com.example.d308_application.entities.Excursion;
import com.example.d308_application.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    String name;
    double price;
    int vacationID;
    EditText editName;
    EditText editPrice;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            //Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
           // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            //return insets;
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);

        editName = findViewById(R.id.titletext);
        editPrice = findViewById(R.id.pricetext);
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        editName.setText(name);
        editPrice.setText(Double.toString(price));
        vacationID = getIntent().getIntExtra("id", -1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        //System.out.println(getIntent().getStringExtra("test"));
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion p : repository.getAllExcursions()) {
            if (p.getVacationID() == vacationID) filteredExcursions.add(p);
        }
        excursionAdapter.setExcursions(filteredExcursions);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            this.finish();
            return true;}
        if(item.getItemId()== R.id.vacationsave) {
            Vacation vacation;
            if (vacationID==-1) {
                if (repository.getAllVacations().size() == 0) vacationID = 1;
                else
                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.insert(vacation);
                this.finish();
            }
            else{
                vacation = new Vacation(vacationID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.update(vacation);
                this.finish();
            }
        }
        return true;
    }
    }
