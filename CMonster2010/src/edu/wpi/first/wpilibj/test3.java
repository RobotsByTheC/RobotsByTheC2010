package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.camera.*;

public class test3 extends IterativeRobot
{
    public final double kWheelCircumference = 25.13274122872; // 8 inches * pi
    
//    AxisCamera cam;

    Joystick stick;
    Compressor compEric;
    Relay kickRelay;
    Jaguar roller;
//    Encoder encoder;
    Timer tTime;

    boolean rollerDirection;
    boolean pump;
    double rollerSpeed;

    // autonomouse
    int rotations;
    int wheelToKick;
    double speed;
    boolean timerRun, kick1, kick2, kick3, timerStarted, stop;
    int waitForPump;
    double distance;
    int lastEncoder;
    
    CircleTracker tracker = new CircleTracker();
    BetterRobotDrive brDrive = new BetterRobotDrive(1, 2, 3, 4);

    public void robotInit()
    {
//        cam = AxisCamera.getInstance();
//        cam.writeResolution(AxisCamera.ResolutionT.k160x120);
//        cam.writeBrightness(0);

        rollerDirection=true;
        roller=new Jaguar(5);

        stick = new Joystick(1);
   //     compEric = new Compressor(8, 1); // 8 = pressure switch, 1 = relay slot
   //     kickRelay = new Relay(2, Relay.Direction.kForward);

 //       encoder = new Encoder(13, 14);
        wheelToKick = 28+10;
        pump = true;
        distance = 0;
        
        lastEncoder=0;
        timerStarted=false;
        speed = 0;

        kick1 = true;
        kick2 = false;
        kick3 = false;
        stop = false;

        waitForPump = 2;

 //       tracker.doRobotInit(cam, brDrive, stick);
//        encoder.start();
        timerRun=true;
    }

    public void autonomousInit()
    {
        distance = 0;
   //     encoder.reset();
    }
    public void autonomousPeriodic() //BIG BOY CODE  AW YEAH.
    {
        /*
        DriverStationLCD.getInstance().updateLCD();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, "Calculating Pi...");

        //////////////////PUMP CONTROLL/////////////////

        if(pump)
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Pump: ON");
        else
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Pump: OFF");
        if(compEric.getPressureSwitchValue())
        { //if the pressure is too high, or the pump boolean is false
            if(compEric.getPressureSwitchValue())
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Pressure: FULL");
            compEric.stop();
        }
        else if(pump)
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Pressure: NOT-FULL");
            compEric.start();
        }
////        /////////////WAIT FOR PUMP/////////
        Watchdog.getInstance().setEnabled(false);
        if(timerRun){
            //Watchdog.getInstance().setExpiration(5);
            Timer.delay(waitForPump);
            //Watchdog.getInstance().setExpiration(Watchdog.kDefaultWatchdogExpiration);
            timerRun=false;
        }
        ///////////ENCODER AND SPEED AND TIMER//////////////
        if(!stop)
            brDrive.jaguarDrive(-0.6, 0.6, -0.6, 0.6); //Forwards
        //////////Remembering How Far the Robot Has Gone/////////////////

        distance = Math.abs(encoder.getRaw())*(kWheelCircumference/1500)+16;
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "distance: "+Double.toString(distance));
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "clicks: "+Double.toString(encoder.getRaw()));
        //////// KICK SEQUENCE ///////////
        if(distance < 38 && distance > 34) // target: 36"
            kickRelay.set(Relay.Value.kOn); // ON!!!!
        if(distance >=38 && distance<70) // target: 50"
            kickRelay.set(Relay.Value.kOff);
        if(distance < 74 && distance > 70) // target: 72"
            kickRelay.set(Relay.Value.kOn);
        if(distance < 84 && distance > 80) // target: 82"
            kickRelay.set(Relay.Value.kOff);
        if(distance > 106 && distance < 110) // target: 108"
            kickRelay.set(Relay.Value.kOn);
        if(distance > 116 && distance < 120) // target: 118"
            kickRelay.set(Relay.Value.kOff);
        if(distance > 125)
        {// STOP
            brDrive.jaguarDrive(0.0, 0.0, 0.0, 0.0);
            stop = true;
        }
         */
    }

