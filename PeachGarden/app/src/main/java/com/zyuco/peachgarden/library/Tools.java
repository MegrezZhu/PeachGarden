package com.zyuco.peachgarden.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZhuLifeng on 2017/11/19.
 */

public class Tools {
    public static class LoadImagesTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        public LoadImagesTask(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            URL imageUrl = null;
            Bitmap bitmap = null;
            InputStream inputStream = null;
            try {
                imageUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
    public static int random(int lower, int upper) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }
    public static String num2String(int number) {
        if (number == 0) return "解锁";
        String[] NUMBER_TABLE = {"零", "壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String[] UNIT_TABLE = {"", "拾", "佰", "仟", "万"};
        String numberString = new StringBuffer(String.valueOf(number)).reverse().toString().trim();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberString.length(); i++) {
            builder.append(UNIT_TABLE[i]);
            builder.append(NUMBER_TABLE[numberString.charAt(i) - 48]);
        }
        builder.reverse();
        String result = builder.toString();
        result = result.replaceAll("零[拾佰仟]", "零");
        result = result.replaceAll("零万", "万");
        while (result.charAt(result.length() - 1) == '零') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
