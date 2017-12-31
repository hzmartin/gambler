#!/bin/bash

EXEC="java -classpath .:cli.jar:lib/* gambler.tools.cli.CLI $@"

echo $EXEC

eval "$EXEC"