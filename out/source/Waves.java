import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Waves extends PApplet {

ArrayList<Wave> waves;
float speeddiv = 10;
public void setup() {
    
    background(255);
    speeddiv = 1.0f / speeddiv;
    waves = new ArrayList<Wave>();
    Wave wave = new Wave();
    waves.add(wave);
}
int newwave = 100;
int newwavecount = 0;
public void draw() {
    background(255);
    for (int i = waves.size() - 1; i >= 0; i--){
        if (!waves.get(i).finished){
            // println("Ran");
            waves.get(i).move();
            waves.get(i).display();
        } else {
            waves.remove(i);
        }
    }
    if (newwavecount > newwave){
        Wave wave = new Wave();
        waves.add(wave);
        newwavecount = 0; 
        newwave = PApplet.parseInt(random(100, 200));
        println(newwave);
    }
    newwavecount += 1;
}

class Wave {
    float gravity = -0.5f;
    float turbulance = 2;
    ArrayList<Verlet> wave_line = new ArrayList<Verlet>();
    ArrayList<PVector[]> wave_lines = new ArrayList<PVector[]>();
    int wave_length = 30;
    boolean left = true;
    boolean finished = false;
    boolean juststarted = true;

    public Wave() {
        if (PApplet.parseInt(random(0,2)) == 1){
            left = false;
        }
        start_wave();
    }

    public void start_wave() {

        for (int i = 0; i < wave_length; i++){
            float x = i*width/(wave_length - 3) - width/(wave_length - 3);
            if (left){
                PVector point = new PVector(x, random(-45-i*2, -5-i*2));
                PVector force = new PVector(0, random(7*speeddiv, 15*speeddiv));
                Verlet ver = new Verlet(point, force);
                wave_line.add(ver);
            } else {
                PVector point = new PVector(x, random(-85+i*2, -45+i*2));
                PVector force = new PVector(0, random(12*speeddiv, 20*speeddiv));
                Verlet ver = new Verlet(point, force);
                wave_line.add(ver);
            }
            
        }

        for (int j = 0; j <= 1; j++){
            for (int i = 1; i < wave_line.size(); i++){
                Verlet prev = wave_line.get(i-1);
                wave_line.get(i).convolve(prev);
            }
        }
        if (visible()){
            PVector[] arr = snapshot();
            wave_lines.add(arr);
        }
       
    }



    public void display() {
        for (int i = 0; i < wave_lines.size(); i++){
            PVector[] curve = wave_lines.get(i);
            noFill();
            stroke(0);
            beginShape();
            for (int j = 0; j < curve.length; j++){
                curveVertex(curve[j].x, curve[j].y);
            }
            endShape();
        }
        noFill();
        stroke(0);
        beginShape();
        for (int j = 0; j < wave_line.size(); j++){
            curveVertex(wave_line.get(j).current.x, wave_line.get(j).current.y);
        }
        endShape();

        if (!visible() && !juststarted){
            finished = true;
        } else if (visible() && juststarted){
            juststarted = false;
        }
    }
    int count = 0;
    int drawn_line_freq = 8;

    public void move() {
        for (int i = 0; i < wave_line.size(); i++){
            wave_line.get(i).next();
        }

        count += 1;
        if (count >= drawn_line_freq && visible()){
            PVector[] arr = snapshot();
            wave_lines.add(arr);
            count = 0;
        }
    }

    public PVector[] snapshot(){
        PVector[] arr = new PVector[wave_line.size()];
        for (int i = 0; i < wave_line.size(); i++){
            arr[i] = wave_line.get(i).current;
        }
        return arr;
    }

    public boolean visible(){
        for (int i = 0; i < wave_line.size(); i++){
            if (wave_line.get(i).current.y > 0){
                return true;
            }
        }
        return false;
    }
}

class Verlet {
    PVector prev;
    PVector current;
    PVector gravity = new PVector(0, -0.05f * speeddiv); 

    public Verlet(PVector pos, PVector force){
        current = pos;
        prev = PVector.sub(current, force);
    }

    public void next(){
        PVector diff = PVector.sub(current, prev);
        diff = PVector.add(diff, gravity);
        prev = current;
        current = PVector.add(current, diff);
    }

    public void convolve(Verlet v) {
        float prevx = prev.x;
        prev = PVector.div(PVector.add(v.prev, prev), 2.0f);
        current = PVector.div(PVector.add(v.current, current), 2.0f);
        prev.x = prevx;
        current.x = prevx;
    }
}

/*
The farther away from the start the weaker the force acting upon it is 
    - can be gravity 
    - might look into distance related force
Would like turbulance of previous wave and new one 
    - can be force used similar to gravity at certain point
        - point can be when it has pass previous wave?
a

*/
  public void settings() {  size(512, 512); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Waves" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}