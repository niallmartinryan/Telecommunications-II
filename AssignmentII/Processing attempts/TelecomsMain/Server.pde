class Server(){
  boolean active;
  
  color activeColor;
  color deactiveColor;
  
  int xpos;
  int ypos;

  Server(int xpos, int ypos){
  
  
  }

  void drawServers(){
    if(active){
      fill(activeColor);
    }
    else{
      fill(deactiveColor)
    }
  }




}
