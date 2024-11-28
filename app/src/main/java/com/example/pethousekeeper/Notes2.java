package com.example.pethousekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Notes2 extends AppCompatActivity {
EditText edTitle, edDesc;
Button button;

NotesHelper notesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes2);

        edTitle = findViewById(R.id.edTitile);
        edDesc = findViewById(R.id.edDesc);
        button = findViewById(R.id.addButton);
        notesHelper = new NotesHelper(Notes2.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edTitle.length()>0&&edDesc.length()>0){

                    notesHelper.insertData(edTitle.getText().toString(),edDesc.getText().toString());
                    Toast.makeText(Notes2.this,"The data Successfull Added",Toast.LENGTH_SHORT).show();
                    edDesc.setText("");
                    edTitle.setText("");
                    startActivity(new Intent(Notes2.this,NotesActivity.class));
                } else {
                    Toast.makeText(Notes2.this,"The Edit Box is empty",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}