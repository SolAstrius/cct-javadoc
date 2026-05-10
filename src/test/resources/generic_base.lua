--- @module generic_base

--[[- 
@source src/test/java/cc/tweaked/javadoc/files/GenericBase.java:25
@type generic_base
]]
local generic_base = {}

--[[- Method on the concrete subclass.

@source src/test/java/cc/tweaked/javadoc/files/GenericBase.java:31
@treturn number Always 1.
]]
function generic_base.getOwn() end

--[[- Inherited getter, declared on the generic base.

@source src/test/java/cc/tweaked/javadoc/files/GenericBase.java:16
@treturn number Always 42.
]]
function generic_base.getInherited() end
