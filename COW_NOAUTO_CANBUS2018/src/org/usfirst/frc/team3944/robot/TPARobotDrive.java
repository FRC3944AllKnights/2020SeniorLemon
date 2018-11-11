/**
 *
 *  @author - gdl
 *  @version  1.00 01/10/2015
 *  
 *  team #   -- 3944
 *  
 * COMMENTS:
 *
 * This is called by the main Robot class.
 *       
 *
 * REVISIONS:
 * 
 *  Deployment - 1.00 - jd  - Initial Deployment
 *
 */

// Imports go here
package org.usfirst.frc.team3944.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;

//main class body
//public class TPARobotDrive extends RobotDrive {
public class TPARobotDrive extends MecanumDrive{
	private double m_magnitude;
	private double m_direction;
	private double m_rotation;
	
	// This is an object declaration that sets a named location in memory. It is a joystick 
	// object of type TPAJoystick. Or a reference variable of type TPAJoystick. 
	// "final" tells the compiler that subclass cannot override.
	private final TPAJoystick joystick ;
	
	// Instantiation of public static CANTalon Object
	//public static TPATalonSRX talon = new TPATalonSRX(1);
	public static WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(TPARobotMap.frontLeftPort);
	public static WPI_TalonSRX backLeftMotor = new WPI_TalonSRX(TPARobotMap.rearLeftPort);
	public static WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(TPARobotMap.frontRightPort);
	public static WPI_TalonSRX backRightMotor = new WPI_TalonSRX(TPARobotMap.rearRightPort);
	
	// Constructor
	public TPARobotDrive(WPI_TalonSRX frontLeftMotor, WPI_TalonSRX rearLeftMotor, WPI_TalonSRX frontRightMotor, WPI_TalonSRX rearRightMotor, TPAJoystick joystick) {
		super(frontLeftMotor,rearLeftMotor,frontRightMotor,rearRightMotor);
		this.joystick=joystick;
	}
	
	// Method
	public void mecanumDrive_Polar() {
		//double throttle=(joystick.getRawAxis(4) - 1) / -2;
		// TPALCD.getInstance().println(1, "Speed mult: x" + throttle);
		//SmartDashboard.putNumber("Speed multiplier", throttle);
		m_magnitude = joystick.getMagnitude() * .8;
		m_direction = joystick.getDirectionDegrees();
		m_rotation = joystick.getTwist() * .8;
		
		drivePolar(m_magnitude, m_direction, m_rotation);
	}
}
