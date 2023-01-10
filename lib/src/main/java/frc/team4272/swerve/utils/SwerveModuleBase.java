package frc.team4272.swerve.utils;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.team4272.globals.MathUtils;

public abstract class SwerveModuleBase {
    public abstract SwerveModuleState getState();

    public abstract void goToState(SwerveModuleState state);

    public static SwerveModuleState optimize(SwerveModuleState desiredState, Rotation2d currentHeading) {
        double inverted = 1.0;

        double desiredDegrees = MathUtils.euclideanModulo(desiredState.angle.getDegrees(), 360.0);
        
        double currentDegrees = currentHeading.getDegrees();
        double currentMod = MathUtils.euclideanModulo(currentDegrees, 360.0);

        double deltaAngle = Math.abs(desiredDegrees - currentMod);

        if(deltaAngle > 90.0 && deltaAngle <= 270.0) {
            inverted = -1.0;
            desiredDegrees -= 180.0;
        }

        deltaAngle = MathUtils.euclideanModulo(desiredDegrees - currentMod, 360.0);

        double counterClock = deltaAngle;
        double clock = deltaAngle - 360.0;

        if(Math.abs(counterClock) < Math.abs(clock)) {
            desiredDegrees = counterClock;
        } else {
            desiredDegrees = clock;
        }

        desiredDegrees += currentDegrees;

        return new SwerveModuleState(desiredState.speedMetersPerSecond * inverted, Rotation2d.fromDegrees(desiredDegrees));
    }

    public static class PositionedSwerveModule {
        private SwerveModuleBase module;
        private Translation2d position;

        public PositionedSwerveModule(SwerveModuleBase module, Translation2d position) {
            this.module = module;
            this.position = position;
        }

        public SwerveModuleBase getModule() {
            return module;
        }

        public Translation2d getPosition() {
            return position;
        }
    }
}