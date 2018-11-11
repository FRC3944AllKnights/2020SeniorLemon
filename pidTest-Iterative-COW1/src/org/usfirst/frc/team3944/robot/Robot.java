
package org.usfirst.frc.team3944.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;


import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;




 
 
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements PIDOutput {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
    
    
    AHRS ahrs;
    RobotDrive myRobot;
    Joystick stick;
    PIDController turnController;
    PIDController autoController;
    double rotateToAngleRate;

    /* The following PID Controller coefficients will need to be tuned */
    /* to match the dynamics of your drive system.  Note that the      */
    /* SmartDashboard in Test mode has support for helping you tune    */
    /* controllers by displaying a form where you can enter new P, I,  */
    /* and D constants and test the mechanism.                         */
    /* Kp the proportional coefficient								   */	
    /* Ki the integral coefficient									   */
    /* Kd the derivative coefficient								   */
    /* Kf the feed forward term										   */

    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;

    static final double kToleranceDegrees = 2.0f;
    
    // Autonomous variables
    
     static final double a_kP = 0.03;
     static final double a_kI = 0.00;
     static final double a_kD = 0.00;
     static final double a_kF = 0.00;
     static final double a_kToleranceDegrees = 2.0f;
     private double Y_Mag 	=  0.2;
	 private double X_Mag	=  0.0;
	 private double testAngle = 5.0f;
	 
	 

    // Channels for the wheels
    Talon frontleft,rearleft,frontright,rearright;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        frontleft  = new Talon(0);
        rearleft   = new Talon (1);
        frontright = new Talon(2);
        rearright  = new Talon(3);
        myRobot    = new RobotDrive(frontleft, rearleft, frontright, rearright);
        myRobot.setExpiration(0.1);
        stick = new Joystick(0);
        try {
            /* Communicate w/navX MXP via the MXP SPI Bus.                                     */
            /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
            /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }
        turnController = new PIDController(kP, kI, kD, kF, ahrs, this);
        turnController.setInputRange(-180.0f,  180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(kToleranceDegrees);
        turnController.setContinuous(true);

      //  myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
      //  myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        
        /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
        /* tuning of the Turn Controller's P, I and D coefficients.            */
        /* Typically, only the P value needs to be modified.                   */
        LiveWindow.addActuator("DriveSystem", "RotateController", turnController);
        LiveWindow.addActuator("Talon", "Front Left Talon", frontleft);
        LiveWindow.addActuator("Talon", "Rear Left Talon", rearleft);
        LiveWindow.addActuator("Talon", "Front Right Talon", frontright);
        LiveWindow.addActuator("Talon", "Rear Right Talon", rearright);
        
        /* AHRS methods */
        ahrs.reset();
   	    ahrs.isConnected(); // Tells if the sensor is connected
   		ahrs.getBarometricPressure(); // Get B pressure
   		ahrs.isMagnetometerCalibrated(); 
   		ahrs.isMagneticDisturbance(); // Indicates whether the current magnetic field strength diverges from the calibrated value for the earth's magnetic field
   		ahrs.getFusedHeading();
		ahrs.zeroYaw(); 
		ahrs.getCompassHeading(); 

		ahrs.getTempC(); // Temp Centigrade
		
	    ahrs.pidGet(); //returns yaw degrees 
	    
	    ahrs.getFirmwareVersion();
	    ahrs.isMoving();
        
        
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
	   
		ahrs.reset();
		ahrs.isConnected(); // Tells if the sensor is connected
		ahrs.getBarometricPressure(); // Get B pressure
		ahrs.isMagnetometerCalibrated(); 
		ahrs.isMagneticDisturbance(); // Indicates whether the current magnetic field strength diverges from the calibrated value for the earth's magnetic field
		/*
		 * Returns the "fused" (9-axis) heading.
The 9-axis heading is the fusion of the yaw angle, the tilt-corrected compass heading, and magnetic disturbance detection. Note that the magnetometer calibration procedure is required in order to achieve valid 9-axis headings.

The 9-axis Heading represents the sensor's best estimate of current heading, based upon the last known valid Compass Angle, and updated by the change in the Yaw Angle since the last known valid Compass Angle. The last known valid Compass Angle is updated whenever a Calibrated Compass Angle is read and the sensor has recently rotated less than the Compass Noise Bandwidth (~2 degrees).

Returns:
Fused Heading in Degrees (range 0-360)
		 */
		ahrs.getFusedHeading();
		ahrs.zeroYaw(); // Sets the user-specified yaw offset to the current yaw value reported by the sensor.
		           // This user-specified yaw offset is automatically subtracted from subsequent yaw values reported by the getYaw() method.
		ahrs.getCompassHeading(); 
		/* Returns the current tilt-compensated compass heading value (in degrees, from 0 to 360) reported by the sensor.
		Note that this value is sensed by a magnetometer, which can be affected by nearby magnetic fields (e.g., the magnetic fields generated by nearby motors).

		Before using this value, ensure that (a) the magnetometer has been calibrated and (b) that a magnetic disturbance is not taking place at the instant when the compass heading was generated.
		*/
		ahrs.getTempC(); // Temp Centigrade
		
	    ahrs.pidGet(); //returns yaw degrees 
	    
	    ahrs.getFirmwareVersion();
	    
	    
		
		autoController = new PIDController(a_kP, a_kI, a_kD, a_kF, ahrs, this);
		autoController.setInputRange(-180.0f,  180.0f);
		autoController.setOutputRange(-1.0, 1.0);  // this could also be -0.25, 0.25
		autoController.setAbsoluteTolerance(a_kToleranceDegrees);
		autoController.setContinuous(true);
		autoController.enable();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
            myRobot.setSafetyEnabled(false);
          
            
            //myRobot.mecanumDrive_Cartesian(stick.getX(), stick.getY(), 
            //                               currentRotationRate, ahrs.getAngle());
       // if ahrs.getAngle() <= 4 degrees  or ahrs.getAngle() >= 6 degrees 
       //  then drive x-speed=0, y-speed=0.2, PID=autoController.setSetpoint(5.0f);, 5.0 degrees
            
    double currentRotationRate;  
    if ((ahrs.getYaw() <= (4.0f)) || (ahrs.getYaw() >= (6.0f))) {
    	X_Mag = 0.0;
    	Y_Mag = 0.2;
    	autoController.setSetpoint(5.0f);
    	testAngle = 5.0f;
    }
    currentRotationRate = rotateToAngleRate;   
    //myRobot.mecanumDrive_Cartesian(X_Mag, Y_Mag, currentRotationRate, ahrs.getAngle());
    myRobot.mecanumDrive_Cartesian(X_Mag, Y_Mag, currentRotationRate, testAngle);
    
    SmartDashboard.putString("YAW: ", "" +ahrs.getYaw());
    SmartDashboard.putString("Angle: ", "" +ahrs.getAngle());
    SmartDashboard.putString("isConnected: ", "" +ahrs.isConnected());
    SmartDashboard.putString("Barometric Pressure: ", "" +ahrs.getBarometricPressure());
    SmartDashboard.putString("isMagnetometerCalibrated: ", "" +ahrs.isMagnetometerCalibrated());
    SmartDashboard.putString("isMagneticDisturbance: ", "" +ahrs.isMagneticDisturbance());
    SmartDashboard.putString("getFusedHeading: ", "" +ahrs.getFusedHeading());
    SmartDashboard.putString("getCompassHeading: ", "" +ahrs.getCompassHeading());
    SmartDashboard.putString("getTempC: ", "" +ahrs.getTempC());
    SmartDashboard.putString("pidGet: ", "" +ahrs.pidGet());
    SmartDashboard.putString("getFirmwareVersion: ", "" +ahrs.getFirmwareVersion());
    SmartDashboard.putString("isMoving: ", "" +ahrs.isMoving());
            
             // Magnitude (speed), Direction (degrees), Rotation (Turning)
    Timer.delay(6.0);	
    autoController.disable();		
            break;
    	case defaultAuto:
    	default:
    		 myRobot.setSafetyEnabled(false);
             myRobot.drive(0.1, 0.0);    // stop robot
             Timer.delay(2.0);		    //    for 2 seconds
             myRobot.drive(0.0, 0.0);	// stop robot

             break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	myRobot.setSafetyEnabled(true);
       // while (isOperatorControl() && isEnabled()) {
            boolean rotateToAngle = false;
            if ( stick.getRawButton(1)) {
                ahrs.reset();
            }
            if ( stick.getRawButton(2)) {
                turnController.setSetpoint(0.0f);
                rotateToAngle = true;
            } else if ( stick.getRawButton(3)) {
                turnController.setSetpoint(90.0f);
                rotateToAngle = true;
            } else if ( stick.getRawButton(4)) {
                turnController.setSetpoint(179.9f);
                rotateToAngle = true;
            } else if ( stick.getRawButton(5)) {
                turnController.setSetpoint(-90.0f);
                rotateToAngle = true;
            }
            double currentRotationRate;
            if ( rotateToAngle ) {
                turnController.enable();
                currentRotationRate = rotateToAngleRate;
            } else {
                turnController.disable();
                currentRotationRate = stick.getTwist();
            }
            try {
                /* Use the joystick X axis for lateral movement,          */
                /* Y axis for forward movement, and the current           */
                /* calculated rotation rate (or joystick Z axis),         */
                /* depending upon whether "rotate to angle" is active.    */
                myRobot.mecanumDrive_Cartesian(stick.getX(), stick.getY(), 
                                               currentRotationRate, ahrs.getAngle());
            
            	

            	// Arcade drive implements single stick driving.
            	// This function lets you directly provide joystick values from any source.
            	// Use the joystick Y axis for moveValue 
            	// public void arcadeDrive(double moveValue, double rotateValue)
            	// Use either currentRotationRate or ahrs.getAngle() for rotateValue
            	
            	//myRobot.arcadeDrive(stick.getY(), (currentRotationRate * 0.25));
            
            } catch( RuntimeException ex ) {
                DriverStation.reportError("Error communicating with drive system:  " + ex.getMessage(), true);
            }
            Timer.delay(0.005);		// wait for a motor update time
         
            SmartDashboard.putString("currentRotationRate: ", "" +currentRotationRate);
            SmartDashboard.putString("rotateToAngleRate: ", "" +rotateToAngleRate);
            SmartDashboard.putString("kP: ", "" +kP);
            SmartDashboard.putString("kI: ", "" +kI);
            SmartDashboard.putString("kD: ", "" +kD);
            SmartDashboard.putString("kF: ", "" +kF);
            
            
            SmartDashboard.putString("YAW: ", "" +ahrs.getYaw());
            SmartDashboard.putString("Angle: ", "" +ahrs.getAngle());
            SmartDashboard.putString("isConnected: ", "" +ahrs.isConnected());
            SmartDashboard.putString("Barometric Pressure: ", "" +ahrs.getBarometricPressure());
            SmartDashboard.putString("isMagnetometerCalibrated: ", "" +ahrs.isMagnetometerCalibrated());
            SmartDashboard.putString("isMagneticDisturbance: ", "" +ahrs.isMagneticDisturbance());
            SmartDashboard.putString("getFusedHeading: ", "" +ahrs.getFusedHeading());
            SmartDashboard.putString("getCompassHeading: ", "" +ahrs.getCompassHeading());
            SmartDashboard.putString("getTempC: ", "" +ahrs.getTempC());
            SmartDashboard.putString("pidGet: ", "" +ahrs.pidGet());
            SmartDashboard.putString("getFirmwareVersion: ", "" +ahrs.getFirmwareVersion());
            SmartDashboard.putString("isMoving: ", "" +ahrs.isMoving());


      //  }   while 
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }	
    	
    
    @Override
        /* This function is invoked periodically by the PID Controller, */
        /* based upon navX MXP yaw angle input and PID Coefficients.    */
        public void pidWrite(double output) {
            rotateToAngleRate = output;
        }
       
    
}
