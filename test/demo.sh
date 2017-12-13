#!/bin/bash
#This script launches the server and calls the endpoints with data.
#Assumes you have lein, curl and probably a number of other things about your computer.

echo "cleaning up files and creating creating uberjar..."
lein do clean, ring uberjar
echo "done"
echo ""

echo "starting server on http://localhost:3000"
java -jar ../target/uberjar/plum-server.jar &
echo "You should see some server logs possible intertwined with these messages"
echo ""

# we want to save the pid so we can kill it 
SERVER_PID=$!

echo "sleep for a bit so server can start..."
sleep 25
echo "done sleeping"

echo "You know have a choice.\n 
You can use your browser to surf the API or continue this script which will make some calls to the api for you.\n
I recommend the browser, you can enter in what you want. \n\n
WARNING: bad inputs are NOT handled gracefully.
As a design choice I delayed handling as they weren't mentioned. Normally I use the validation to produce an error message and
send it back to the client. This actually happens for the GET request by default as a clojure spec"

echo -n "Continue and make API Calls (y) Browser API and press n to kill server when done (n) "
read answer

# YES
if echo "$answer" | grep -iq "^y" ;then
echo ""
echo "great lets make some API Calls"
sleep 1
echo "Adding some people..."

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "person": "smith,john,male,blue,04/07/1950" 
 }' 'http://localhost:3000/persons'

echo ""
echo ""

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "person": "xaviar,john,female,blue,04/07/1950" 
 }' 'http://localhost:3000/persons'


echo ""
echo ""

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "person": "xaviar,john,male,blue,04/07/1951" 
 }' 'http://localhost:3000/persons'

 sleep 10
 echo ""
 echo "done adding people.."
 echo ""

 echo "retrieving them sorted by last name descending (z..a) so we expect our xaviar to be first in the list."
 echo "-----------------------------------------------------------------------"
 echo ""

curl -X GET --header 'Accept: application/json' 'http://localhost:3000/persons/last-name'
sleep 5

 echo ""
echo "great it looks like he is."

 echo ""
 echo "sort by Females > Males then by last name ascending (a...z)"
 echo "-----------------------------------------------------------------------"
 echo ""

curl -X GET --header 'Accept: application/json' 'http://localhost:3000/persons/gender-and-lastname'
sleep 5


 echo ""
echo "looks good"

 echo ""
 echo "sort by birthdate descending (newer...older)"
 echo "-----------------------------------------------------------------------"
 echo ""

curl -X GET --header 'Accept: application/json' 'http://localhost:3000/persons/date-of-birth'
sleep 5

 echo ""
echo "looks good"

 #NO 
else
    echo "I hope your enjoyed browsing the API"
fi

echo ""
echo "Thanks for watching the demo."
kill $SERVER_PID
