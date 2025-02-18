#!/bin/bash

# * Calls latest VDMJ build (see vdmj_build.sh) with corresponding annotations and parameters
# * Raw-call version (of vdmj.sh) without rlwrap - useful as launch configuration within IDEs

if [ -z "$VDMJ_VERSION" ]
then
	# should be VDMJ_VERSION=X.Y.Z-SNAPSHOT
    echo "Variable VDMJ_VERSION not set; should be X.Y.Z-SNAPSHOT"
    exit 1
fi

BASE=`dirname $0`
VERSION=$VDMJ_VERSION
NAME=vdmj

JAR=/usr/local/lib/${NAME}-${VERSION}.jar
ANNOTATIONS=/usr/local/lib/annotations-${VERSION}.jar:/usr/local/lib/annotations2-${VERSION}.jar:/usr/local/lib/witness-1.0.0.jar:/usr/local/lib/annotations3-${VERSION}.jar

export CLASSPATH="$CLASSPATH:${ANNOTATIONS}"

if [ ! -f ${JAR} ]; then
    echo -e "Executable jar: \n\n${JAR}\n\ndoes not exist.  It must be in /usr/local/lib."
    exit -1;
fi

#java -cp ${ANNOTATIONS} -jar -Dmax.errors=1000 ${JAR} $@
java -cp ${ANNOTATIONS}:${JAR} -Dmax.errors=1000 com.fujitsu.vdmj.VDMJ -vdmsl -strict $@
