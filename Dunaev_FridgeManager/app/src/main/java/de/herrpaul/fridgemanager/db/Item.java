package de.herrpaul.fridgemanager.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true) public int id;
    @ColumnInfo(name = "name") public String name;
    @ColumnInfo(name = "expdate") public long lExpDate;

    public Date getExpDate() {
        return new Date(lExpDate);
    }

    public void setExpDate(Date date) {
        lExpDate = date.getTime();
    }
}
