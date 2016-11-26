package com.strajerii.parkingguardian.Activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.strajerii.parkingguardian.R;

import java.util.ArrayList;
import java.util.List;

public class ForeignPlatesActivity extends ListActivity {
    private List<String> listValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_plates);

        listValues = (ArrayList<String>) getIntent().getSerializableExtra("ForeignPlates");

        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
                R.layout.row_layout, R.id.listText, listValues);

        // assign the list adapter
        setListAdapter(myAdapter);
    }
}