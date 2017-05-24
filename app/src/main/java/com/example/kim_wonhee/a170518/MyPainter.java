package com.example.kim_wonhee.a170518;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kim_wonhee on 2017. 5. 18..
 */

public class MyPainter extends View {

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    String operationType = "";

    String file = "";

    boolean stamp = false;

    int oldX = -1;
    int oldY = -1;

    int width = 0;
    int height = 0;

    public MyPainter(Context context) {
        super(context);

        this.mPaint = new Paint();
    }

    public MyPainter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (stamp == true) {
            drawStamp();
            invalidate();
        }

        //--- eraser
        if (operationType.equals("eraser")) {
            mBitmap.eraseColor(Color.DKGRAY);
            invalidate();
            operationType = "";
        }

        //--- open
        if (operationType.equals("open")) {
            Open(file);
            invalidate();
            operationType = "";
        }

        //--- save
        if (operationType.equals("save")) {
            Save(file);
            invalidate();
            operationType = "";
            //Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();
        }

        //--- bluring
        if (operationType.equals("bluring")) {
            if (stamp == true) {
                BlurMaskFilter blur = new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL);
                mPaint.setMaskFilter(blur);
                //invalidate();
            }
        } else if (operationType.equals("notbluring")) {
            mPaint.reset();
        }

        //--- coloring
        if (operationType.equals("coloring")) {
            if (stamp == true) {
                float[] matrixarray = { 2f, 0f, 0f, 0f, -25f,
                                        0f, 2f, 0f, 0f, -25f,
                                        0f, 0f, 2f, 0f, -25f,
                                        0f, 0f, 0f, 1f, -25f};
                ColorMatrix matrix = new ColorMatrix(matrixarray);
                mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));
            }
        } else if (operationType.equals("notcoloring")) {
            mPaint.reset();
        }

        //--- Pen Width Big
        if (operationType.equals("penwidthbig")) {
            mPaint.setStrokeWidth(5);
        } else if (operationType.equals("notpenwidthbig")) {
            mPaint.setStrokeWidth(3);
        }

        //--- Pen Color Red
        if (operationType.equals("pencolorred")) {
            mPaint.setColor(Color.RED);
        }

        //--- Pen Color Red
        if (operationType.equals("pencolorblue")) {
            mPaint.setColor(Color.BLUE);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int X = (int) event.getX();
        int Y = (int) event.getY();

        //--- stamp 찍기
        if (stamp == true) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldX = X - (width / 2);
                oldY = Y - (height / 2);
                invalidate();
            }
        }
        //--- 펜으로 낙서하기기
        else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldX = X;
                oldY = Y;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (oldX != -1) {
                    mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                    invalidate();
                }
                oldX = X;
                oldY = Y;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (oldX != -1) {
                    mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                    invalidate();
                }
                oldX = -1;
                oldY = -1;
            }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.WHITE);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.DKGRAY);
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
        invalidate();
    }

    //--- stamp 효과
    private void drawStamp() {
        Bitmap stamp = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        width = stamp.getWidth();
        height = stamp.getHeight();

        stamp = Bitmap.createScaledBitmap(stamp, width/3, height/3, true);

        if (operationType.equals("rotate")) {
            mCanvas.rotate(30, this.getWidth() / 2, this.getHeight() / 2);
            mCanvas.drawBitmap(stamp, oldX, oldY, mPaint);
            mCanvas.rotate(-30, this.getWidth() / 2, this.getHeight() / 2);
            Toast.makeText(getContext(), "ROTATE", Toast.LENGTH_SHORT).show();
            operationType = "";
        } else if (operationType.equals("move")) {
            mCanvas.translate(10, 10);
            mCanvas.drawBitmap(stamp, oldX, oldY, mPaint);
            mCanvas.translate(-10, -10);
            Toast.makeText(getContext(), "MOVE", Toast.LENGTH_SHORT).show();
            operationType = "";
        } else if (operationType.equals("scale")) {
            mCanvas.scale(1.5f, 1.5f);
            mCanvas.drawBitmap(stamp, oldX, oldY, mPaint);
            mCanvas.scale(0.67f, 0.67f);
            Toast.makeText(getContext(), "SCALE", Toast.LENGTH_SHORT).show();
            operationType = "";
        } else if (operationType.equals("skew")) {
            mCanvas.skew(0.2f, 0.0f);
            mCanvas.drawBitmap(stamp, oldX, oldY, mPaint);
            mCanvas.skew(-0.2f, 0.0f);
            Toast.makeText(getContext(), "SKEW", Toast.LENGTH_SHORT).show();
            operationType = "";
        } else {
            mCanvas.drawBitmap(stamp, oldX, oldY, mPaint);
        }
    }

    //--- 파일 저장하기
    public boolean Save(String file_name) {
        try {
            FileOutputStream out = new FileOutputStream(file_name);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
        return false;
    }

    //--- 파일 읽어오기
    public void Open(String file_name) {
        Bitmap prev = BitmapFactory.decodeFile(file_name);
        if (prev != null) {
            mBitmap.eraseColor(Color.DKGRAY);

            int prev_width = prev.getWidth();
            int prev_height = prev.getHeight();

            prev = Bitmap.createScaledBitmap(prev, prev_width / 2, prev_height / 2, false);

            int prev_X = mCanvas.getWidth() / 2 - prev_width / 4;
            int prev_Y = mCanvas.getHeight() / 2 - prev_height / 4;

            mCanvas.drawBitmap(prev, prev_X, prev_Y, mPaint);

            Toast.makeText(getContext(), "저장된 파일을 열었습니다. ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "저장된 파일이 없습니다. ", Toast.LENGTH_SHORT).show();
        }
    }

}
