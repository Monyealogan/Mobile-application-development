package com.example.d308_application.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private double price;
    private int vacationID;
    private String excursionDate;
    private String note;

    public Excursion(int excursionID, String excursionName, double price, int vacationID, String excursionDate) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.price = price;
        this.vacationID = vacationID;
        this.excursionDate = excursionDate;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
    public void setExcursionDate(String excursionDate) {this.excursionDate = excursionDate; }
    public String getExcursionDate() {
        return excursionDate;
    }

    public String getName() {
        return excursionName;
    }
    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }
}
