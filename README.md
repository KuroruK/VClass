===================================
# Virtual Classroom (VClass)
===================================

By : Mubeen Khan    - i170071, Ahmed Abdullah - i170120, Mustafa Shoaib - i170204

<dl>
  VClass is an android application that provides the features of a classroom from 
  the comfort of people’s homes. Using virtual reality technology, a more immersive and
  interactive experience can be had relative to the typical online video conferencing or 
  classroom software.
</dl>

Start
==========
Open the project VClass in Android Studio and install to device

<dl>
  The application uses firebasee as an online database to handle thus to use run the application
  you will first need to set the firebase URL in the following classes to one that u can run.
  <dd></dd>
   <dd> 
     Classes : LiveStudentClassActivity, DBHelper, BoardListActivity
   </dd>
   <dt>
      replace        FIREBASE_URL = "_insert url here_"
   </dt><dd></dd>
  
  Note: For multiple devices to use, the local SQL databse of the initial device will need to be shared,
        but it will run  on a single device.
     
   Currently to store credentials and other information, we are using a local SQL database.
   This is being loaded with .csv files currenly that ar using a specific format. Thus loading functions
   for timetable will not work without them present (currently in assets folder as we are using the emulator)
   but the values can be manually inserted for running as well. 
   the local database is at "data/com.example.vclasslogin/databases/VClass_Users.db" in phone
   currently some values for testing are initialisd at line 47-50 in LoginActivity.java, that stets up some accounts
   such as admin, student, and teacher.
   
   Default Admin's account is:
   <dt>
      username = "admin"
   </dt><dt>
      password = "pass"
    </dt>
    <dd></dd>
  This account can be used to set some initial accounts for students and teachers for testing
  
  Some student accounts are 
  <dt>
      username = "student1", "student2", "student3", "student4"
   </dt><dt>
      password = "1234" for all
    </dt>
    <dd></dd>
  some teacher accounts are 
  <dt>
      username = "teacher1", "teacher2", "teacher3", "teacher4"
   </dt><dt>
      password = "1234" for all
    </dt>
     
</dl>

Current Implementation
==========
We are currently using 2 databases, a local SQL database, that
will be moved to the firebase later on, and firebase to manage the online functions
and information for the whiteboard and chat-room. The SQL database is used to store
data such as the credentials, timetable slots, courses information, and other information
related components.

For the whiteboard, we are using a firebase-based implementation, where each
course has a unique whiteboard link generated. the links and their corresponding
information, such as lines, are stored on firebase and constantly synced with other
endpoints that have the same whiteboard opened. For example, the whiteboard for the
course Programming Fundamentals (PF) is opened by teacher1 and student1, the teacher
writes something on the board, the inputs are quickly uploaded to the database and all
other instances of the current whiteboard, in this case, student1, will also have their
views updated simultaneously.

For the chat-room, we are again using a firebase-based implementation where
the messages are uploaded and retrieved by the firebase handler. to set it up for a group
chat, messages sent by different people have the course name as a receiver, where the
course name refers to the group chat those messages belong to.



=====================================================================================
# Unity Component
The unity components for teacher and students are built as separate unity projects (later made 
into different scenes). Photon is used for the networking between different users.

Teacher side<dd></dd>
This is a 3d unity project used to show a 2d user interface. The teacher side has 3 key 
functionalities; shared whiteboard(editable by teacher), shared document(changed and navigated 
by teacher) and voice functionalities.

Student side<dd></dd>
This is a full 3d environment that can be viewed in either a 3d video mode or a VR mode by
using google cardboard. The students can use this to view the shared whiteboard, shared document
and voice chat with the rest of the class and teacher when so desired.

Photon networking<dd></dd>
We photon for unity for the networking and voice aspect of our project. Photon voice is used for the 
voice call functionality for each room and photon pun is used to trigger events so as all the students 
who are in a session will be shown the same changes to their respective sides.

=====================================================================================
# User Manual
The administrator has numerous functions he can do.
1. Admin can load a premade timetable into the database by loading CSV
files for specific days. This table is used for scheduling and can be viewed
by all other users.
2. Admin can add, edit and remove students. Admin can edit the student
registered courses, their details as well as other student information.
3. Admin can add, edit and remove teachers. Admin can edit the teachers’
courses, their details as well as other teacher information.
4. Admin can add, edit and remove courses. Admin can edit the courses, their
details as well as other course information.

The student will be shown to the main student menu after logging in. Here the
student can select one of the following:
• Timetable: This is the global timetable
• Schedule: This is the individual schedule of the student based on their
registered course.
• Classes: These are the registered classes that lead to the classes class hub.
• Chat: This is a shortcut to the group chats of each of the classes.
Class Hubs:
Each class will have its hub where the class's tasks, resources, chat, public
whiteboard, and joining live session option are available.
• Tasks: Here the student can view their tasks that are due as well as those
which are complete. Selecting a task the user can view the uploaded files,
and submit their solutions and if the task has been graded, they can view
the remarks and grades.
• Resource: Here the students can view all the shared resource files that are
available for their class.
• Join Session: This takes the student to a VR environment where they can
view the virtual whiteboard and the shared slides as well as be joined to
the voice chat of the live session.

The teacher will be shown to the main teacher menu after logging in. This is similar
to the student side with the main differences being in the classroom hub.
• Timetable: This is the global timetable
• Schedule: This is the individual schedule of the teacher based on the
courses they are teaching.
• Classes: These are the registered classes that lead to the classes class hub.
• Chat: This is a shortcut to the group chats of each of the classes.

Class Hubs:
Each class will have its hub where the class's tasks, resources, chat, public
whiteboard, and creating live session option are available.
• Tasks: Here the teacher can view the tasks they have placed for the
students as well as add a new task. They can upload the required files for
the task as well as review students submitted tasks and give grades and
feedback.
• Resource: Here the teacher can share and view the resource files for the
class.
• Create Session: This takes the teacher to an interface where they can write
on the whiteboard as well as select the pdf they want to share with the
students. The teacher will also join the voice chat of the classroom and
also be able to navigate the pdf and write on the whiteboard while it is
synced to the users.


References
==========
<dd></dd>
Basic UI: https://code-projects.org/teacher-classroom-assistant-in-android-with-source-code/
<dd></dd>
Timetable: https://github.com/tlaabs/TimetableView
<dd></dd>
Virtual Whiteboard: https://github.com/googlearchive/AndroidDrawing

