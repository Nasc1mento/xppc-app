#!/bin/sh

export PATH=/app/jre/bin:/usr/bin
java -Djava.awt.headless=false -jar /app/bin/xppc-1.0.0-all.jar "$@"