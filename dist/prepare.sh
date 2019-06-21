#!/usr/bin/env bash

here=$(realpath $(dirname "${0}"))

pushd "${here}"

version=$(cat ../VERSION)
jar="sas-convert-${version}.jar"
zip="sas-convert-${version}.zip"

# build
echo "Building sas-convert ..."
pushd ..
{ mvn clean && mvn package; } ||
{ echo "Build failed."; exit 1; }
popd

# copy files; create zip archive
echo "Creating ${zip} ..."
if [[ -d "sas-convert" ]]; then
    rm -r sas-convert
fi
mkdir sas-convert &&
cp sas-convert.sh sas-convert/sas-convert &&
cp ../VERSION sas-convert/VERSION &&
cp ../target/${jar} sas-convert/${jar} &&
zip -r "${zip}" sas-convert/ &&
echo "Done."

popd

