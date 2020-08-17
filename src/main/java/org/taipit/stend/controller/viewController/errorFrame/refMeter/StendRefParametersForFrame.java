package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.model.stend.StendDLLCommands;

public interface StendRefParametersForFrame {
    void addMovingActions();
    void initRefType(StendDLLCommands stendDLLCommands);
    void readParameters() throws InterruptedException;
    void transferParameters(Commands command);
    StendRefParametersForFrame getStendRefParametersForFrame();
}
