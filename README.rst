========
ATMCraft
========

This is a Bukkit plugin for Minecraft;   See http://bukkit.org/.

Building
========

Decent tutorial on build environments for Bukkit / Maven:

    http://wiki.bukkit.org/User:Sagacious_Zed/Maven_Plugin_Tutorial

To build:

  mvn package

To install, copy target/AtmCraft.jar to the plugins/ directory where CraftBukkit is running.

Using the correct Bukkit Version
--------------------------------
Look at the verison of the "bukkit" artifact ID in pom.xml to see the
specific version of Bukkit this is built against, the CraftBukkit
version in use should match.   For example,
"<version>1.7.2-R0.2</version>".   To use a new Bukkit version,
upgrade this version number.  Maven should know to go out to the
repository listed in <repositories> and get the new .jar file, which
will then be deposited as an "artifact", which on my system is at
"~/.m2/repository/org/bukkit/bukkit/".

Server
======

The plugin runs within the CraftBukkit server, but illustrates calling out to
*another* server which is written in Python.  That server is the "ATMCraft"
server, available at:

    https://bitbucket.org/zzzeek/pycon2014_atmcraft/


