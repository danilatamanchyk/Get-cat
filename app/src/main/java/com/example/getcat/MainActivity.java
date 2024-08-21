package com.example.getcat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ImageView imageViewDogOrCat;
    private Button buttonGetCatOrDog;
    private RadioButton radioButtonCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setViewModelObservers();
        setClickListeners();
        viewModel.loadCatImage();
    }
    private void setViewModelObservers() {
        viewModel.getDogOrCatImage().observe(this, new Observer<DogOrCatImage>(){
            @Override
            public void onChanged(DogOrCatImage dogOrCatImage) {
                Glide.with(MainActivity.this)
                        .load(dogOrCatImage.getMessage())
                        .into(imageViewDogOrCat);
            }
        });
    }
    private void setClickListeners() {
        buttonGetCatOrDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonCat.isChecked()){
                    viewModel.loadCatImage();
                } else{
                    viewModel.loadDogImage();
                }
            }
        });
    }
    private void init(){
        imageViewDogOrCat = findViewById(R.id.imageViewDogOrCat);
        buttonGetCatOrDog = findViewById(R.id.buttonGetDogOrCat);
        radioButtonCat = findViewById(R.id.radioButtonCat);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

}