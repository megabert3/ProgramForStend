package stend.controller.Commands;

public interface Commands{
    void execute();
    String getName();
    boolean isActive();
    void setActive(boolean active);
}
