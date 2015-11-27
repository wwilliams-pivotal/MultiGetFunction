#!/bin/bash

echo "Stopping locator and server..."
gfsh <<!

connect --locator=localhost[10334]
shutdown --include-locators=true
Y

exit;
!
