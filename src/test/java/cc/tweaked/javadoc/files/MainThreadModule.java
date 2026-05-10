package cc.tweaked.javadoc.files;

import dan200.computercraft.api.lua.LuaFunction;

/**
 * Methods marked {@code mainThread = true} should advertise that they yield.
 *
 * @cc.module mainthread
 */
public class MainThreadModule {
    /**
     * Move to the given target. Runs on the server thread.
     *
     * @param target The target value.
     */
    @LuaFunction(mainThread = true)
    public final void setTarget(int target) {
    }

    /**
     * Read the current value. Pure getter, no main-thread dispatch.
     *
     * @return The current value.
     */
    @LuaFunction
    public final int getValue() {
        return 0;
    }
}
