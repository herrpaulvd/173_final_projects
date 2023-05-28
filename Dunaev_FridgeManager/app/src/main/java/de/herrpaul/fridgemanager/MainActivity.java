package de.herrpaul.fridgemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.herrpaul.fridgemanager.db.AppDatabase;
import de.herrpaul.fridgemanager.db.Item;
import de.herrpaul.fridgemanager.db.ItemDao;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private ItemDao dao;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private void spawnMain() {
        setContentView(R.layout.activity_main);
        ViewGroup root = (ViewGroup) findViewById(R.id.lvAll);

        List<Item> all = dao.getAll();
        Date now = new Date();
        LayoutInflater inflater = getLayoutInflater();
        for (final Item i : all) {
            View view = inflater.inflate(R.layout.item_linear_layout, null);
            root.addView(view);
            TextView tvName = view.findViewById(R.id.tvName);
            TextView tvDate = view.findViewById(R.id.tvDate);
            Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
            Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
            tvName.setText(i.name);
            tvDate.setText(sdf.format(i.getExpDate()));

            if(i.getExpDate().compareTo(now) <= 0) {
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.expired));
                btnDelete.setText(R.string.btnTrashImage);
            }

            btnDelete.setOnClickListener(v -> {
                dao.delete(i);
                view.setVisibility(View.GONE);
            });

            btnEdit.setOnClickListener(v -> {
                spawnEdit(i, true);
            });
        }

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Item newItem = new Item();
            newItem.name = "Tasty";
            newItem.setExpDate(now);
            spawnEdit(newItem, false);
        });

        TextView tvToday = findViewById(R.id.tvToday);
        tvToday.setText(String.format("Today: %s", sdf.format(now)));
    }

    private void spawnEdit(Item i, boolean editMode) {
        setContentView(R.layout.edit_layout);
        EditText etName = (EditText) findViewById(R.id.etName);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAdd.setText(editMode ? R.string.btnEditText : R.string.btnAddText);
        etName.setText(i.name);

        btnCancel.setOnClickListener(v -> spawnMain());
        btnAdd.setOnClickListener(v -> {
            i.name = etName.getText().toString();
            Calendar c = Calendar.getInstance();
            c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            i.setExpDate(c.getTime());
            if(editMode)
                dao.update(i);
            else
                dao.insert(i);
            spawnMain();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "fridgedb")
                .allowMainThreadQueries()
                .build();
        dao = db.itemDao();

        spawnMain();
    }
}