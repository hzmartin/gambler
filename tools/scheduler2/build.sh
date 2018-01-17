#!/bin/bash

workdir=`pwd`
reldir=~/apps/quartzrel
echo "start packaging ..."
echo "working dir: $workdir"
echo "release dir: $reldir"

rm -rf $reldir
mkdir $reldir

mvn clean package -DskipTests

cd $reldir

cp -r $workdir/target/classes/* $reldir
cp -r $workdir/target/lib $reldir/
cp -r $workdir/go.sh $reldir

