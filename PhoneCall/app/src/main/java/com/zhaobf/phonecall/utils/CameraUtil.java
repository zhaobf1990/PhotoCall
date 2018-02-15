package com.zhaobf.phonecall.utils;

/**
 * 作者：YINGHK on 2015-04-22 15:14
 * 邮箱：yhk@timevale.com
 */

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * 在onActivityResult方法中根据requestCode和resultCode来获取当前拍照的图片地址。
 * 注意：这里有个问题，在有些机型当中（如SamsungI939、note2等）遇见了当拍照并存储之后，intent当中得到的data为空：
 * data = null 的情况主要是由于拍照的时候横屏了,导致重新create, 普通的解决方法可以在sharedpreference里面保存拍照文件的路径(onSaveInstance保存),
 * 在onRestoreSaveInstance里面在获取出来.
 * 最简单的可以用fileUtil 里面的一个静态变量保存起来..
 */


public class CameraUtil {

    private static final String IMAGE_TYPE = "image/*";

    private Context mContext;

    public CameraUtil(Context context) {
        mContext = context;
    }

    /**
     * 打开照相机
     *
     * @param activity    当前的activity
     * @param requestCode 拍照成功时activity forResult 的时候的requestCode
     * @param photoFile   拍照完毕时,图片保存的位置
     */
    public void openCamera(Activity activity, int requestCode, File photoFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 本地照片调用  只能选一张照片
     *
     * @param activity
     * @param requestCode
     */
    public void openPhotosSingle(Activity activity, int requestCode) {
        if (openPhotosNormal(activity, requestCode) && openPhotosBrowser(activity, requestCode) && openPhotosFinally())
            ;
    }




    /**
     * PopupMenu打开本地相册.
     */
    private boolean openPhotosNormal(Activity activity, int actResultCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        try {
            activity.startActivityForResult(intent, actResultCode);

        } catch (android.content.ActivityNotFoundException e) {
            return true;
        }

        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private boolean openPhotosBrowser(Activity activity, int requestCode) {
        Toast.makeText(mContext, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private boolean openPhotosFinally() {
        Toast.makeText(mContext, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG).show();
        return false;
    }


//    /** 这个方法在4.4及以上版本时,获取文件有问题     赵佰枫   时间:2015-07-22     具体BUG参考http://www.2cto.com/kf/201502/376975.html
//     * 获取从本地图库返回来的时候的URI解析出来的文件路径
//     *
//     * @return
//     */
//    public static String getPhotoPathByLocalUri(Context context, Intent data) {
//        String picturePath = null;
//        try {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePath = cursor.getString(columnIndex);
//                cursor.close();
//            } else {
//
//                selectedImage = data.getData();
//                picturePath = selectedImage.getPath();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            MobclickAgent.reportError(context, "从相机或相册获取文件路径失败" + e.getMessage());
//        }
//        return picturePath;
//    }


    /**
     * 获取从"文件","拍照","相册"返回来的时候的URI解析出来的文件路径
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  //android4.4, Google公司代号为KitKat

            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                return getPathGreaterThanOrEqualKitKat(context, uri); //如果android系统版本大于等于4.4版本,则调用这个方法获取Path

            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 如果android系统版本大于等于4.4版本,则调用这个方法获取Path
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPathGreaterThanOrEqualKitKat(Context context, Uri uri) {
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }

        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    split[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        return null;
    }


    /**
     * 4.4版本以下(不包括4.4)获取从MediaStore的URI中解析出来的路径
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * 4.4版本以下(不包括4.4)  获取从外部存储器的URI中解析出来的路径
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
