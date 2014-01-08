Gooru Web 0.5.11.0
=============
Introducing Gooru Web 0.5.11.0 - a solution to help teachers faciliate personalized learning for students.

Introduction
==============
Gooru Web is the front-end of the Gooru application. Gooru’s free solution enables teachers to create, curate, and share collections of web resources on any K-12 topic. With millions of multimedia resources and quiz questions, Gooru makes it easy to discover topic-relevant and standards-aligned content to address specific students’ needs.  At Gooru, we believe education is a human right.  Now, with access to this Git repository, open-sourced under the MIT license, you can build along side us to support this mission and help students around the world reach their full potential.

Gooru Web is developed using GWTP model-view-presenter framework to simplify the GWT implementation, followed by advanced Restlet client which will communicate with the high performance Gooru Restful Services. 

Features:
==============
Gooru’s features fall into four major groups: Discover, Organize, Teach and Study.  Here are short descriptions of each component. 

Discover
==============
Discover services provide the solution to search millions of resources, collections, and question items in the Gooru Learning Catalog. The Search APIs with the indexing support allow for filtering content or the Suggest APIs to personalize the experience by taking user activity and preferences into account.

Organize
==============
Organize services provide the solution for the registered users to create collections (multimedia lessons) which include resources such as videos, textbooks, interactives and question items. The Organize services allow you to access content and perform CRUD operations for collections, resources and question items.

Teach
==============
Teach services provide the solution to manage classes, create assignments, and track collection sessions. Teachers can create multiple Classpages, which are unique pages for different student groups accessible through URL or a “Class Code”. Each Classpage can have a set of assignments, with instructions and a date.

Library
==============
The Gooru Library is a brand new way to discover the best collections that the Gooru community has curated across core subject areas.

Authentication
==============
Create new accounts on Gooru using the authentication system or Gmail connect.

Social
==============
Share, flag and tag content, access a flexible rating system and protect your users with abuse reporting using Gooru Social services.

Resource Player
==============
This module is responsible for rendering Gooru Resources. Resources can be labeled with one of the following nine categories: Video, Website, Interactive, Question, Slide, Textbook, Handout, Lesson, Exam. The Resource Player is capable of rendering all of them. This player also provides functionality for sharing a Resource via Facebook, Twitter or Email.

Collection Player
==============
The Gooru Collection Player is capable of rendering all types of Gooru Collections including Quizzes, and other collections with Questions. The player module contains collection navigation, narration, summary, sharing out, and adding the resource/collection to the user’s Organize page.

Environment setup and build process
==============
Eclipse IDE is used for the development of Gooru Web 0.5.11.0. Each developer machine should have the below configuration at minimum level.
JDK: 1.6 or above 
Memory: 4 GB RAM 
Disk Space: 20 GB
Operating System: Windows 7 and above or Ubuntu
GWT SDK: 2.4 (stable) 
Application container: Apache tomcat7
Apache Maven: Maven 3.0.4
GWT Maven plugin setup instructions: http://mojo.codehaus.org/gwt-maven-plugin/
GWT environment setup in Windows: http://www.youtube.com/watch?v=MKuLDRo8fpM or https://developers.google.com/eclipse/docs/install-eclipse-4.2
GWT environment setup in Ubuntu: http://www.youtube.com/watch?v=bkKxqynHrrQ
 
Build Process
==============
Process 1
1.	Navigate to the development project folder
	For example, cd Home\Projects\ROOT
2.	From the linux terminal Clean install the build
	Command: mvn clean install
3.	Run the project
	Command: mvn gwt:run
Process 2
Right-click on the development project in Eclipse IDE and run as "Maven build". Enter "gwt:run" in the prompted run window.Introducing Gooru Web 0.5.11.0 - a solution to help teachers faciliate personalized learning for students. 

