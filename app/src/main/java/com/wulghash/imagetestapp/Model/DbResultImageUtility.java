package com.wulghash.imagetestapp.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DbResultImageUtility {


    public static Uri saveImageToStorage(Bitmap bitmap) {
        File file;
        String path = Environment.getExternalStorageDirectory().toString();
        file = new File(path, bitmap.toString()+".jpg");
        try {
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.parse(file.getAbsolutePath());
    }



    public static void deleteImageFromDir(Uri imageUri) {
        System.out.println(" FILE DELETED ?" + new File(imageUri.toString()).delete());
    }
}
