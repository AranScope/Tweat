package com.company;

/**
 * Created by aranscope on 07/11/15.
 */
public class Vector2 {
    private float x;
    private float y;

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public Vector2 normalise(){
        float mag = abs();
        this.x /= mag;
        this.y /= mag;
        return this;
    }

    public Vector2 vectorTo(Vector2 target){
        return new Vector2(target.getX() - this.x, target.getY() - this.y);
    }

    public float abs(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public Vector2 mult(Vector2 multVector){
        return new Vector2(this.x * multVector.getX(), this.y * multVector.getY());
    }

    public Vector2 mult(float val){
            return new Vector2(this.x * val, this.y * val);
    }

    public Vector2 div(Vector2 divVector){
        return new Vector2(this.x/divVector.getX(), this.y/divVector.getY());
    }

    public Vector2 add(Vector2 addVector){
        return new Vector2(this.x + addVector.getX(), this.y + addVector.getY());
    }

    public Vector2 sub(Vector2 subVector){
        return new Vector2(this.x - subVector.getX(), this.y - subVector.getY());
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(){

    }
}
