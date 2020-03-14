#include "WheelSpinnies.h"

WheelSpinnies::WheelSpinnies(){
}

void WheelSpinnies::init(){
 spinnymotorLeft.RestoreFactoryDefaults();
    
    // set PID coefficients
    spinnypidleft.SetP(kP);
    spinnypidleft.SetI(kI);
    spinnypidleft.SetD(kD);
    spinnypidleft.SetIZone(kIz);
    spinnypidleft.SetFF(kFF);
    spinnypidleft.SetOutputRange(kMinOutput, kMaxOutput);

 spinnymotorRight.RestoreFactoryDefaults();
    
    // set PID coefficients
    spinnypidright.SetP(kP);
    spinnypidright.SetI(kI);
    spinnypidright.SetD(kD);
    spinnypidright.SetIZone(kIz);
    spinnypidright.SetFF(kFF);
    spinnypidright.SetOutputRange(kMinOutput, kMaxOutput);
}

void WheelSpinnies::spinrev(bool up1, bool down1, bool up2, bool down2, bool revUp){
    if (up1 && up1pressed == false){
        up1pressed = true;
        velocity1 += 50;
    }
    else if (up1 == false && up1pressed){
        up1pressed = false;
    }

    if (down1 && down1pressed == false){
        down1pressed = true;
        velocity1 -= 50;
    }
    else if (down1 == false && down1pressed){
        down1pressed = false;
    }

    if (up2 && up2pressed == false){
        up2pressed = true;
        velocity2 += 50;
    }
    else if (up2 == false && up2pressed){
        up2pressed = false;
    }

    if (down2 && down2pressed == false){
        down2pressed = true;
        velocity2 -= 50;
    }
    else if (down2 == false && down2pressed){
        down2pressed = false;
    }

    if (revUp){
        spinnypidright.SetReference(velocity1, rev::ControlType::kVelocity);
        spinnypidleft.SetReference(-velocity2, rev::ControlType::kVelocity);
        feeder.Set(ControlMode::PercentOutput, -.7);
    }
    else{
        spinnymotorRight.Set(0);
        spinnymotorLeft.Set(0);
        feeder.Set(ControlMode::PercentOutput, 0);
    }

    if (++_loops >= 20) {
		_loops = 0;
        _sb.append("\tV1:");
	    _sb.append(std::to_string(velocity1));
	    _sb.append("\tspd1:");
	    _sb.append(std::to_string(spinnyencoderright.GetVelocity()));

        _sb.append("\tV2:");
	    _sb.append(std::to_string(velocity2));
	    _sb.append("\tspd2:");
	    _sb.append(std::to_string(spinnyencoderleft.GetVelocity()));

		printf("%s\n",_sb.c_str());
        _sb.clear();
	}
}
    