package cc.tweaked.javadoc.files;

import dan200.computercraft.api.lua.LuaFunction;

/// A basic module
///
/// @cc.module basic_md
public class BasicModuleMarkdown {
    /// Add two numbers together.
    ///
    /// This might be useful if you need to add two numbers and want to avoid
    /// depending on jQuery.
    ///
    /// One more paragraph.
    ///
    ///  - We just want to check that we desugar lists into other lists. This ensures that one can correctly use
    ///    markdown features (otherwise they're nested within HTML, which stinks).
    ///  - And
    ///
    ///    another entry.
    ///
    /// <customTag attribute="value"></customTag>
    /// <custom-tag attribute="value"></custom-tag>
    ///
    /// [#add] and [add two numbers][#add] are the same method.
    ///
    /// @param x The first number to add
    /// @param y The second number to add
    /// @return The added values
    /// @cc.usage Do something simple.
    ///
    /// ```lua
    /// print("Hello!")print("World")
    /// ```
    ///
    /// @cc.usage Another example
    /// ```lua {attribute="value"}
    /// print("Test")
    /// ```
    ///
    /// @since 1.2.3
    @LuaFunction
    public int add(int x, int y) {
        return x + y;
    }
}
