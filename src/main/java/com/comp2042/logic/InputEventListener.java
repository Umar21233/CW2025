package com.comp2042.logic;

import com.comp2042.model.DownData;
import com.comp2042.model.MoveEvent;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    //hard drop returns DownData as well
    DownData onHardDrop(MoveEvent event);

    void createNewGame();

    void onGameExit();
}
