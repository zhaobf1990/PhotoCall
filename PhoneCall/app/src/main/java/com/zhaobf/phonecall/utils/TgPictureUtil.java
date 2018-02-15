package com.zhaobf.phonecall.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ccy on 2015/4/8.
 */
public class TgPictureUtil {
    public static void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

//    public static String transImage(String fromFile, int width, int height, int quality)
//    {
//        try
//        {
//            File toFile= TgPictureUtil.getTSignPicTmpFile();
//            if(toFile==null)
//                return fromFile;
//            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
//            if(bitmap==null) return null;
//            int bitmapWidth = bitmap.getWidth();
//            int bitmapHeight = bitmap.getHeight();
//            // 缩放图片的尺寸
//            height=(width*bitmapHeight)/bitmapWidth;
//            float  scaleWidth = (float) width / bitmapWidth;
//            float   scaleHeight = (float) height / bitmapHeight;
//
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            // 产生缩放后的Bitmap对象
//            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
//            // save file
//            FileOutputStream out = new FileOutputStream(toFile);
//            if(resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
//                out.flush();
//                out.close();
//            }
//            if(!bitmap.isRecycled()){
//                bitmap.recycle();//记得释放资源，否则会内存溢出
//            }
//            if(!resizeBitmap.isRecycled()){
//                resizeBitmap.recycle();
//            }
//        return toFile.getPath();
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            return fromFile;
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//            return fromFile;
//        }
//    }


    /**
     * Get the directory for the user's public pictures directory.
     *
     * @param albumName
     * @return
     */
    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists()) {
            if (!file.mkdirs())
                return null;
        }
        return file;
    }

    public  static  File getPhoneFile() {
        final String dir = getTSignPicRootPath("/phoneCall/");

        File newDir = new File(dir);
        newDir.mkdirs();
        String mCameraPath = dir  + "phont.txt";
        File newFile = new File(mCameraPath);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    public static File getTSignPicTmpFile() {
        final String dir = getTSignPicRootPath("/phoneCall/pic/");

        File newDir = new File(dir);
        newDir.mkdirs();
        String mCameraPath = dir + System.currentTimeMillis() + ".png";
        File newFile = new File(mCameraPath);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    public static String getTSignPicRootPath( String path) {
        String imgRootPath = Environment.getExternalStorageDirectory() + path;
        File file = new File(imgRootPath, "");
        if (!file.exists()) {
            if (!file.mkdirs())
                return null;
        }
        return imgRootPath;
    }





}
