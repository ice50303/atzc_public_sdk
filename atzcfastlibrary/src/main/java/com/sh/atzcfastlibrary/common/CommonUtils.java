package com.sh.atzcfastlibrary.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CommonUtils {

    private static final String TAG = "CommonUtils";

    public static boolean isHighDensity(float density) {
        return density > 2.0;
    }

    public static boolean isLowDensity(float density) {
        return density <= 1.5;
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v, boolean focused) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focused) {
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        } else {
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    public static int getStatusBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 计算出该TextView中文字的长度(像素)
     *
     * @param c
     * @param text
     * @param textSize_dp
     * @return
     */
    public static float getTextViewWidth(Context c, String text, float textSize_dp) {
        if (text == null || text.equals("")) {
            return 0;
        }

        TextView textView = new TextView(c);
        TextPaint paint = textView.getPaint();
        paint.setTextSize(Dp2Px(c, textSize_dp));

        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        //return Px2Dp(c, textLength);
        return textLength;
    }

    public static float getTextViewWidthAsPx(Context c, String text, float textSize_px) {
        if (text == null || text.equals("")) {
            return 0;
        }

        TextView textView = new TextView(c);
        TextPaint paint = textView.getPaint();
        paint.setTextSize(textSize_px);

        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        //return Px2Dp(c, textLength);
        return textLength;
    }


    public static float getBoldTextViewWidth(Context c, String text, float textSize_dp) {
        if (text == null || text.equals("")) {
            return 0;
        }

        TextView textView = new TextView(c);
        TextPaint paint = textView.getPaint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(Dp2Px(c, textSize_dp));

        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        //return Px2Dp(c, textLength);
        return textLength;
    }


    /**
     * 将dp转换为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float Dp2Px(Context context, float dp) {
        // px = dp * (dpi / 160)
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
    }

    /**
     * 将px转换为dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static String exce(String cmd, String workdirectory) {
        StringBuffer result = new StringBuffer();
        try {
            // 创建操作系统进程（也可以由Runtime.exec()启动）
            // Runtime runtime = Runtime.getRuntime();
            // Process proc = runtime.exec(cmd);
            // InputStream inputstream = proc.getInputStream();
            ProcessBuilder builder = new ProcessBuilder(cmd);
            if (workdirectory == null) {
                workdirectory = "/sdcard";
            }

            InputStream in = null;
            // 设置一个路径（绝对路径了就不一定需要）
            if (workdirectory != null) {
                // 设置工作目录（同上）
                builder.directory(new File(workdirectory));
                // 合并标准错误和标准输出
                builder.redirectErrorStream(true);
                // 启动一个新进程
                Process process = builder.start();

                // 读取进程标准输出流
                in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
                    result = result.append(new String(re));
                }
            }
            // 关闭输入流
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static void execCommand(String command) throws IOException {

        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void copyAssestFileToSdcard(Context myContext,
                                              String ASSETS_NAME, String filename) {
        // 如果目录不中存在，创建这个目录
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets()
                        .open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取屏幕宽和高
     *
     * @param context
     * @return
     */
    public static int[] getScreenHW(Context context) {
        int[] hw = new int[3];
        try {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            hw[0] = dm.widthPixels;//屏幕宽带(像素)
            hw[1] = dm.heightPixels;//屏幕高度(像素)
            hw[2] = dm.densityDpi;//屏幕密度(120/160/240)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hw;
    }

    /**
     * 手机屏幕宽度
     *
     * @param ctx
     * @return
     */
    public static int getWindowWidth(Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.widthPixels;
    }

    /**
     * 手机屏幕高度
     *
     * @param ctx
     * @return
     */
    public static int getWindowHeight(Context ctx) {

        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.heightPixels;
    }

    /**
     * 指定图片的缩放比例
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // 给定的BitmapFactory设置解码的参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 压缩完后便可以将inJustDecodeBounds设置为false了。
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // 给定的BitmapFactory设置解码的参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 压缩完后便可以将inJustDecodeBounds设置为false了。
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽、高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

//      if (height > reqHeight || width > reqWidth) {
//          //这里有两种压缩方式，可供选择。
//          /**
//           * 压缩方式二
//           */
//          // final int halfHeight = height / 2;
//          // final int halfWidth = width / 2;
//          // while ((halfHeight / inSampleSize) > reqHeight
//          // && (halfWidth / inSampleSize) > reqWidth) {
//          // inSampleSize *= 2;
//          // }
//
        /**
         * 压缩方式一
         */
        // 计算压缩的比例：分为宽高比例
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//      }

        return inSampleSize;
    }

    /**
     * 适配7.0 popWindow showAsDropDown 显示异常的问题
     *
     * @param pw
     * @param dropDownView
     */
    public static void showPopupWindow(PopupWindow pw, View dropDownView) {
        if (Build.VERSION.SDK_INT < 24) {
            pw.showAsDropDown(dropDownView);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            dropDownView.getLocationOnScreen(location);
            // 7.1 版本处理
            if (Build.VERSION.SDK_INT >= 25) {
                WindowManager wm = (WindowManager) pw.getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
                int mscreenHeight = wm.getDefaultDisplay().getHeight();
                pw.setHeight(mscreenHeight - location[1] - dropDownView.getHeight());
            }
            pw.showAtLocation(dropDownView, Gravity.NO_GRAVITY, 0, location[1] + dropDownView.getHeight());
        }
    }

    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;

    private volatile static Point[] mRealSizes = new Point[2];


    public static int getScreenRealHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getScreenHeight(context);
        }

        int orientation = context != null
                ? context.getResources().getConfiguration().orientation
                : context.getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (mRealSizes[orientation] == null) {
            WindowManager windowManager = context != null
                    ? (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
                    : (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            mRealSizes[orientation] = point;
        }
        return mRealSizes[orientation].y;
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

}
