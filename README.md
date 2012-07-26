# Generic Framework

This document describes the process of designing a custom framework based on the Generic Framework that has been developed.  The purpose of the Generic Framework is to allow a programmer to quickly and conveniently create a framework style software application that is easily customizable for specific usage.  The Generic Framework provides a simple interface to do just this.  

## Overview

The Generic Framework contains custom graphical widgets, custom UI classes, and a well structured system for creating a framework style application.  The Generic Framework has a strong MVC design pattern that distinctly separates the models behind the user interface with the user interface itself.  The overview of the system will begin with the model behind the user interface.  

### Module Systems and Modules

People can build modules for the Generic Framework.  Modules are .jar files that are loaded into the framework at run-time.  A module system is responsible for loading a set of modules.  For example, the Plugins Module System loads .jar files from a 'plugins' folder in the installation directory.  It also ensures that each of the modules is of type 'Plugin'.  

When the application loads, it first searches the 'system' directory for .jar files.  This is where dependency jars will be placed.  Additionally, Module Systems will be placed here.  Once the framework reads in all the Module Systems, it iterates through them and directs each Module System to loads its specific modules.  

This bootstrapping effect allows for flexibility and logical grouping of modules.

### Project

A project is what the user will view as he works in the Generic Framework.  A project contains a set of settings, along with the �Tools� that will be used within the project.  

### Tools

Tools can be easily written to plug into the Generic Framework.  A tool can be added or removed from a project.  Tools are capable of saving all of the information within them, and reloading them when necessary.  The Tool API allows a third party user to easily create new tools that can integrate with other Tools written by other programmers.  

### Project Container

Project Containers are the way in which the user can view the project.  The Generic Framework has one prewritten view, called the TabbedProjectContainer, which contains tabs for each of the tools in the project.   The Project Container Manager can be used to manage the views, including those created by a programmer who has used the Generic Framework to create their own framework style application.  The main window of the application automatically looks for the current view stored in the Project Container manager, and receives updates from the project when they are fired.  
