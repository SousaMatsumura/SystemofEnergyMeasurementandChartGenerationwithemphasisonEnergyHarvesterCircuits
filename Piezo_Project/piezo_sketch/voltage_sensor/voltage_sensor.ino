const float R1 = 30000.0;
const float R2 = 7500.0;

void setup(){
  Serial.begin(9600);
}

void loop(){
  //float voltage = (float) ((5/1023)*analogRead(A1)*(R1+R2))/R2;
  float voltage = (float) (5*analogRead(A1)*(R1+R2))/(1023.0*R2);
  //float current = (float) (5*analogRead(A1))/(1023.0*R2);
  if(voltage >= 0.35){
     Serial.println(voltage, 8);
     //Serial.print("|");
     //Serial.println(voltage/(R1+R2), 8);
     //Serial.println(" volts;");
  }
  delay(1);
}
