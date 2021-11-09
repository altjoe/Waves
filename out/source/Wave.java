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

public class Wave extends PApplet {

public void setup() {
    
    background(255);
    Wave wave = new Wave();
    wave.display();
    
}

public void draw() {
    
}

class Wave {
    float gravity = -2;
    float turbulance = 2;
    // float[] wave_force;
    PVector[] wave_line;

    public Wave() {
        // start_wave();
    }

    // void start_wave() {
    //     PVector[] points = new PVector[20];
    //     for (int i = 0; i < points.length; i++){
    //         PVector point = new PVector(width*i/(points.length-1), 5);
    //     }
    //     wave_line = points;
    // }

    public void display() {
        beginShape();
        for (int i = 0; i < wave_line.length; i++){
            curveVertex(wave_line[i].x, wave_line[i].y);
        }
        endShape();
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
    String[] appletArgs = new String[] { "Wave" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
