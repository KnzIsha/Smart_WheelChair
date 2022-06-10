
#include<Servo.h>
/*
 1b == A0
 1f == A1
int motor1b = 2;//motor 1 : input 1 high backward
int motor1f = 3;//motor1 :  input 2 high forward
*/


/*
 2f=A2
 2b=A3
int motor2f = 4;//motor 2 : input 3 high forward
int motor2b = 5;//mototr 2 : input 4 high backward
*/

Servo myservo1, myservo2;

//for head position 
const int trigPin = 2;
const int echoPin = 3;

long duration;
int distanceCm;

// for left
int echo1 = 4;
int trig1 = 5;


//for right
int echo2 = 6;
int trig2 = 7;

//for backwards
int echo3 = 8;
int trig3 = 9;


//-------*******------SETUP FUNCTION --------********---------

void setup() {

  myservo1.attach(12);
  myservo2.attach(13);
  
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);  
  pinMode(trig1, OUTPUT); // Sets the trigPin as an Output
  pinMode(echo1, INPUT);
  
  pinMode(trig2, OUTPUT); // Sets the trigPin as an Output
  pinMode(echo2, INPUT);
  pinMode(trig3, OUTPUT); // Sets the trigPin as an Output
  pinMode(echo3, INPUT);
  
  pinMode(A0, OUTPUT);
  pinMode(A1, OUTPUT);
  pinMode(A2, OUTPUT);
  pinMode(A3, OUTPUT);
  
  Serial.begin(9600);

}

void headPositionFix(){
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distanceCm = duration * 0.034 / 2;
  Serial.println(distanceCm);
  if (distanceCm >= 22 && distanceCm <= 35)
  { myservo1.write(70);
    delay(1500);
    myservo1.write(80);
    delay(1500);
    myservo1.write(90);
    delay(1500);
    myservo1.write(100);
    delay(1500);
    myservo1.write(110);
    delay(1500);
    myservo1.write(100);
    delay(1500);
    myservo1.write(90);
    delay(1500);
    myservo1.write(80);
    delay(1500);
    myservo1.write(70);
    delay(1500);

    delay(2500);

    myservo2.write(100);
    delay(1500);


    myservo2.write(110);
    delay(1500);

    myservo2.write(120);
    delay(1500);


    myservo2.write(130);
    delay(1500);

    myservo2.write(140);
    delay(1500);


    myservo2.write(150);
    delay(1500);


    myservo2.write(160);
    delay(1500);


    myservo2.write(170);
    delay(1500);
    myservo2.write(180);
    delay(1500);

    myservo2.write(170);
    delay(1500);

    myservo2.write(160);
    delay(1500);

    myservo2.write(150);
    delay(1500);

    myservo2.write(140);
    delay(1500);


    myservo2.write(130);
    delay(1500);
    myservo2.write(120);
    delay(1500);

    myservo2.write(110);
    delay(1500);

    myservo2.write(100);
    delay(1500);


  }
  //delay(1000);
}

int checkdistance(int echo, int trig)
{
  long duration;
  int distance;

  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  duration = pulseIn(echo, HIGH);
  distance = duration * 0.034 / 2;

  if (distance >= 30)
    return 1;
  else
    return 0;
}

void forward()
{
  digitalWrite(A1, HIGH);
  digitalWrite(A0, LOW);
  digitalWrite(A2, HIGH);
  digitalWrite(A3, LOW);
  delay(3000);

}

void back()
{
  digitalWrite(A0, HIGH);
  digitalWrite(A1, LOW);
  digitalWrite(A3, HIGH);
  digitalWrite(A2, LOW);
  delay(3000);

}
void right()
{
  digitalWrite(A1, HIGH);
  digitalWrite(A0, LOW);
  digitalWrite(A3, LOW);
  digitalWrite(A2, LOW);
  delay(3000);

}
void left()
{
  digitalWrite(A0, LOW);
  digitalWrite(A1, LOW);
  digitalWrite(A2, HIGH);
  digitalWrite(A3, LOW);
  delay(3000);

}


void stopped()
{
  digitalWrite(A0, LOW);
  digitalWrite(A1, LOW);
  digitalWrite(A2, LOW);
  digitalWrite(A3, LOW);
  delay(3000);

}

// *********---------LOOP----------******

void loop() {
  int Received = 0;

  headPositionFix();

  if (Serial.available() > 0)
  {
    Received = Serial.parseInt();
    Serial.println(Received);

  }

  // FOR FORWARD
  if (Received == 1)//ascii 49 =1
  {

    forward();

    Serial.println("FORWARD GOING   ");
    Serial.println(Received);
    delay(1000);
  }
  else if (Received == 5) //ascii 53=5
  {

    stopped();

    Serial.println("STOPPED ");
    Serial.println(Received);
    delay(1000);

  }
  else if (Received == 2) //ascii 50=2
  {
    int check = checkdistance(echo3, trig3);
    if (check == 1)
    {

    back();

    Serial.println("BACKED ");
    Serial.println(Received);
    delay(1000);
   }
    else
    stopped();

  }
  else if (Received == 3) //ascii 51=3
  {
    int check = checkdistance(echo1, trig1);
    if (check == 1)
    {

      left();

      Serial.println("left ");
      delay(1000);

    }
    else
      stopped();


  }
  else if (Received == 4) //ascii  52=4
  {
      int check = checkdistance(echo2, trig2);
     if (check == 1)
     {
      right();

      Serial.println("right ");
      Serial.println(Received);
      delay(1000);
    }
    else
    {
        stopped();
    }

  }

  headPositionFix();
  delay(2000);

}
