package com.example.alpay.learnwithdrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by alpay on 09.07.2017.
 */

public class File_Ops {

    public static void saveImage(PaintView pv, Context context){
        File file = null;
        Bitmap bitmap = pv.getDrawingCache();
        try
        {
            file = File.createTempFile("temp", ".png", context.getCacheDir());
            Toast toast = Toast.makeText(context, "temp.png is created",Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException e) {
            // Error while creating file
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "IO Exception",Toast.LENGTH_SHORT);
            toast.show();
        }

        try
        {
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            Toast toast = Toast.makeText(context, "Saved to Cache",Toast.LENGTH_SHORT);
            toast.show();
            ostream.close();
            pv.invalidate();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "IO Exception",Toast.LENGTH_SHORT);
            toast.show();
        }finally
        {

            pv.setDrawingCacheEnabled(false);
        }
    }

}
