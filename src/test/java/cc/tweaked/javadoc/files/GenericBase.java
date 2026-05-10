package cc.tweaked.javadoc.files;

import dan200.computercraft.api.lua.LuaFunction;

public class GenericBase {
    /**
     * Abstract base with a free type parameter. Its @LuaFunctions should
     * still attach to concrete @cc.module subclasses below.
     */
    public static abstract class Base<T> {
        /**
         * Inherited getter, declared on the generic base.
         *
         * @return Always 42.
         */
        @LuaFunction
        public final int getInherited() {
            return 42;
        }
    }

    /**
     * @cc.module generic_base
     */
    public static class Concrete extends Base<String> {
        /**
         * Method on the concrete subclass.
         *
         * @return Always 1.
         */
        @LuaFunction
        public final int getOwn() {
            return 1;
        }
    }
}
