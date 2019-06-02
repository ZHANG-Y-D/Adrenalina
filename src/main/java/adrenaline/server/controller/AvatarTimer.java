package adrenaline.server.controller;

import adrenaline.server.controller.states.AvatarSelectionState;

public class AvatarTimer implements Runnable{

    private AvatarSelectionState avatarSelectionState;

    public AvatarTimer(AvatarSelectionState avatarSelectionState){
        this.avatarSelectionState = avatarSelectionState;
    }

    @Override
    public void run() {
        avatarSelectionState.selectRandom();
    }
}
