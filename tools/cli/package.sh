#!/bin/bash

rel_path=~/cli
rm -rf $rel_path
mkdir $rel_path
mvn clean package -DskipTests

cp scripts/go.sh $rel_path
chmod +x $rel_path/go.sh
cp target/classes/*.xml $rel_path
cp target/classes/datasource.properties $rel_path
cp target/cli.jar $rel_path
cp -R target/lib $rel_path

