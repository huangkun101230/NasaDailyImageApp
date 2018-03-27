package nz.ac.vuw.ecs.nasadailyimage.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.text.StaticLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yiming on 11/08/16.
 */
public class PictureUtils {
    public static Bitmap getBitmapFromURL(String src){
        //##Missing##
        // Add implementation for this function to obtain a bitmap from the given image url.
        // Hints:
        // 1. Define URL
        // 2. Make HttpURLConnection
        // 3. set connection and InputStream
        // 4. Use BitmapFactory.decodeStream to get the bitmap, then return.
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }
}
