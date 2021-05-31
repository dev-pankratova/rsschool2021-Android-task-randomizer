package com.rsschool.android2021;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    FirstFragment firstFragment;
    SecondFragment secondFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFirstFragment(0);
    }

    private void openFirstFragment(int previousNumber) {
        firstFragment = FirstFragment.newInstance(previousNumber);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, firstFragment);
        transaction.commit();

        firstFragment.setInterface(new MinMaxInterface() {
            @Override
            public void setValues(@NotNull String min, @NotNull String max) {
                handleInputs(min, max);
            }
        });
    }

    private void openSecondFragment(int min, int max) {
        secondFragment = SecondFragment.newInstance(min, max);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, secondFragment, "second");
        transaction.commit();

        secondFragment.setInterface(new PreviousValueInterface() {
            @Override
            public void givePrevious(int value) {
                openFirstFragment(value);
            }
        });
    }

    private void handleInputs(String min, String max) {
        try {
            if (min.equals("") && max.equals("")) {
                Toast.makeText(this, "Введите оба значения", Toast.LENGTH_SHORT).show();
            } else if (max.equals("")) {
                Toast.makeText(this, "Введите максимальное значение", Toast.LENGTH_SHORT).show();
            } else if (min.equals("")) {
                Toast.makeText(this, "Введите минимальное значение", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(min) > Integer.parseInt(max)) {
                Toast.makeText(this, "Минимальное значение не должно быть больше максимального", Toast.LENGTH_SHORT).show();
            } else {
                openSecondFragment(Integer.parseInt(min), Integer.parseInt(max));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Указано недопустимое значение в одном из полей", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("second") != null) {
            secondFragment.givePreviousValue();
        } else {
            super.onBackPressed();
        }
    }
}
