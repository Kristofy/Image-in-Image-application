
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

void setup(){
  img = loadImage("./node/data/bigger.jpg");
  screen = createGraphics(img.width, img.height);
  screen.beginDraw();
  images = new ArrayList<Img>();
  size(400, 400);
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
  scl = float(min(width, height))/float(max(screen.height, screen.width));
  img.loadPixels();
  scale(scl);
  image(img, screen.width/2, screen.height/2);

  tint(255,255,255,255);
}

int g_x = 0, g_y = 0;
void draw(){
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
   println(str(float(g_y)/float(rows)*100) + "% done");
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

void printImg(Img curr, int x, int y){
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

void loadData(){
 String[] lines = loadStrings("populated_values.txt");
 int index = 0;
 for(String line : lines){
   String[] p = line.split(" ");
   images.add(
     new Img(
       int(p[0]),
       int(p[1]),
       new int[]{
         int(p[2]),
         int(p[3]),
         int(p[4]),
         int(p[5]),
         int(p[6]),
         int(p[7]),
         int(p[8]),
         int(p[9]),
         int(p[10])
       }
     ));
   index++;
 }
  
}
