#!/bin/bash
cd "$(dirname "$0")"
cd ..
java -jar Application\ Builder.jar "Properties/data/BackupDatabase.xml"
