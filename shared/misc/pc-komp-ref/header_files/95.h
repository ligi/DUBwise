typedef struct
{
  unsigned char Revision;
  unsigned char Kanalbelegung[12];       // GAS[0], GIER[1],NICK[2], ROLL[3], POTI1, POTI2, POTI3
  unsigned char GlobalConfig;           // 0x01=Höhenregler aktiv,0x02=Kompass aktiv, 0x04=GPS aktiv, 0x08=Heading Hold aktiv
  unsigned char Hoehe_MinGas;           // Wert : 0-100
  unsigned char Luftdruck_D;            // Wert : 0-250
  unsigned char HoeheChannel;           // Wert : 0-32
  unsigned char Hoehe_P;                // Wert : 0-32
  unsigned char Hoehe_Verstaerkung;     // Wert : 0-50
  unsigned char Hoehe_ACC_Wirkung;      // Wert : 0-250
  unsigned char Hoehe_HoverBand;        // Wert : 0-250
  unsigned char Hoehe_GPS_Z;            // Wert : 0-250
  unsigned char Hoehe_StickNeutralPoint;// Wert : 0-250
  unsigned char Stick_P;                // Wert : 1-6
  unsigned char Stick_D;                // Wert : 0-64
  unsigned char StickGier_P;            // Wert : 1-20
  unsigned char Gas_Min;                // Wert : 0-32
  unsigned char Gas_Max;                // Wert : 33-250
  unsigned char GyroAccFaktor;          // Wert : 1-64
  unsigned char KompassWirkung;         // Wert : 0-32
  unsigned char Gyro_P;                 // Wert : 10-250
  unsigned char Gyro_I;                 // Wert : 0-250
  unsigned char Gyro_D;                 // Wert : 0-250
  unsigned char Gyro_Gier_P;            // Wert : 10-250
  unsigned char Gyro_Gier_I;            // Wert : 0-250
  unsigned char Gyro_Stability;         // Wert : 0-16
  unsigned char UnterspannungsWarnung;  // Wert : 0-250
  unsigned char NotGas;                 // Wert : 0-250     //Gaswert bei Empängsverlust
  unsigned char NotGasZeit;             // Wert : 0-250     // Zeitbis auf NotGas geschaltet wird, wg. Rx-Problemen
  unsigned char Receiver;                   // 0= Summensignal, 1= Spektrum, 2 =Jeti, 3=ACT DSL, 4=ACT S3D
  unsigned char I_Faktor;               // Wert : 0-250
  unsigned char UserParam1;             // Wert : 0-250
  unsigned char UserParam2;             // Wert : 0-250
  unsigned char UserParam3;             // Wert : 0-250
  unsigned char UserParam4;             // Wert : 0-250
  unsigned char ServoNickControl;       // Wert : 0-250     // Stellung des Servos
  unsigned char ServoNickComp;          // Wert : 0-250     // Einfluss Gyro/Servo
  unsigned char ServoNickMin;           // Wert : 0-250     // Anschlag
  unsigned char ServoNickMax;           // Wert : 0-250     // Anschlag
  //--- Seit V0.75
  unsigned char ServoRollControl;       // Wert : 0-250     // Stellung des Servos
  unsigned char ServoRollComp;          // Wert : 0-250
  unsigned char ServoRollMin;           // Wert : 0-250
  unsigned char ServoRollMax;           // Wert : 0-250
  //---
  unsigned char ServoNickRefresh;       // Speed of the Servo
  unsigned char ServoManualControlSpeed;//
  unsigned char CamOrientation;         //
  unsigned char Servo3;                    // Value or mapping of the Servo Output
  unsigned char Servo4;                            // Value or mapping of the Servo Output
  unsigned char Servo5;                            // Value or mapping of the Servo Output
  unsigned char LoopGasLimit;           // Wert: 0-250  max. Gas während Looping
  unsigned char LoopThreshold;          // Wert: 0-250  Schwelle für Stickausschlag
  unsigned char LoopHysterese;          // Wert: 0-250  Hysterese für Stickausschlag
  unsigned char AchsKopplung1;          // Wert: 0-250  Faktor, mit dem Gier die Achsen Roll und Nick koppelt (NickRollMitkopplung)
  unsigned char AchsKopplung2;          // Wert: 0-250  Faktor, mit dem Nick und Roll verkoppelt werden
  unsigned char CouplingYawCorrection;  // Wert: 0-250  Faktor, mit dem Nick und Roll verkoppelt werden
  unsigned char WinkelUmschlagNick;     // Wert: 0-250  180°-Punkt
  unsigned char WinkelUmschlagRoll;     // Wert: 0-250  180°-Punkt
  unsigned char GyroAccAbgleich;        // 1/k  (Koppel_ACC_Wirkung)
  unsigned char Driftkomp;
  unsigned char DynamicStability;
  unsigned char UserParam5;             // Wert : 0-250
  unsigned char UserParam6;             // Wert : 0-250
  unsigned char UserParam7;             // Wert : 0-250
  unsigned char UserParam8;             // Wert : 0-250
  //---Output ---------------------------------------------
  unsigned char J16Bitmask;             // for the J16 Output
  unsigned char J16Timing;              // for the J16 Output
  unsigned char J17Bitmask;             // for the J17 Output
  unsigned char J17Timing;              // for the J17 Output
  // seit version V0.75c
  unsigned char WARN_J16_Bitmask;       // for the J16 Output
  unsigned char WARN_J17_Bitmask;       // for the J17 Output
  //---NaviCtrl---------------------------------------------
  unsigned char NaviOut1Parameter;      // for the J16 Output
  unsigned char NaviGpsModeChannel;     // Parameters for the Naviboard
  unsigned char NaviGpsGain;
  unsigned char NaviGpsP;
  unsigned char NaviGpsI;
  unsigned char NaviGpsD;
  unsigned char NaviGpsPLimit;
  unsigned char NaviGpsILimit;
  unsigned char NaviGpsDLimit;
  unsigned char NaviGpsA;
  unsigned char NaviGpsMinSat;
  unsigned char NaviStickThreshold;
  unsigned char NaviWindCorrection;
  unsigned char NaviAccCompensation;    // New since 0.86 -> was: SpeedCompensation
  unsigned char NaviOperatingRadius;
  unsigned char NaviAngleLimitation;
  unsigned char NaviPH_LoginTime;
  //---Ext.Ctrl---------------------------------------------
  unsigned char ExternalControl;         // for serial Control
  //---CareFree---------------------------------------------
  unsigned char OrientationAngle;        // Where is the front-direction?
  unsigned char CareFreeChannel;     // switch for CareFree
  unsigned char MotorSafetySwitch;
  unsigned char MotorSmooth;
  unsigned char ComingHomeAltitude;
  unsigned char FailSafeTime;
  unsigned char MaxAltitude;
  unsigned char FailsafeChannel;         // if the value of this channel is > 100, the MK reports "RC-Lost"
  unsigned char ServoFilterNick;  
  unsigned char ServoFilterRoll;  
  unsigned char StartLandChannel;  
  unsigned char LandingSpeed;  
  //------------------------------------------------
  unsigned char BitConfig;          // (war Loop-Cfg) Bitcodiert: 0x01=oben, 0x02=unten, 0x04=links, 0x08=rechts / wird getrennt behandelt
  unsigned char ServoCompInvert;    // //  0x01 = Nick, 0x02 = Roll, 0x04 = relative moving // WICHTIG!!! am Ende lassen
  unsigned char ExtraConfig;        // bitcodiert
  unsigned char GlobalConfig3;      // bitcodiert
  char Name[12];
  unsigned char crc;                                // must be the last byte!
} 
