#!/bin/bash

FOLDERPATH=$HOME/VDM2ISA

echo "Setting export paths"

export CLASSPATH="/usr/local/lib/ST-4.3.1.jar:$CLASSPATH"

echo "Setting VDMJ exports"

export VDMJ_HOME=$FOLDERPATH/vdmj
export VDMJTK_HOME=$FOLDERPATH/VDM_Toolkit
export VDMJ_VERSION=4.4.4-SNAPSHOT

