#!/bin/bash

# Make sure only root can run our script
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

# Make sure vdmj & vdmj_build don't already exist

rm -f /usr/local/bin/vdmj
rm -f /usr/local/bin/vdmj_build

ln -s $VDMJTK_HOME/scripts/vdmj.sh /usr/local/bin/vdmj
ln -s $VDMJTK_HOME/scripts/vdmj_build.sh /usr/local/bin/vdmj_build
