package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class HexagonMaskView extends View {
    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    private float width, height;
    private int maskColor;
    Racket racket1;
    Racket racket2;
    Racket racket3;

    //    private float x1 = width / 2 - (float) (Math.sqrt(3) * radius / 2);
//    private float y1 = height / 2;
//    private float x2 = width / 2 + (float) (Math.sqrt(3) * radius / 4) - 5;
//    private float y2 = (float) (height/2 - 0.75*radius);
//    private float x3 = width / 2 + (float) (Math.sqrt(3) * radius / 4) - 5;
//    private float y3 = (float) (height/2 + 0.75*radius);
    private float initialXRacket1;
    private float initialXRacket2;
    private float initialXRacket3;
    private float initialYRacket1;
    private float initialYRacket2;
    private float initialYRacket3;

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
        this.racket1 = new Racket(Color.BLUE, 30);
        this.racket2 = new Racket(Color.RED, 30);
        this.racket3 = new Racket(Color.MAGENTA, 30);

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

        this.drawRackets(c);

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

    public void drawRackets(Canvas c) {
        c.drawLine(this.racket1.getStartX(),
                this.racket1.getStartY(),
                this.racket1.getStopX(),
                this.racket1.getStopY(),
                this.racket1.getPaint());

        c.drawLine(this.racket2.getStartX(),
                this.racket2.getStartY(),
                this.racket2.getStopX(),
                this.racket2.getStopY(),
                this.racket2.getPaint());

        c.drawLine(this.racket3.getStartX(),
                this.racket3.getStartY(),
                this.racket3.getStopX(),
                this.racket3.getStopY(),
                this.racket3.getPaint());
    }

    /// TODO: Check movement instead of replacement for rackets.
    public void racket1Left() {
        /// TODO: Change these magic number.
        if (this.initialYRacket1 - 40 > 365) {
            this.initialYRacket1 -= 40;
            this.racket1.setY(this.initialYRacket1);
            this.calculateRacket1Position();
            this.invalidate();
        }
    }

    public void racket1Right() {
        /// TODO: Change these magic number.
        if (this.initialYRacket1 + 40 < 750) {
            this.initialYRacket1 += 40;
            this.racket1.setY(this.initialYRacket1);
            this.calculateRacket1Position();
            this.invalidate();
        }
    }

    public void racket2Left() {
        /// TODO: Change these magic number.
        if (this.initialYRacket2 - 40 > 105) {
            this.initialXRacket2 -= Math.sqrt(3) * 40;
            this.initialYRacket2 -= 40;
            this.racket2.setY(this.initialYRacket2);
            this.calculateRacket2Position();
            this.invalidate();
            System.out.println("L Y2 >>> " + this.initialYRacket2);
        }
    }

    public void racket2Right() {
        /// TODO: Change these magic number.
        if (this.initialYRacket2 + 40 < 285) {
            this.initialXRacket2 += Math.sqrt(3) * 40;
            this.initialYRacket2 += 40;
            this.racket2.setY(this.initialYRacket2);
            this.calculateRacket2Position();
            this.invalidate();
        }
    }

    public void racket3Left() {
        /// TODO: Change these magic number.
        if (this.initialYRacket3 + 40 < 1000) {
            this.initialXRacket3 -= Math.sqrt(3) * 40;
            this.initialYRacket3 += 40;
            this.racket3.setY(this.initialYRacket3);
            this.calculateRacket3Position();
            this.invalidate();
        }
    }

    public void racket3Right() {
        /// TODO: Change these magic number.
        if (this.initialYRacket3 - 40 > 810) {
            this.initialXRacket3 += Math.sqrt(3) * 40;
            this.initialYRacket3 -= 40;
            this.racket3.setY(this.initialYRacket3);
            this.calculateRacket3Position();
            this.invalidate();
        }
    }

    // getting the view size and default radius
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        radius = height / 2 - 80;

        initialXRacket1 = width / 2 - (float) (Math.sqrt(3) * radius / 2);
        initialYRacket1 = height / 2;
        initialXRacket2 = width / 2 + (float) (Math.sqrt(3) * radius / 4);
        initialYRacket2 = (float) (height / 2 - 0.75 * radius);
        initialXRacket3 = width / 2 + (float) (Math.sqrt(3) * radius / 4);
        initialYRacket3 = (float) (height / 2 + 0.75 * radius);

        this.calculateRacketsPosition();
        calculatePath();

    }

    private void calculateRacketsPosition() {
        this.calculateRacket1Position();
        this.calculateRacket2Position();
        this.calculateRacket3Position();
    }

    private void calculateRacket1Position() {
        this.racket1.setX(this.initialXRacket1);
        this.racket1.setY(this.initialYRacket1);
        this.racket1.setStartX(this.initialXRacket1);
        this.racket1.setStopX(this.initialXRacket1);
        this.racket1.setStartY(this.initialYRacket1 - radius / 10);
        this.racket1.setStopY(this.initialYRacket1 + radius / 10);
    }

    private void calculateRacket2Position() {
        this.racket2.setX(this.initialXRacket2);
        this.racket1.setY(this.initialYRacket2);
        this.racket2.setStartX((float) (this.initialXRacket2 - Math.sqrt(3) * radius / 20));
        this.racket2.setStopX((float) (this.initialXRacket2 + Math.sqrt(3) * radius / 20));
        this.racket2.setStartY(this.initialYRacket2 - radius / 20);
        this.racket2.setStopY(this.initialYRacket2 + radius / 20);
    }

    private void calculateRacket3Position() {
        this.racket3.setX(this.initialXRacket3);
        this.racket1.setY(this.initialYRacket3);
        this.racket3.setStartX((float) (this.initialXRacket3 + Math.sqrt(3) * radius / 20));
        this.racket3.setStopX((float) (this.initialXRacket3 - Math.sqrt(3) * radius / 20));
        this.racket3.setStartY(this.initialYRacket3 - radius / 20);
        this.racket3.setStopY(this.initialYRacket3 + radius / 20);
    }

}
