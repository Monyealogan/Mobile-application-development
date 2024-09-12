package com.example.d308_application.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_application.Database.Repository;
import com.example.d308_application.R;
import com.example.d308_application.entities.Excursion;
import com.example.d308_application.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            //Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            //return insets;
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        repository= new Repository(getApplication());
        List<Vacation> allVacations=repository.getAllVacations();
        final VacationAdapter vacationAdapter=new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
        //System.out.println(getIntent().getStringExtra("test"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter= new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        repository=new Repository(getApplication());
        if(item.getItemId()==R.id.mysample) {
          //  Toast.makeText(VacationList.this,"put in sample data", Toast.LENGTH_LONG).show();
            Vacation vacation=new Vacation(0, "Camping vacation", 100.0);
            repository.insert(vacation);
            vacation = new Vacation(0,"London", 600.0);
            repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Snorkeling", 200.0, 1);
            repository.insert(excursion);
            excursion = new Excursion(0, "Boat Tour", 400.0, 1);
            repository.insert(excursion);
            return true;
        }
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
}