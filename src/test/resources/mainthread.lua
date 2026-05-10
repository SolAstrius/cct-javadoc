--- @module mainthread

--[[- Methods marked `mainThread = true` should advertise that they yield.
@source src/test/java/cc/tweaked/javadoc/files/MainThreadModule.java:10
@type mainthread
]]
local mainthread = {}

--[[- Move to the given target. Runs on the server thread.


<aside class="cct-yields" role="note"><span class="cct-yields-label">Yields</span>Yields until the next server tick.</aside>
@source src/test/java/cc/tweaked/javadoc/files/MainThreadModule.java:16
@tparam number target The target value.
]]
function mainthread.setTarget(target) end

--[[- Read the current value. Pure getter, no main-thread dispatch.

@source src/test/java/cc/tweaked/javadoc/files/MainThreadModule.java:25
@treturn number The current value.
]]
function mainthread.getValue() end
