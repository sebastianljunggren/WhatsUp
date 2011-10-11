#!/bin/bash
#
# Requires git, ant and that the tools and platform-tools 
# folder in the Android SDK is on your path.
#
# Retrieve the latest version from your repository﻿
#
git clone git://github.com/sebastianljunggren/WhatsUp.git
#
# Generate build.xml
#
cd WhatsUp/
android update project -p .
android update test-project -m ../ -p WhatsUpTest/
#
# Compile, sign with debug key install to emulator/device and
# run tests.
#
cd WhatsUpTest
ant run-tests
﻿
