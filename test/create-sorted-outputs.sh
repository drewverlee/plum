#!/bin/bash
#This creates csvs that demo the sort options.

echo "creating jar.."
lein uberjar
echo ""

echo "creating output csvs...
     1: sorted by gender (females before males) then last-name ascending
     2: sorted by birth date, ascending
     3: sorted by last name, descending"

echo ""

java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort gender-and-lastname resources/input.csv resources/output-1.csv
java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort date-of-birth resources/input.csv resources/output-2.csv
java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort last-name resources/input.csv resources/output-3.csv

echo "inspecting..."
echo ""

echo "----------------------1------------------------"
cat resources/output-1.csv
echo ""

echo "----------------------2------------------------"
cat resources/output-2.csv
echo ""

echo "----------------------3------------------------"
cat resources/output-3.csv
echo ""

echo "testing with pip seperator"

java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort gender-and-lastname resources/input-pip.csv resources/output-pip.csv

echo "--------------- with pip -----------------------"
cat resources/output-pip.csv

echo ""

echo "testing with space seperator"

java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort gender-and-lastname resources/input-space.csv resources/output-space.csv

echo "--------------- with space -----------------------"
cat resources/output-space.csv

echo ""
echo "----------------------------------------------"
echo "BONUS"

echo "demoing help message produced by running --help"
echo ""

java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort --help

echo "demo error messages.."
echo ""

echo "java -jar target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort foobar" 
echo ""
echo "produces..."
echo "---------------------------------------------------------------------------"
java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort foobar
echo "---------------------------------------------------------------------------"

echo "java -jar target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort lastname resources/input.csv idontexistfile"
echo ""
echo "produces..."
echo "---------------------------------------------------------------------------"
java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort lastname resources/input.csv idontexistfile
echo "---------------------------------------------------------------------------"
echo ""
echo "with seperator"

echo "java -jar target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort last-name resources/input.csv resources/output-extra.csv --separator X"
echo ""

echo "---------------------------------------------------------------------------"
java -jar ../target/uberjar/plum-0.1.0-SNAPSHOT-standalone.jar sort last-name resources/input.csv resources/output-extra.csv --separator X
echo "---------------------------------------------------------------------------"
echo ""
echo "produces..."
echo "--------------- with X separator -----------------------"
cat resources/output-extra.csv

echo ""
echo "DONE. Thanks for running the demo!"
