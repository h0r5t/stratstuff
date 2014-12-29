stratstuff
==========
Dwarf Fortress Java Clone... xD

Currently implemented debug_commands:

| Command | Description |
| ------------- | ------------- |
| spawn 0 x y z  | Spawns worker unit at x y z |
| chg groundID x y z | Changes the ground ID at x y z |
| ls | lists all units living |
| set $var value  | sets $var to value |
| get $var  | prints the value of $var |
| build elementID x y z | Builds element at x y z |
| destroy elementID x y z | Destroys element at x y z |
| move unitID x y z | creates a unit move task for that unit to x y z |

==========

Scripts have to be placed in /resources/scripts/
default.script will be executed automatically after world creation.

==========

Library used for pathfinding:
https://code.google.com/p/path-finder-library/