    public void teleopPeriodic()
    {
        //pressure switch value true means that it is over 115psi and needs to stop pumping
        //pressure switch value false meanse that it is under 95psi and needs to pump

        /////////////// COMPRESSOR CONTROL ///////////////////////
        //button 8 turns pump on
        //button 9 turns pump off
        if(stick.getRawButton(8))
            pump=true;
        if(stick.getRawButton(9))
            pump=false;
        if(pump)
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Pump: ON");
        else
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Pump: OFF");

 /*       if(compEric.getPressureSwitchValue() || !pump)
        { //if the pressure is too high, or the pump boolean is false
            if(compEric.getPressureSwitchValue())
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Pressure: FULL");
            compEric.stop();
        }
        else if(pump)
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Pressure: NOT-FULL");
            compEric.start();
        }
*/
        //////////// Fire Piston and Kick Ball ////////////////
        if(stick.getTrigger())
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Piston Extended");
  //          kickRelay.set(Relay.Value.kOn);
        }
        else
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Piston Retracted");
   //         kickRelay.set(Relay.Value.kOff);
        }

        ////////////// DRIVE MODES ///////////////////////////// EVERYTHING IS NOW IN BetterRobotDrive
        if(!stick.getRawButton(2)){
            if(stick.getRawButton(3) && !stick.getRawButton(4) && !stick.getRawButton(5))   //when button 3 is pressed
                brDrive.mecanumDrive(stick);
            else if(!stick.getRawButton(4) && !stick.getRawButton(5))                       //if no buttons are pressed
                brDrive.arcadeDrive(stick);

            else if(stick.getRawButton(4))                                                  //if button 4 is pressed
                brDrive.crabLeft(.5);
            else if(stick.getRawButton(5))                                                  //if button 5 is pressed
                brDrive.crabRight(.5);
        }
   //     tracker.trackCirclePeriodic();

        //////////////ROLLER CONTROL///////////////////

   //     SwitchableDirectionRoller();
        //Roller Direction: true sucks ball, false shoots ball

        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "RSpeed:"+Double.toString(rollerSpeed));

        //////////////CAMERA OPERATIONS////////////////

        
        DriverStationLCD.getInstance().updateLCD();

        ///////// The Roller////////////////////////////
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "RSpeed:"+Double.toString(rollerSpeed));

    }
    public void UnidirectionalRoller(){
        rollerSpeed=(Math.abs(stick.getZ()-0.98))/2;
        roller.set(rollerSpeed);
    }
    public void BidirectionalRoller(){
        rollerSpeed=stick.getZ();
        roller.set(rollerSpeed);
    }
    public void SwitchableDirectionRoller(){
        if(stick.getRawButton(6))
            rollerDirection=false;
        if(stick.getRawButton(7))
            rollerDirection=true;
        if(rollerDirection)
            rollerSpeed=(Math.abs(stick.getZ()-0.98))/2;
        else
            rollerSpeed=-(Math.abs(stick.getZ()-0.98))/2;
        roller.set(rollerSpeed);
    }
}

//Top Line:     Drive mode
//Line 2:       Status of piston and kicking
//Line 3:       What the pump is set to
//Line 4:       If the pressure is full
//Line 5:       Z Axis
//Line 6:       Everything Else

/*
 * ************BUTTON LAYOUT****************
 * 
 * Button|| Function
 * 2     || Find Target
 * 3     || Drive in mecanum mode
 * 4     || Quick crab left
 * 5     || Quick crab right
 * 6     ||
 * 7     ||
 * 8     || Turn pump on
 * 9     || Turn pump off
 * 10    || Virus Alert
 * 11    ||
 */