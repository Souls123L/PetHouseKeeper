package com.example.pethousekeeper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Notes2Fragment extends Fragment {

    private static final String TAG = "Notes2Fragment";

    EditText edTitle, edDesc;
    Button button;
    NotesHelper notesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesHelper = new NotesHelper(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加載 Fragment 的佈局
        return inflater.inflate(R.layout.fragment_notes2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edTitle = view.findViewById(R.id.edTitle); // 確保 ID 正確
        edDesc = view.findViewById(R.id.edDesc);
        button = view.findViewById(R.id.addButton);

        // 設置按鈕點擊事件
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String title = edTitle.getText().toString().trim();
                String desc = edDesc.getText().toString().trim();

                if(!title.isEmpty() && !desc.isEmpty()){
                    notesHelper.insertData(title, desc);
                    Log.d(TAG, "Inserted Data: " + title + ", " + desc);
                    Toast.makeText(requireContext(), "The data Successfully Added", Toast.LENGTH_SHORT).show();
                    edTitle.setText("");
                    edDesc.setText("");
                    // 返回到 NotesFragment，彈出回退棧
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "The Edit Box is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        // 清理任何需要的資源
    }
}