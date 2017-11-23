class Client{
  boolean active;
  color activeColor;
  color deactiveColor;
  
  int radius;
  int xpos;
  int ypos;

  Client(int xpos, int ypos, int radius){
    
  }
  
  void drawClients(){
    if(active){
      fill(activeColor);
    }
    else{
      fill(deactiveColor);
    }
    ellipse(xpos,ypos, radius, radius);
    
  }











}
