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

public class ImageInImage extends PApplet {


PGraphics screen;
PImage img;
int img_size = 50;
int sample_size = 11;
int spacing = (img_size - 3*sample_size)/4;
int cols, rows;
int N;
boolean flip = true;
float scl;
ArrayList<Img> images;
float[] diffs;

public void setup(){
  img = loadImage("./node/data/bigger.jpg");
  screen = createGraphics(img.width, img.height);
  screen.beginDraw();
  images = new ArrayList<Img>();
  
  screen.imageMode(CENTER);
  screen.colorMode(RGB);
  imageMode(CENTER);
  colorMode(RGB);
  loadData();
  N = images.size();
  diffs = new float[N];
  cols = screen.width/img_size;
  rows = screen.height/img_size;
  tint(255,255,255,100);
  scl = PApplet.parseFloat(min(width, height))/PApplet.parseFloat(max(screen.height, screen.width));
  img.loadPixels();
  scale(scl);
  image(img, screen.width/2, screen.height/2);

  tint(255,255,255,255);
}

int g_x = 0, g_y = 0;
public void draw(){
   scale(scl);
   int[] data = new int[sample_size*sample_size];
   for (int i = 0; i < N; i++){ diffs[i] = 0;}
   for(int i = 1; i <= 3; i++){
     for(int j = 1; j <= 3; j++){
      int x = j*spacing + (j-1)*sample_size; 
      int y = i*spacing + (i-1)*sample_size; 

      for(int a = 0; a < sample_size; a++){
       for(int b = 0; b < sample_size; b++){
         int index = (y+a+g_y*img_size)*screen.width + (x+b+g_x*img_size);
         data[a*sample_size+b] = img.pixels[index];
       }
      }
      
      data = sort(data, sample_size*sample_size);
      int middle = data[sample_size*sample_size/2];
      
      for(int k = 0; k < N; k++){
        float r = red(middle) - red(images.get(k).colors[(i-1)*3+j-1]);
        float g = green(middle) - green(images.get(k).colors[(i-1)*3+j-1]);
        float b = blue(middle) - blue(images.get(k).colors[(i-1)*3+j-1]);
        float d = r*r+g*g+b*b; 
        diffs[k] += d;
      }

    }
  }  
  
  
  float min = diffs[0];
  int min_index = 0;
  for(int i = 0; i < N; i++){
    if(min > diffs[i]){
     min = diffs[i];
     min_index = i;
    }
  }
    
  printImg(images.get(min_index), g_x*img_size, g_y*img_size);
  g_x++;
  if(g_x == cols){
   g_y++;
   println(str(PApplet.parseFloat(g_y)/PApplet.parseFloat(rows)*100) + "% done");
   if(g_y == rows){
     screen.endDraw();
     println("Image rendered!");
     //save("result.png");
     screen.save("result.png");
     noLoop();  
     exit();
    }
     g_x = 0;
  }
 
}

public void printImg(Img curr, int x, int y){
  screen.pushMatrix();
  screen.translate(x+img_size/2, y + img_size/2);
  switch(curr.config){
   case 0:   break;
   case 1:
     screen.rotate(PI/2);
   break;
   case 2:
     screen.rotate(PI);
   break;
   case 3:
     screen.rotate(PI*3/2);
   break;
   case 4:
     screen.scale(-1, 1);
   break;
   case 5:
     screen.scale(-1, 1);
     screen.rotate(PI/2);
   break;
   case 6:
     screen.scale(-1, 1);
     screen.rotate(PI);   
   break;
   case 7:
     screen.scale(-1, 1);
     screen.rotate(PI*3/2);
   break;
  }
  PImage tmp_img = loadImage("./node/data/cat"+curr.index+".jpg");
  screen.image(tmp_img, 0, 0);
  
  screen.popMatrix();
  
  pushMatrix();
  translate(x+img_size/2, y + img_size/2);
  switch(curr.config){
   case 0:   break;
   case 1:
     rotate(PI/2);
   break;
   case 2:
     rotate(PI);
   break;
   case 3:
     rotate(PI*3/2);
   break;
   case 4:
     scale(-1, 1);
   break;
   case 5:
     scale(-1, 1);
     rotate(PI/2);
   break;
   case 6:
     scale(-1, 1);
     rotate(PI);   
   break;
   case 7:
     scale(-1, 1);
     rotate(PI*3/2);
   break;
  }
  image(tmp_img, 0, 0);
  
  popMatrix();
}

public void loadData(){
 String[] lines = loadStrings("populated_values.txt");
 int index = 0;
 for(String line : lines){
   String[] p = line.split(" ");
   images.add(
     new Img(
       PApplet.parseInt(p[0]),
       PApplet.parseInt(p[1]),
       new int[]{
         PApplet.parseInt(p[2]),
         PApplet.parseInt(p[3]),
         PApplet.parseInt(p[4]),
         PApplet.parseInt(p[5]),
         PApplet.parseInt(p[6]),
         PApplet.parseInt(p[7]),
         PApplet.parseInt(p[8]),
         PApplet.parseInt(p[9]),
         PApplet.parseInt(p[10])
       }
     ));
   index++;
 }
  
}

class Img{
 int index;
 int config;
 int[] colors;
  
 public Img(int i, int c, int[] cols){
   index = i;
   config = c;
   colors = cols;
 }
  
}
  public void settings() {  size(400, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ImageInImage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
