#!/usr/bin/env bash
# run the twitter connector
connect-standalone connect-standalone.properties twitter.properties
# OR (linux / mac OSX)
connect-standalone.sh connect-standalone.properties twitter.properties
# OR (Windows)
connect-standalone.bat connect-standalone.properties twitter.properties