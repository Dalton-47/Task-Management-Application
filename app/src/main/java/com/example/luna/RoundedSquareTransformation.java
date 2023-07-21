package com.example.luna;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;


    //
    public class RoundedSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float cornerRadius = size / 10f; // adjust the corner radius to your liking
            float edgeRadius = size / 60f; // adjust the edge radius to your liking

            Path path = new Path();
            path.moveTo(cornerRadius, 0);
            path.lineTo(size - cornerRadius, 0);
            path.quadTo(size, 0, size, cornerRadius);
            path.lineTo(size, size - cornerRadius);
            path.quadTo(size, size, size - cornerRadius, size);
            path.lineTo(cornerRadius, size);
            path.quadTo(0, size, 0, size - cornerRadius);
            path.lineTo(0, cornerRadius);
            path.quadTo(0, 0, cornerRadius, 0);
            path.close();

            canvas.drawPath(path, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "rounded-square";
        }
    }




    //

