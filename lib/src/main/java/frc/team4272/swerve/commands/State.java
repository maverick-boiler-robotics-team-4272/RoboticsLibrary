package frc.team4272.swerve.commands;

import java.util.Set;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public abstract class State<T extends Subsystem> implements Sendable, Command {
    protected final T requiredSubsystem;

    protected State(T subsystem) {
        String name = getClass().getName();
        SendableRegistry.add(this, name.substring(name.lastIndexOf('.') + 1));
        requiredSubsystem = subsystem;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.of(requiredSubsystem);
    }

    @Override
    public String getName() {
        return SendableRegistry.getName(this);
    }

    /**
     * Sets the name of this Command.
     *
     * @param name name
     */
    public void setName(String name) {
        SendableRegistry.setName(this, name);
    }

    /**
     * Decorates this Command with a name. Is an inline function for
     * #setName(String);
     *
     * @param name name
     * @return the decorated Command
     */
    public State<T> withName(String name) {
        this.setName(name);
        return this;
    }

    /**
     * Gets the subsystem name of this Command.
     *
     * @return Subsystem name
     */
    public String getSubsystem() {
        return SendableRegistry.getSubsystem(this);
    }

    /**
     * Sets the subsystem name of this Command.
     *
     * @param subsystem subsystem name
     */
    public void setSubsystem(String subsystem) {
        SendableRegistry.setSubsystem(this, subsystem);
    }

    /**
     * Initializes this sendable. Useful for allowing implementations to easily
     * extend SendableBase.
     *
     * @param builder the builder used to construct this sendable
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Command");
        builder.addStringProperty(".name", this::getName, null);
        builder.addBooleanProperty(
                "running",
                this::isScheduled,
                value -> {
                    if (value) {
                        if (!isScheduled()) {
                            schedule();
                        }
                    } else {
                        if (isScheduled()) {
                            cancel();
                        }
                    }
                });

        // Commented out due to the fact that CommandGroupBase.getGroupedCommands is not visible
        // builder.addBooleanProperty(
        //         ".isParented", () -> CommandGroupBase.getGroupedCommands().contains(this), null);
    }
}