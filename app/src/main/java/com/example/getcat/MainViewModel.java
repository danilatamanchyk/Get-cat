package com.example.getcat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainViewModel extends AndroidViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    //URLs for JSONs
    private static final String DOG_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String CAT_URL = "https://api.thecatapi.com/v1/images/search";
    //Keys for JSON parsing
    private static final String KEY_MESSAGE_DOG = "message";
    private static final String KEY_STATUS_DOG = "status";
    private static final String KEY_URL = "url";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<DogOrCatImage> dogOrCatImage = new MutableLiveData<>();

    public MutableLiveData<DogOrCatImage> getDogOrCatImage() {
        return dogOrCatImage;
    }

    public void loadDogImage(){
        Disposable disposable = loadDogImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DogOrCatImage>() {
                    @Override
                    public void accept(DogOrCatImage image) throws Throwable {
                        dogOrCatImage.setValue(image);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadCatImage(){
        Disposable disposable = loadCatImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DogOrCatImage>() {
                    @Override
                    public void accept(DogOrCatImage image) throws Throwable {
                        dogOrCatImage.setValue(image);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {

                    }
                });
        compositeDisposable.add(disposable);
    }

    private Single<DogOrCatImage> loadDogImageRx(){
        return Single.fromCallable(new Callable<DogOrCatImage>() {
            @Override
            public DogOrCatImage call() throws Exception {
                URL url = new URL(DOG_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder data = new StringBuilder();
                String result;

                do {
                    result = bufferedReader.readLine();
                    if (result != null) {
                        data.append(result);
                    }
                } while (result != null);

                JSONObject jsonObject = new JSONObject(data.toString());
                String message = jsonObject.getString(KEY_MESSAGE_DOG);
                String status = jsonObject.getString(KEY_STATUS_DOG);
                DogOrCatImage image = new DogOrCatImage(message, status);
                return image;
            }
        });
    }
    private Single<DogOrCatImage> loadCatImageRx(){
        return Single.fromCallable(new Callable<DogOrCatImage>() {
            @Override
            public DogOrCatImage call() throws Exception {
                URL url = new URL(CAT_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder data = new StringBuilder();
                String result;

                do {
                    result = bufferedReader.readLine();
                    if (result != null) {
                        data.append(result);
                    }
                } while (result != null);

                JSONArray jsonArray = new JSONArray(data.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String message = jsonObject.getString(KEY_URL);
                String status;
                if (!(message.isEmpty())) {
                    status = "success";
                } else {
                    status = "something went wrong";
                }
                DogOrCatImage image = new DogOrCatImage(message, status);
                return image;
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
