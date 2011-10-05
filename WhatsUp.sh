#!/bin/bash

#
# Retrieve the latest version from your repository﻿
#
git clone git://github.com/sebastianljunggren/WhatsUp.git
#
# Generate build.xml
#
cd WhatsUp/
android update project -p .
#
# Compile, sign with debug key and install to emulator/device.
#
ant install
﻿
