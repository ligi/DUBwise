/*#######################################################################################
Flight Control
#######################################################################################*/

#ifndef _FC_H
#define _FC_H
//#define GIER_GRAD_FAKTOR 1291L // Abhängigkeit zwischen GyroIntegral und Winkel
//#define GIER_GRAD_FAKTOR 1160L 
extern long GIER_GRAD_FAKTOR; // Abhängigkeit zwischen GyroIntegral und Winkel
#define STICK_GAIN 4

#define FLAG_MOTOR_RUN  1
#define FLAG_FLY        2
#define FLAG_CALIBRATE  4
#define FLAG_START      8
#define MAX_MOTORS      12

#define CHECK_MIN_MAX(wert,min,max) {if(wert < min) wert = min; else if(wert > max) wert = max;}

extern volatile unsigned char MikroKopterFlags;
extern volatile unsigned int I2CTimeout;
extern unsigned char Sekunde,Minute;

extern long IntegralNick,IntegralNick2;
extern long IntegralRoll,IntegralRoll2;
//extern int IntegralNick,IntegralNick2;
//extern int IntegralRoll,IntegralRoll2;

extern long Mess_IntegralNick,Mess_IntegralNick2;
extern long Mess_IntegralRoll,Mess_IntegralRoll2;
extern long IntegralAccNick,IntegralAccRoll;
extern volatile long Mess_Integral_Hoch;
extern long Integral_Gier,Mess_Integral_Gier,Mess_Integral_Gier2;
extern int  KompassValue;
extern int  KompassStartwert;
extern int  KompassRichtung;
extern int  TrimNick, TrimRoll;
extern long  ErsatzKompass;
extern int   ErsatzKompassInGrad; // Kompasswert in Grad
extern int HoehenWert;
extern int SollHoehe;
extern int MesswertNick,MesswertRoll,MesswertGier;
extern int AdNeutralNick,AdNeutralRoll,AdNeutralGier, Mittelwert_AccNick, Mittelwert_AccRoll;
extern int NeutralAccX, NeutralAccY,Mittelwert_AccHoch;
extern unsigned char HoehenReglerAktiv;
extern volatile float NeutralAccZ;
extern long Umschlag180Nick, Umschlag180Roll;
extern signed int ExternStickNick,ExternStickRoll,ExternStickGier;
extern unsigned char Parameter_UserParam1,Parameter_UserParam2,Parameter_UserParam3,Parameter_UserParam4,Parameter_UserParam5,Parameter_UserParam6,Parameter_UserParam7,Parameter_UserParam8;
extern int NaviAccNick,NaviAccRoll,NaviCntAcc;
extern unsigned int modell_fliegt;
void MotorRegler(void);
void SendMotorData(void);
void CalibrierMittelwert(void);
void Mittelwert(void);
void SetNeutral(void);
void Piep(unsigned char Anzahl, unsigned int dauer);

extern unsigned char h,m,s;
extern volatile unsigned char Timeout ;
extern unsigned char CosinusNickWinkel, CosinusRollWinkel;
extern int  DiffNick,DiffRoll;
extern int  Poti1, Poti2, Poti3, Poti4; 
extern volatile unsigned char SenderOkay;
extern unsigned char RequiredMotors;
extern int StickNick,StickRoll,StickGier;
extern char MotorenEin;
extern void DefaultKonstanten1(void);
extern void DefaultKonstanten2(void);
extern void DefaultKonstanten3(void);
extern void DefaultStickMapping(void);

#define  STRUCT_PARAM_LAENGE  sizeof(EE_Parameter) 
struct mk_param_struct
 {
   unsigned char Kanalbelegung[8];       // GAS[0], GIER[1],NICK[2], ROLL[3], POTI1, POTI2, POTI3
   unsigned char GlobalConfig;           // 0x01=Höhenregler aktiv,0x02=Kompass aktiv, 0x04=GPS aktiv, 0x08=Heading Hold aktiv
   unsigned char Hoehe_MinGas;           // Wert : 0-100
   unsigned char Luftdruck_D;            // Wert : 0-250
   unsigned char MaxHoehe;               // Wert : 0-32
   unsigned char Hoehe_P;                // Wert : 0-32
   unsigned char Hoehe_Verstaerkung;     // Wert : 0-50
   unsigned char Hoehe_ACC_Wirkung;      // Wert : 0-250
   unsigned char Stick_P;                // Wert : 1-6
   unsigned char Stick_D;                // Wert : 0-64
   unsigned char Gier_P;                 // Wert : 1-20
   unsigned char Gas_Min;                // Wert : 0-32
   unsigned char Gas_Max;                // Wert : 33-250
   unsigned char GyroAccFaktor;          // Wert : 1-64
   unsigned char KompassWirkung;         // Wert : 0-32
   unsigned char Gyro_P;                 // Wert : 10-250
   unsigned char Gyro_I;                 // Wert : 0-250
   unsigned char Gyro_D;                 // Wert : 0-250
   unsigned char UnterspannungsWarnung;  // Wert : 0-250
   unsigned char NotGas;                 // Wert : 0-250     //Gaswert bei Empängsverlust
   unsigned char NotGasZeit;             // Wert : 0-250     // Zeitbis auf NotGas geschaltet wird, wg. Rx-Problemen
   unsigned char UfoAusrichtung;         // X oder + Formation
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
   unsigned char ServoNickRefresh;       //
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
//---NaviCtrl---------------------------------------------
   unsigned char NaviGpsModeControl;     // Parameters for the Naviboard
   unsigned char NaviGpsGain;     
   unsigned char NaviGpsP;        
   unsigned char NaviGpsI;        
   unsigned char NaviGpsD;        
   unsigned char NaviGpsPLimit;        
   unsigned char NaviGpsILimit;        
   unsigned char NaviGpsDLimit;        
   unsigned char NaviGpsACC;        
   unsigned char NaviGpsMinSat;        
   unsigned char NaviStickThreshold;        
   unsigned char NaviWindCorrection;        
   unsigned char NaviSpeedCompensation;        
   unsigned char NaviOperatingRadius;        
   unsigned char NaviAngleLimitation;
   unsigned char NaviPH_LoginTime;
//---Ext.Ctrl---------------------------------------------
   unsigned char ExternalControl;        // for serial Control
//------------------------------------------------
   unsigned char BitConfig;             // (war Loop-Cfg) Bitcodiert: 0x01=oben, 0x02=unten, 0x04=links, 0x08=rechts / wird getrennt behandelt
   unsigned char ServoCompInvert;    // //  0x01 = Nick, 0x02 = Roll   0 oder 1  // WICHTIG!!! am Ende lassen
   unsigned char Reserved[4];
   char Name[12];
 }; 
