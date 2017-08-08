package com.example.alpay.learnwithdrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    protected static Bitmap scaleBitmap(PaintView pv, float newWidth, float newHeight)
    {
        Bitmap bm = pv.getDrawingCache();
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    protected static int[] BitmapToPixelArray(Bitmap bp)
    {
        int[] pixels = new int[bp.getHeight() * bp.getWidth()];
        bp.getPixels(pixels, 0, bp.getWidth(), 0, 0, bp.getWidth(), bp.getHeight());
        return pixels;
    }

}
