# Armour
Java programming test

Armour Java Programming Test
----------------------------
I am not sure this is exactly what you asked for.
Due to not having winzip I cannot use mvn as mvn uses unzip to deploy webservices.
I used to use glassfish4 until winzip was removed from windows10.
I tried to use dropWizard but that requires mvn to build and run.

The method I used does not lend itself to JSON payloads unfortunately and runs from Internet 
Explorer.

Source
--------
runIndex.java  - Program to run from command prompt which uses port 8080
login.html     - Front end screen which connects to port 8080 via Internet Explorer
davefish1.xml  - Contains paths and user info. This includes name, userid and password.

All sources need to reside in a sub-folder Armour.

to compile java - C:\JobTests\Armour>javac -cp ../ runIndex.java

How to Run
--------------

The backend is executed from the command prompt with default set to Armour, as follows :-

C:\JobTests\Armour>java -cp ../ Armour.runIndex

Once the backend is started start internet explorer and execute the following command :-
	http://localhost:8080/login

This should display the login screen.


