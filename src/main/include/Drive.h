#ifndef DRIVE_H
#define DRIVE_H
#include <frc/drive/DifferentialDrive.h>
#include <frc/SpeedControllerGroup.h>
#include <ctre/Phoenix.h>

class Drive{
    public:
        Drive();
        void init();
        void Arcade(double x, double y);

    private:
        WPI_TalonSRX m_frontLeft{4};
      //  WPI_TalonSRX m_rearLeft{2};
        WPI_TalonSRX m_frontRight{3};
      //  WPI_TalonSRX m_rearRight{4};
      //  frc::SpeedControllerGroup m_Left{m_frontLeft, m_rearLeft};
      //  frc::SpeedControllerGroup m_Right{m_frontRight, m_rearRight};
        frc::DifferentialDrive m_robotDrive{m_frontLeft, m_frontRight};
};

#endif