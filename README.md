# VClass
==========

<dl>
  VClass will be an android application that will provide the features of a classroom from 
  the comfort of people’s homes. Using virtual reality technology, a more immersive and
  interactive experience can be had relative to the typical online video conferencing or 
  classroom software.
</dl>

start
==========
<dl>
  The application uses firebasee as an online database to handle thus to thus to use run the application
  you will first need to set the fire base URL in the following classes to one that u can run.
  <dd></dd>
   <dd> 
     Classes : LiveStudentClassActivity, DBHelper,BoardListActivity
   </dd>
   <dt>
      replace        FIREBASE_URL = "insert url here"
   </dt><dd></dd>
     
   Currently to store credentials and other information, we are using a local SQL database.
   
  
     
</dl>

current implementation
==========
we are currently using 2 databases, a local SQL database, that
will be moved to a web-service later on, and firebase to manage the online functions
and information for the whiteboard and chatroom. The SQL database is used to store
data such as the credentials, timetable slots, course information, and other information
related components.
For the whiteboard, we are using a firebase-based implementation, where each
course has a unique whiteboard link generated. the links and their corresponding
information, such as lines, are stored on firebase and constantly synced with other
endpoints that have the same whiteboard opened. for example, the whiteboard for the
course programming fundamentals is opened by teacherA and studentA, the teacher
writes something on the board, the inputs are quickly uploaded to the database and all
other instances of the current whiteboard, in this case, studentA, will also have their
views updated simultaneously.
For the chatroom, we are again using a firebase-based implementation where
the messages are uploaded and retrieved by the firebase handler. to set it up for a group
chat, messages sent by different people have the course name as a receiver, where the
course name refers to the group chat those messages belong to.
