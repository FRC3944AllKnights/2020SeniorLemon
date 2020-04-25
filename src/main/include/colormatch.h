#ifndef COLORMATCH_H
#define COLORMATCH_H
#include <frc/TimedRobot.h>
#include <frc/smartdashboard/smartdashboard.h>
#include <frc/util/color.h>
#include "rev/ColorSensorV3.h"
#include "rev/ColorMatch.h"

class colormatch{
    public:
        colormatch();
        void init();
        void colorPeriodic();
        
    private:
     static constexpr auto i2cPort = frc::I2C::Port::kOnboard;

     rev::ColorSensorV3 m_colorSensor{i2cPort};
     rev::ColorMatch m_colorMatcher;
     static constexpr frc::Color kBlueTarget = frc::Color(0.143, 0.427, 0.429);
     static constexpr frc::Color kGreenTarget = frc::Color(0.197, 0.561, 0.240);
     static constexpr frc::Color kRedTarget = frc::Color(0.561, 0.232, 0.114);
     static constexpr frc::Color kYellowTarget = frc::Color(0.361, 0.524, 0.113);
};
#endif