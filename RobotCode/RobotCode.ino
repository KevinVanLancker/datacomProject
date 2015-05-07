#include <Servo.h> 
int MotorRight1=5;
int MotorRight2=6;
int MotorLeft1=10;
int MotorLeft2=11;
const int SensorLeft = 7;     
const int SensorMiddle = 4;   
const int SensorRight = 3;     
int SL;    
int SM;   
int SR;  

Servo myservo;     
int delay_time = 250;

int Fspeedd = 0;           
int directionn = 0;   

int inputPin = A1;  
int outputPin =8;

int Fgo = 8;        
int Rgo = 6;        
int Lgo = 4;         
int Bgo = 2;  
int mode = 1;
int val;
void setup()
{  
  Serial.begin(9600) ;
  pinMode(MotorRight1, OUTPUT);  
  pinMode(MotorRight2, OUTPUT);  
  pinMode(MotorLeft1,  OUTPUT);   
  pinMode(MotorLeft2,  OUTPUT);  
  pinMode(SensorLeft, INPUT); 
  pinMode(SensorMiddle, INPUT);
  pinMode(SensorRight, INPUT); 
  
  pinMode(inputPin, INPUT);   
  pinMode(outputPin, OUTPUT);  
  
  myservo.attach(9); 
  myservo.write(75);
  
}

void loop() 
 {
  
   if(Serial.available()){
     val = Serial.read()-48;
     Serial.println(val);
     delay(5);
   }
   
   SL = digitalRead(SensorLeft);
   SM = digitalRead(SensorMiddle);
   SR = digitalRead(SensorRight);
   if(val == 1){
     mode = 1;
     Serial.println("mode 1");
   }
   else if(val == 0){
     mode = 0;
     Serial.println("mode 0");
   }
   /// BESTURING //////
     if(mode == 1){
        if(val == 8){
         advance(100);
       } 
       if(val == 4){
         hardLeft(1);
       } 
       if(val == 6){
         hardRight(1);
       } 
       if(val == 2){
         back(1);
       } 
       if(val == 5){
         stopp();
       }
     }
/////// AUTONOOM //////////
       if(mode == 0){  
         Serial.println("autonoom");
        stopp();   
       if (SM == LOW)
       { 
         
          if (SL == LOW & SR == HIGH) 
          {  
            hardRight(10);
          } 
          else if (SR == LOW & SL == HIGH) 
          {  
             hardLeft(10);
          }
         else  
          { 
             collisionDetection();
         }      
       } 
       else 
      {  
         if (SL == LOW & SR == HIGH)
        {  
            softLeft(10);
        }
         else if (SR == LOW & SL == HIGH) 
        {  
           softRight(10);
        }
         else 
        {    
           advance(10);
           
            
        }
      }
      }
     }
        void advance(int d){
            digitalWrite(MotorRight1,HIGH);
            digitalWrite(MotorRight2,LOW);
            digitalWrite(MotorLeft1,HIGH);
            digitalWrite(MotorLeft2,LOW);
        }
        void stopp(){
           digitalWrite(MotorRight1,LOW);
           digitalWrite(MotorRight2,LOW);
           digitalWrite(MotorLeft1,LOW);
           digitalWrite(MotorLeft2,LOW); 
          
     
        }
        void back(int d){
           digitalWrite(MotorRight1,LOW);
           digitalWrite(MotorRight2,HIGH);
           digitalWrite(MotorLeft1,LOW);
           digitalWrite(MotorLeft2,HIGH);    
        }
        void softRight(int d){
             digitalWrite(MotorRight1,HIGH);
             digitalWrite(MotorRight2,LOW);
             analogWrite(MotorLeft1,120);
             analogWrite(MotorLeft2,0);
        }
        void hardRight(int d){
            digitalWrite(MotorRight1,LOW);
           digitalWrite(MotorRight2,LOW);
           digitalWrite(MotorLeft1,HIGH);
           digitalWrite(MotorLeft2,LOW);
        }
        void turnR(int d)        
        {
           digitalWrite(MotorRight1,LOW);
           digitalWrite(MotorRight2,HIGH);
           digitalWrite(MotorLeft1,HIGH);
           digitalWrite(MotorLeft2,LOW); 
            delay(500);
        }
        void softLeft(int d){
             analogWrite(MotorRight1,120);
             analogWrite(MotorRight2,0);
             digitalWrite(MotorLeft1,HIGH);
             digitalWrite(MotorLeft2,LOW);
        }
        void hardLeft(int d){
            digitalWrite(MotorRight1,HIGH);
            digitalWrite(MotorRight2,LOW);
            digitalWrite(MotorLeft1,LOW);
            digitalWrite(MotorLeft2,LOW);
        }
        void turnL(int d){
           digitalWrite(MotorRight1,HIGH);
           digitalWrite(MotorRight2,LOW);
           digitalWrite(MotorLeft1,LOW);
           digitalWrite(MotorLeft2,HIGH); 
           delay(d);
        }
        
        
        
            /// DETECTION   //////
void detection(){      
      
      ask_pin_F();           
     // Serial.println(Fspeedd);
     if(Fspeedd == 0){
       directionn = 8;
     }
     else if(Fspeedd < 5)       
      {
        
        directionn = 2;
      }
           
      else if(Fspeedd < 10)         
      {
          directionn = 4;
       }
       else{
         directionn = 8;
       }
     
    }    
void ask_pin_F()   
    {
      
      digitalWrite(outputPin, LOW);   
      delayMicroseconds(2);
      digitalWrite(outputPin, HIGH);  
      delayMicroseconds(10);
      digitalWrite(outputPin, LOW); 
      pinMode(inputPin, INPUT);    
      float Fdistance = pulseIn(inputPin, HIGH, 38000);  
      
      Fdistance= Fdistance/5.8/10;       
      //Serial.print("F distance:");      
      //Serial.println(Fdistance);        
      Fspeedd = Fdistance;             
    }  
void collisionDetection(){
  detection();
            if(directionn == 2)             
           {
             back(100);                    
             turnL(350);  
                           
             Serial.print(" Reverse ");  
           }
           if(directionn == 6)               
           {
             back(10); 
             turnR(250);  
                          
             Serial.print(" Right ");    
           }
           if(directionn == 4)          
           {  
                  
             turnL(250);                  
               Serial.print(" Left ");     
           }  
           if(directionn == 8)          
           { 
           advance(10);                  
          Serial.print(" Advance ");   
          Serial.print("   ");    
           }
} 

