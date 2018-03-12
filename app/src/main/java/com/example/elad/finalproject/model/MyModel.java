package com.example.elad.finalproject.model;

import android.graphics.Bitmap;

/**
 * Created by elad on 1/4/2018.
 */

public class MyModel {
    private FireBaseModel modelFireBase = new FireBaseModel();
    private static MyModel model = new MyModel();

    private MyModel(){}

    public static MyModel getInstance(){
        return model;
    }

    public void uploadImage(Bitmap imageBmp, FireBaseModel.UploadImageListener listener) {
        modelFireBase.uploadImage(imageBmp, listener);
    }
}
