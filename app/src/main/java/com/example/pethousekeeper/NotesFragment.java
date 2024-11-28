package com.example.pethousekeeper;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private static final String TAG = "NotesFragment";

    FloatingActionButton floatingId;
    RecyclerView recyclerView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter notesAdapter;
    NotesHelper notesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        notesHelper = new NotesHelper(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加載 Fragment 的佈局
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingId = view.findViewById(R.id.floatingId);
        recyclerView = view.findViewById(R.id.recyclerView);

        // 設置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter(arrayList);
        recyclerView.setAdapter(notesAdapter);

        // 加載數據
        loadNotes();

        // 設置 FloatingActionButton 的點擊事件
        floatingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotes2Fragment();
            }
        });
    }

    // 加載筆記數據的方法
    private void loadNotes(){
        arrayList.clear(); // 清除現有數據以防重複
        Cursor cursor = notesHelper.showData();
        if(cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesHelper.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(NotesHelper.COLUMN_DESCRIPTION));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesHelper.COLUMN_ID));
                arrayList.add(new NotesModel(title, desc, id));
                Log.d(TAG, "Loaded Data: " + title + ", " + desc);
            }
            cursor.close();
        }
        notesAdapter.notifyDataSetChanged(); // 通知 Adapter 數據已更改
    }

    @Override
    public void onResume(){
        super.onResume();
        loadNotes(); // 當 Fragment 重新顯示時重新加載數據
    }

    // 開啟 Notes2Fragment 的方法
    private void openNotes2Fragment() {
        Notes2Fragment notes2Fragment = new Notes2Fragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, notes2Fragment)
                .addToBackStack(null) // 添加到回退棧，允許用戶按返回鍵返回
                .commit();
    }
}