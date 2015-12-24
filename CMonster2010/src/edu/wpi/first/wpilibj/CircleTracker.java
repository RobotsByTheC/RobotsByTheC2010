/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

//TODO: switch to using alternate blocking function call with specific task
//TODO: fix PCVideo server failure killing crio
//TODO: Tune loop better
//TODO: add more joystick functionality
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CircleTracker
{
    double kScoreThreshold = .01;
    AxisCamera cam;
    Gyro gyro = new Gyro(1);
    BetterRobotDrive brDrive;

//        brDrive.setInvertedMotor(BetterRobotDrive.MotorType.kRearLeft, true);
//        brDrive.setInvertedMotor(BetterRobotDrive.MotorType.kRearRight, true);
    
    Joystick js;
    PIDController turnController = new PIDController(.08, 0.0, 0.5, gyro, new PIDOutput() {

        public void pidWrite(double output) {
//            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, Double.toString(output));
            brDrive.circleDrive(output);
        }
    }, .005);
    TrackerDashboard trackerDashboard = new TrackerDashboard();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void doRobotInit(AxisCamera camera, BetterRobotDrive betterDrive, Joystick j)
    {
        cam = camera;
        brDrive = betterDrive;
        js = j;
        gyro.setSensitivity(.007);
        turnController.setInputRange(-360.0, 360.0);
        turnController.setTolerance(1 / 90. * 100);
        turnController.disable();
    }

    boolean lastTrigger = false;

    public void trackCirclePeriodic() {

   //     DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, Double.toString(gyro.pidGet()));
        long startTime = Timer.getUsClock();
        if (!js.getRawButton(2)) {
            if (lastTrigger) {
                turnController.disable();
            }
            lastTrigger = false;
        }
        else {
            if (!lastTrigger) {
                turnController.enable();
                turnController.setSetpoint(gyro.pidGet());
            }
            lastTrigger = true;
            ColorImage image = null;
            try {
                if (cam.freshImage()) {// && turnController.onTarget()) {
                    double gyroAngle = gyro.pidGet();
                    image = cam.getImage();
                    Thread.yield();
                    Target[] targets = Target.findCircularTargets(image);
                    Thread.yield();
                    if (targets.length == 0 || targets[0].m_score < kScoreThreshold) {
                        System.out.println("No target found");
                        Target[] newTargets = new Target[targets.length + 1];
                        newTargets[0] = new Target();
                        newTargets[0].m_majorRadius = 0;
                        newTargets[0].m_minorRadius = 0;
                        newTargets[0].m_score = 0;
                        for (int i = 0; i < targets.length; i++) {
                            newTargets[i + 1] = targets[i];
                        }
                        trackerDashboard.updateVisionDashboard(0.0, gyro.getAngle(), 0.0, 0.0, newTargets);
                    } else {
                        System.out.println(targets[0]);
                        System.out.println("Target Angle: " + targets[0].getHorizontalAngle());
                        turnController.setSetpoint(gyroAngle + targets[0].getHorizontalAngle());
                        trackerDashboard.updateVisionDashboard(0.0, gyro.getAngle(), 0.0, targets[0].m_xPos / targets[0].m_xMax, targets);
                    }
                }
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (image != null) {
                        image.free();
                    }
                } catch (NIVisionException ex) {
                }
            }
            System.out.println("Time : " + (Timer.getUsClock() - startTime) / 1000000.0);
            System.out.println("Gyro Angle: " + gyro.getAngle());
        }
    }
}
