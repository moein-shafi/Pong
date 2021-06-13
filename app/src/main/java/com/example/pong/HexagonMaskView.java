package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

public class HexagonMaskView extends View {
    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    private float width, height;
    private int maskColor;
    private Racket racket1;
    private Racket racket2;
    private Racket racket3;
    float centerX;
    float centerY;
    float triangleHeight;
    ArrayList<Pair<Float, Float>> coordinations = new ArrayList<>();
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

    public Racket getRacket1() {
        return  racket1;
    }

    public Racket getRacket2() {
        return  racket2;
    }

    public Racket getRacket3() {
        return  racket3;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    private void init() {
        hexagonPath = new Path();
        hexagonBorderPath = new Path();
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

    public void initiateCoordinations() {
        coordinations.add(new Pair(centerX, centerY + radius));
        coordinations.add(new Pair(centerX - triangleHeight, centerY + radius/2));
        coordinations.add(new Pair(centerX - triangleHeight, centerY - radius/2));
        coordinations.add(new Pair(centerX, centerY - radius));
        coordinations.add(new Pair(centerX + triangleHeight, centerY - radius/2));
        coordinations.add(new Pair(centerX + triangleHeight, centerY + radius/2));
    }

    public ArrayList<Pair<Float, Float>> getCoordinations() {
        return this.coordinations;
    }

    private void calculatePath() {
        this.triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        this.centerX = width / 2;
        this.centerY = height / 2;
        hexagonPath.moveTo(centerX, centerY + radius);

        for (Pair coordination : this.coordinations){
            hexagonPath.lineTo((float)coordination.first , (float)coordination.second);}
        hexagonPath.moveTo(centerX, centerY + radius);
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
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(10);

        this.drawRackets(c);

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


    public void racket1Left() {
        if ((this.initialYRacket1 - (radius/8) - SingleDeviceActivity.Rocket_Movement) >= (float)this.coordinations.get(2).second) {
            this.initialYRacket1 -= SingleDeviceActivity.Rocket_Movement;
            this.calculateRacket1Position();
            this.invalidate();
        }
    }

    public void racket1Right() {
        if (this.initialYRacket1 + (radius/10) + SingleDeviceActivity.Rocket_Movement <= (float)this.coordinations.get(1).second) {
            this.initialYRacket1 += SingleDeviceActivity.Rocket_Movement;
            this.calculateRacket1Position();
            this.invalidate();
        }
    }

    public void racket2Left() {

        if (this.racket2.getStartY() - SingleDeviceActivity.Rocket_Movement > (float)this.coordinations.get(3).second) {
            this.initialXRacket2 -= Math.sqrt(3) * SingleDeviceActivity.Rocket_Movement;
            this.initialYRacket2 -= SingleDeviceActivity.Rocket_Movement;
            this.calculateRacket2Position();
            this.invalidate();
        }
    }

    public void racket2Right() {

        if (this.racket2.getStopY() + SingleDeviceActivity.Rocket_Movement <= (float)this.coordinations.get(4).second) {
            this.initialXRacket2 += Math.sqrt(3) * SingleDeviceActivity.Rocket_Movement;
            this.initialYRacket2 += SingleDeviceActivity.Rocket_Movement;
            this.calculateRacket2Position();
            this.invalidate();
        }
    }

    public void racket3Left() {
        if (this.racket3.getStopY() + SingleDeviceActivity.Rocket_Movement <= (float)this.coordinations.get(0).second) {
            this.initialXRacket3 -= Math.sqrt(3) * SingleDeviceActivity.Rocket_Movement;
            this.initialYRacket3 += SingleDeviceActivity.Rocket_Movement;
            this.calculateRacket3Position();
            this.invalidate();
        }
    }

    public void racket3Right() {

        if (this.racket3.getStartY() - SingleDeviceActivity.Rocket_Movement >= (float)this.coordinations.get(5).second) {
            this.initialXRacket3 += Math.sqrt(3) * SingleDeviceActivity.Rocket_Movement;
            this.initialYRacket3 -= SingleDeviceActivity.Rocket_Movement;
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
        this.triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        this.centerX = width / 2;
        this.centerY = height / 2;
        initialXRacket1 = centerX - triangleHeight;
        initialYRacket1 = centerY;
        initialXRacket2 = centerX + triangleHeight / 2;
        initialYRacket2 = (float) (centerY - 0.75 * radius);
        initialXRacket3 = centerX + (float) (triangleHeight/2);
        initialYRacket3 = (float) (centerY + 0.75 * radius);
        this.initiateCoordinations();
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
        this.racket1.setStartY(this.initialYRacket1 - radius / SingleDeviceActivity.RACKET_SIZE);
        this.racket1.setStopY(this.initialYRacket1 + radius / SingleDeviceActivity.RACKET_SIZE);
    }

    private void calculateRacket2Position() {
        this.racket2.setX(this.initialXRacket2);
        this.racket1.setY(this.initialYRacket2);
        this.racket2.setStartX((float) (this.initialXRacket2 - Math.sqrt(3) * radius / (SingleDeviceActivity.RACKET_SIZE * 2)));
        this.racket2.setStopX((float) (this.initialXRacket2 + Math.sqrt(3) * radius / (SingleDeviceActivity.RACKET_SIZE * 2)));
        this.racket2.setStartY(this.initialYRacket2 - radius / (SingleDeviceActivity.RACKET_SIZE * 2));
        this.racket2.setStopY(this.initialYRacket2 + radius / (SingleDeviceActivity.RACKET_SIZE * 2));
    }

    private void calculateRacket3Position() {
        this.racket3.setX(this.initialXRacket3);
        this.racket1.setY(this.initialYRacket3);
        this.racket3.setStartX((float) (this.initialXRacket3 + Math.sqrt(3) * radius / (SingleDeviceActivity.RACKET_SIZE * 2)));
        this.racket3.setStopX((float) (this.initialXRacket3 - Math.sqrt(3) * radius / (SingleDeviceActivity.RACKET_SIZE * 2)));
        this.racket3.setStartY(this.initialYRacket3 - radius / (SingleDeviceActivity.RACKET_SIZE * 2));
        this.racket3.setStopY(this.initialYRacket3 + radius / (SingleDeviceActivity.RACKET_SIZE * 2));
    }

}
