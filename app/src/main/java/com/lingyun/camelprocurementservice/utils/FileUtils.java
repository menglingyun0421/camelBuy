package com.lingyun.camelprocurementservice.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 凌云 on 2018/8/14.
 */

public class FileUtils {

    Uri contentUri;

    static boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


    // 生成文件夹
    public static boolean makeRootDirectory(String filePath) {
            File file = null;
            try {
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdir();
                    return false;
                }
                return true;
            } catch (Exception e) {
                Log.e("error_mly:", e+"");
                return false;
            }

    }

    //存数据
    public static void saveData(String data,String path, String pathName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
            File saveFile = new File(sdCardDir+path, pathName);

            //写数据
            try {
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(data.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static  String  getData(String path,String pathName) {
        //取数据
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
            File saveFile = new File(sdCardDir+path, pathName);

            //读数据
            try {
                FileInputStream fis = new FileInputStream(saveFile);
                int len = 0;
                byte[] buf = new byte[1024];
                StringBuffer sb = new StringBuffer();
                while ((len = fis.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                Log.e("mly_savePath", "------------" + sb.toString());
//                String []a = sb.toString().split("##");
//                for (int i = 0; i < a.length; i++) {
//                    Log.e("mly_savePath", "------------" + a[i]);
//                }

                fis.close();
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "200";
            }
        }else {
            return "200";
        }
    }

    public void setContentUri(Uri uri) {
        this.contentUri = uri;
    }

    public Uri getContentUri() {
        return contentUri;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, Uri uri) {
        Log.v(" File -",
                "Authority: " + uri.getAuthority() +
                        ", Fragment: " + uri.getFragment() +
                        ", Port: " + uri.getPort() +
                        ", Query: " + uri.getQuery() +
                        ", Scheme: " + uri.getScheme() +
                        ", Host: " + uri.getHost() +
                        ", Segments: " + uri.getPathSegments().toString()
        );

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            return getRealPathFromUriKitKatPlus(context, uri);
        } else {
            return getRealPathFromUriMinusKitKat(context, uri);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromUriKitKatPlus(Context context, Uri uri) {
        Cursor cursor = null;
        String wholeId = DocumentsContract.getDocumentId(uri);
        String id = wholeId.split(":")[1];

        try {
            String proj[] = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj, sel, new String[]{id}, null);
            int column_index = cursor.getColumnIndexOrThrow(proj[0]);
            String filePath = "";
            if (cursor.moveToFirst()){
                filePath = cursor.getString(column_index);
            }
            cursor.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getRealPathFromUriMinusKitKat(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        System.out.println(picturePath);
        return picturePath;
    }

    //进行复制的函数
    public static String copyFile(Context context,String oldPath, String newPath) {
        String faildPath="faild";
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);

            String imagePath=newPath+File.separator+oldfile.getName();


                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs2 = new FileOutputStream(newPath+File.separator+oldfile.getName());

                byte[] buffer = new byte[1024];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs2.write(buffer, 0, byteread);
                }
                inStream.close();
//                Toast.makeText(context,"success",Toast.LENGTH_LONG).show();
                return imagePath;
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return  faildPath;
        }

    }
}
