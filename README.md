# AADL 2 RTSJ
---
## Summary
A transformation of detailed AADL (Architecture Analysis and Design Language) software models to RTSJ (Real-Time Specification for Java) code. 

---
## DEMO
The autopilot project we presented at <a href="http://2016.dasconline.org/">DASC 2016</a> is contained in the sources as *autopilot_impl_Instance-rtsj* project. A video of this project and our setup can be seen [here](https://www.researchgate.net/project/MBE-for-Autonomous-Vehicles-with-Real-Time-Java-and-AADL)

---
## Setup
The overall setup of our machine is depicted in the figure below.

![alt text](https://github.com/Sandared/aadl2rtsj/blob/paper/SetupDASC16.png "Setup")

In order to achieve this setup you have to do the following:

1. Install [X-Plane](http://www.x-plane.com/). You can use the demo version which allows 10 minutes of flight for each start.
  1. Within your X-Plane installation directory go to *Aircraft* folder and create a new subfolder *Quadrocopter*
  2. Copy the file *QRO_X_no_differential_thrust.acf* into this folder.
2. Install [Virtual Box (VB)] (https://www.virtualbox.org/)
3. In VB: 
  1. Create a new machine with a 64-bit Linux of your choice (we use [Ubuntu MATE](https://ubuntu-mate.org/))
  2. Go to File -> Settings -> Network -> Host-only Networks -> Add new Adapter -> Remember newly created adapter's *name* -> click on Screwdriver -> Remember *IPv4 Host Address* and *Netmask* -> OK -> OK (You need the remebered values in step 4.4)
4. In Linux:
  1. Install [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) in order to run Eclipse
  2. Install [Eclipse Neon Modeling Tools](http://www.eclipse.org/downloads/eclipse-packages/)
  3. Install the Personal Edition of [JamaicaVM](https://www.aicas.com/cms/en/jamaicavm-pe)
  4. Connections -> Edit Connections -> Add Connection: Ethernet -> Ethernet -> Device MAC address: the one that's not used by our standard connection -> IPv4 Settings -> Method: manual -> Addresses -> Add -> Address: *IPv4 Host Address* but choose a random new number for the last segment, Netmask: *Netmask* -> Remember this new address as *IPv4 Guest Address* -> Save
5. Start X-Plane and:
  1. In Quickstart select LOWI (Innsbruck Kranebitten) as Airport and the newly created Quadrocopter as Airplane. Weather: clear -> Fly!
  2. Go to Settings -> Data in- and output -> check Internet and Cockpitdisplay in the upper right corner -> check 1st and 4th checkbox of *pitch, roll, headings* / *lat, lon, altitude* / *throttle command* -> set UDP transfer rate to 20.0/s inthe lower right -> close
  3. Go to Settings -> Network Connections -> Data -> For IP Address For data ouput insert the remembered *IPv4 Guest Address*, port is 49003 -> *check IP of data receiver* -> *UDP Ports* are from top to bottom: 49000, 49001, 49002 
  4. Now X-Plane should be able to communicate with our autopilot. If not there might be some issues with your firewall.
6. In Linux:
  1. Start Eclipse
  2. Check out the projects *de.uniaugsburg.smds.aadl2rtsj.generation.util* and *de.uniaugsburg.smds.aadl2rtsj.generation* into your workspace
  3. Get the sources of OSATE2 as described [here](https://wiki.sei.cmu.edu/aadl/index.php/Getting_Osate_2_sources)
  4. Start a new instance of OSATE2 as described in the section [Running Osate 2](https://wiki.sei.cmu.edu/aadl/index.php/Getting_Osate_2_sources#Running_Osate_2)
  5. In your OSATE2 instance 
    1. Install the [JamaicaVM Plugin for Eclipse](https://www.aicas.com/cms/en/eclipse-plugin)
    2. Check out the projects *de.uniaugsburg.smds.aadl2rtsj.generation.tests* and *autopilot_impl_Instance-rtsj*
    
---
##Example

Currently our approach only supports automted addition of components, but not removal. So the easiest way to test the setup is to create something like a new thread within the autopilot process, connect it via a port connection and some data ports to other threads and fill out the generated *"newThread"UserCode* class with your own implementation.








