package com.comp2042;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    //hard drop returns DownData as well
    DownData onHardDrop(MoveEvent event);

    void createNewGame();
}
