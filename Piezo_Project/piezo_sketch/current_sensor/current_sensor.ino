#define VIN A0
const float VCC = 5.0;
//const int model = 0;

const float cutOffLimit = 0.1; // 0.00000001;  //0.5;
const float sensitivity = 0.185;
const float QOV = 0.5 * VCC;
float voltage;

void setup() {
  Serial.begin(9600);
}

void loop() {
  float voltage_raw = (5.0/1023.0) * analogRead(VIN);
  voltage = voltage_raw - QOV + 0.012;
  float current = voltage/sensitivity;
  if(abs(current)>cutOffLimit){
    Serial.print("V: ");
      Serial.print(voltage, 3);
      Serial.print("V, I: ");
      Serial.print(current, 8);
      Serial.println("A");
    }else{
      Serial.println("No Current");  
    }
    delay(500);
}
