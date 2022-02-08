Changes from initial proposal:
- The player is able to move left/right and jump
- The "boing" control is now called ground pound.
    - It launches the player up in a fixed time after landing
        (instead of the player being able to hold down the key
         to control how long they charge it up for).
        This is because holding up already allows the player
        to control their jump height.


Game hints:
- Starting a ground pound boosts the player slightly upwards
- Starting a ground pound resets velocity
- The jump at the end of a ground pound can be cancelled by being in the air while it's supposed to happen
- Wall jumping is possible by jumping right after switching gravity while on ground
  (though I haven't created any levels that require this
   (this has potential use on level 3))


Known bugs:


Once in a while (pretty rarely), this error pops up (the line numbers probably don't match up anymore):

Exception in thread "main" java.lang.IllegalStateException: Buffers have not been created
	at java.desktop/sun.awt.X11.XComponentPeer.getBackBuffer(XComponentPeer.java:1143)
	at java.desktop/java.awt.Component$FlipBufferStrategy.getBackBuffer(Component.java:4152)
	at java.desktop/java.awt.Component$FlipBufferStrategy.updateInternalBuffers(Component.java:4137)
	at java.desktop/java.awt.Component$FlipBufferStrategy.createBuffers(Component.java:4128)
	at java.desktop/java.awt.Component$FlipBufferStrategy.revalidate(Component.java:4249)
	at java.desktop/java.awt.Component$FlipBufferStrategy.getDrawGraphics(Component.java:4235)
	at potatoritos.blobboing.Window.<init>(Window.java:57)
	at potatoritos.blobboing.Game.<init>(Game.java:40)
	at potatoritos.blobboing.Main.main(Main.java:105)

It seems to happen randomly.


This error also occurs randomly:

Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException: Cannot invoke "java.awt.image.VolatileImage.contentsLost()" because "this.backBuffers[...]" is null
	at java.desktop/java.awt.Component$BltBufferStrategy.contentsLost(Component.java:4577)
	at java.desktop/javax.swing.BufferStrategyPaintManager.flushAccumulatedRegion(BufferStrategyPaintManager.java:369)
	at java.desktop/javax.swing.BufferStrategyPaintManager.endPaint(BufferStrategyPaintManager.java:333)
	at java.desktop/javax.swing.RepaintManager.endPaint(RepaintManager.java:1428)
	at java.desktop/javax.swing.JComponent.paint(JComponent.java:1108)
	at java.desktop/java.awt.GraphicsCallback$PaintCallback.run(GraphicsCallback.java:39)
	at java.desktop/sun.awt.SunGraphicsCallback.runOneComponent(SunGraphicsCallback.java:75)
	at java.desktop/sun.awt.SunGraphicsCallback.runComponents(SunGraphicsCallback.java:112)
	at java.desktop/java.awt.Container.paint(Container.java:2005)
	at java.desktop/java.awt.Window.paint(Window.java:3959)
	at java.desktop/javax.swing.RepaintManager$4.run(RepaintManager.java:890)
	at java.desktop/javax.swing.RepaintManager$4.run(RepaintManager.java:862)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:399)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:86)
	at java.desktop/javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:862)
	at java.desktop/javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:835)
	at java.desktop/javax.swing.RepaintManager.prePaintDirtyRegions(RepaintManager.java:784)
	at java.desktop/javax.swing.RepaintManager$ProcessingRunnable.run(RepaintManager.java:1898)
	at java.desktop/java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:318)
	at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:771)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:722)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:716)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:399)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:86)
	at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:741)
	at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:124)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:113)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:109)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:90)

but it doesn't seem to affect the running of my program.