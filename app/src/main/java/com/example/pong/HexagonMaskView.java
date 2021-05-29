package com.example.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Printer;
import android.view.View;

public class HexagonMaskView extends View {
    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    private float width, height;
    private int maskColor;
//    private float x1 = width / 2 - (float) (Math.sqrt(3) * radius / 2);
//    private float y1 = height / 2;
//    private float x2 = width / 2 + (float) (Math.sqrt(3) * radius / 4) - 5;
//    private float y2 = (float) (height/2 - 0.75*radius);
//    private float x3 = width / 2 + (float) (Math.sqrt(3) * radius / 4) - 5;
//    private float y3 = (float) (height/2 + 0.75*radius);
    private float x1,x2,x3,y1,y2,y3;


    public HexagonMaskView(Context context) {
        super(context);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        hexagonPath = new Path();
        hexagonBorderPath = new Path();
//        maskColor = 0x8BBDD9;

        maskColor = Color.rgb(119,189,191);
    }

    public void setRadius(float r) {
        this.radius = r;
        calculatePath();
    }

    public void setMaskColor(int color) {
        this.maskColor = color;
        invalidate();
    }

    private void calculatePath() {
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = width/2;
        float centerY = height/2;
        hexagonPath.moveTo(centerX, centerY + radius);

        hexagonPath.lineTo(centerX - triangleHeight, centerY + radius/2);
        hexagonPath.lineTo(centerX - triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX, centerY - radius);
        hexagonPath.lineTo(centerX + triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX + triangleHeight, centerY + radius/2);
        hexagonPath.moveTo(centerX, centerY + radius);
//        hexagonPath.lineTo((float) (centerX - 0.95 *width/2), centerY + radius);
//        hexagonPath.moveTo((float) (centerX - 0.95 *width/2), centerY + radius);
//        hexagonPath.lineTo((float) (centerX - 0.95 *width/2), centerY - radius);
//        hexagonPath.lineTo(centerX -radius/2, centerY - radius/2);
//        hexagonPath.lineTo(centerX + radius/2, centerY - radius/2);
//        hexagonPath.lineTo(centerX +radius/2, centerY + radius/2);
//        hexagonPath.moveTo(centerX+radius/2, centerY + radius/2);
//

//        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
//        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + radiusBorder/2);
//        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
//        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - radiusBorder/2);
//        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
//        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - radiusBorder/2);
//        hexagonBorderPath.moveTo(centerX-triangleBorderHeight, centerY + radiusBorder/2);
//        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + radiusBorder/2);
//        hexagonBorderPath.moveTo(centerX-triangleBorderHeight, centerY + radiusBorder/2);
//        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        invalidate();
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        c.clipPath(hexagonBorderPath, Region.Op.DIFFERENCE);
        c.drawColor(Color.rgb(255,255,255));
        c.save();
        c.clipPath(hexagonPath, Region.Op.DIFFERENCE);
        c.drawColor(maskColor);
        c.save();
        float centerX = width/2;
        float centerY = height/2;
        float radiusBorder = radius + 30;
        float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
//        Canvas c = new Canvas();
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(10);
        Paint p_racket = new Paint();
        p_racket.setColor(Color.BLUE);
        p_racket.setStrokeWidth(30);
        c.drawLine(x1,y1-radius/10,x1,y1+radius/10,p_racket);
        c.drawLine((float) (x2 - Math.sqrt(3) * radius /20),y2 - radius/20
                , (float) (x2 + Math.sqrt(3) * radius /20), y2 + radius/20,p_racket);
        c.drawLine((float) (x3 + Math.sqrt(3) * radius /20),y3 - radius/20
                , (float) (x3 - Math.sqrt(3) * radius /20), y3 + radius/20,p_racket);
        c.drawLine(centerX-triangleBorderHeight,
                centerY + radiusBorder/2,
                centerX - triangleBorderHeight,
                centerY - radiusBorder/2,p);
        c.drawLine(centerX-triangleBorderHeight,
                centerY + radiusBorder/2,
                centerX - triangleBorderHeight,
                centerY - radiusBorder/2,p);
        c.drawLine(centerX,
                centerY - radiusBorder,
                centerX + triangleBorderHeight,
                centerY - radiusBorder/2,p);
        c.drawLine(centerX+triangleBorderHeight,
                centerY + radiusBorder/2,
                centerX ,
                centerY + radiusBorder,p);
//        c.drawCircle(centerX,centerY,radius/5,p_racket);
//        Bitmap bMap = BitmapFactory.decodeFile("/draw");
//        c.drawBitmap();




    }

    // getting the view size and default radius
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height =  MeasureSpec.getSize(heightMeasureSpec);
        radius = height / 2 -80;
        x1 = width / 2 - (float) (Math.sqrt(3) * radius / 2) ;
        y1 = height / 2;
        x2 = width / 2 + (float) (Math.sqrt(3) * radius / 4);
        y2 = (float) (height/2 - 0.75*radius);
        x3 = width / 2 + (float) (Math.sqrt(3) * radius / 4);
        y3 = (float) (height/2 + 0.75*radius);
        calculatePath();
    }
}
