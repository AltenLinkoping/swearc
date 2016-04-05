#!/bin/sh

echo "Building and deploying"
echo "================================="
echo ""


../../gradlew installDist

echo ""
echo ""
echo "Starting web server"
echo "================================="
echo ""


build/install/webserver/bin/webserver
