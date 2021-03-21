package com.kozlovskiy.mostocks.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapUtil {

    /**
     * @param context context
     * @param resId   background res. id
     * @param symbol  first char of stock's symbol
     * @return beautiful company logo :)
     */
    public static Bitmap markSymbolOnBitmap(Context context, int resId, String symbol) {
        Resources resources = context.getResources();

        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTextSize((int) (52 * scale));

        Rect bounds = new Rect();
        paint.getTextBounds(symbol, 0, symbol.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText(symbol, x, y, paint);
        return bitmap;
    }
}
