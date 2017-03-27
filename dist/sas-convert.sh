#!/bin/bash

here=$(realpath $(dirname "${0}"))
version=$(cat "${here}/VERSION")
jar="${here}/sas-convert-${version}.jar"

# Check if the jar present
if [ ! -e "${jar}" ]; then
    echo "Cannot find jar: ${jar}"
    exit 1
fi

# Choose the location of the java binary
prefix=
if [ ! "x${JAVA_HOME}" == "x" ]; then
    prefix="${JAVA_HOME}/bin/"
fi

# Run the jar
${prefix}java -jar "${jar}" $@

