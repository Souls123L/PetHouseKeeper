package com.example.pethousekeeper;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalculatorFragment extends Fragment {

    private EditText editWeightCat, editWaterCat;
    private Button btnCalculateCat, btnClearCat;

    private EditText editWeightDog, editWaterDog;
    private Button btnCalculateDog, btnClearDog;

    // Calculate water intake
    private final double CAT_WATER_PER_KG = 40; // 貓咪每公斤體重飲水量 (ml)
    private final double DOG_WATER_PER_KG = 55; // 狗狗每公斤體重飲水量 (ml)

    public CalculatorFragment() {
        // 必須的空構造方法
    }

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加載片段的布局
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化貓咪的 UI 元件
        editWeightCat = view.findViewById(R.id.edit_weight_cat);
        editWaterCat = view.findViewById(R.id.edit_water_cat);
        btnCalculateCat = view.findViewById(R.id.btn_calculate_cat);
        btnClearCat = view.findViewById(R.id.btn_clear_cat);

        // 初始化狗狗的 UI 元件
        editWeightDog = view.findViewById(R.id.edit_weight_dog);
        editWaterDog = view.findViewById(R.id.edit_water_dog);
        btnCalculateDog = view.findViewById(R.id.btn_calculate_dog);
        btnClearDog = view.findViewById(R.id.btn_clear_dog);

        // 設置貓咪計算按鈕監聽器
        btnCalculateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weightInput = editWeightCat.getText().toString();

                if (weightInput.isEmpty()) {
                    Toast.makeText(getContext(), "please input cat's weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double weight = Double.parseDouble(weightInput);

                    // 計算每日飲水量
                    double water = weight * CAT_WATER_PER_KG;

                    // 顯示結果，保留兩位小數
                    editWaterCat.setText(String.format("%.2f", water));

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please input vaild number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 設置貓咪清除按鈕監聽器
        btnClearCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editWeightCat.setText("");
                editWaterCat.setText("");
            }
        });

        // 設置狗狗計算按鈕監聽器
        btnCalculateDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weightInput = editWeightDog.getText().toString();

                if (weightInput.isEmpty()) {
                    Toast.makeText(getContext(), "Please input dog's weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double weight = Double.parseDouble(weightInput);

                    // 計算每日飲水量
                    double water = weight * DOG_WATER_PER_KG;

                    // 顯示結果，保留兩位小數
                    editWaterDog.setText(String.format("%.2f", water));

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please input vaild number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 設置狗狗清除按鈕監聽器
        btnClearDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editWeightDog.setText("");
                editWaterDog.setText("");
            }
        });
    }
}