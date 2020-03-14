#include "Drive.h"

Drive::Drive(){
}

void Drive::init(){
    m_frontLeft.SetInverted(true);
   // m_rearLeft.SetInverted(true);
    m_frontRight.SetInverted(true);
   // m_rearRight.SetInverted(true);
}

void Drive::Arcade(double x, double y){
    m_robotDrive.ArcadeDrive(x*0.8, y*0.6);
}