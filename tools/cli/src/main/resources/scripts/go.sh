#!/bin/bash

workingdir=$1

shift

EXEC="java -classpath .:hdcli.jar:lib/* -Dworkingdir=$workingdir yixin.hd.cli.CLI $@"

echo $EXEC

eval "$EXEC"