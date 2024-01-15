
<h1>StudyWithMe (Calendar and study app)</h1>

It was a school project worked on in 2023 with Kayden Kessler, Sakhana Suresh Kumar, Tanner Mandziak, and Tommy Ojo. 
It was originally in a repository on GitLab, then moved to Tommy's GitHub where development continued. This is a copy from Tommy's repository from January 14th, 2024, where the placeholder name "circles_app" was replaced.
https://github.com/okaytom/circles_app

At the time this copy was made, the note taking feature was incomplete due to time constraints and a reduction from 5 to 2 members working on the project. Originally the notes feature worked with pdf files but it had issues with formatting the documents, resulting in the inability to highlight or change the colour of a single section of text or adding an image in the intended location. Thus upon finding a library that supported iterating through a docx file, splitting its content by similar formatting, we switched to apache.poi.ooxml. Since this switch occurred after the class was already finished, it resulted in a lack of members and time to fully implement this change.


I worked on the project as a backend developer. Rather then setting up a proper data base, we used JSON files and folders to locally store data due to time constraints and a greater familiarity with JSON files.

<br>
  

<h3>Features I worked on:</h3>
<ul>
  <li>loading and saving Java objects from JSON files using GSON</li>
  <li>began work on methods that created a list of objects from a docx file to pass to the frontend</li>
  <li>began work on methods that took a list of objects from the front end to create a docx file</li>
  
  <li>backend for 
    <ul>
      <li>the search feature</li>
      <li>managing a list of objects for courses and their folders</li>
      <li>cue cards</li>
    </ul>
  </li>
</ul>



<br>
  


It was run and tested with Intellij using Java 17 on Windows
- run Launcher.java's main to launch the app

<br>
    
<h3>Requirements / Libraries Used:</h3>
<ul>
  <li>apache.poi.ooxml 5.2.5</li>
  <li>JavaFX 17.0.2</li>
  <li>GSON 2.8.6</li>
  <li>pdfbox 2.0.30</li>
  <li>fontbox 2.0.29</li>
  <li>fxmisc.richtext 0.11.2</li>
</ul>

<br><br>

<h1>Dental website</h1>
This project was developed in 2022 for a second year course on website design and development, the assignment was to create a website for a dental clinic with certain features. If I had the time, I would like to work on the JavaScript futher

<br>
<h3>changes I would make:</h3>
<ul>
  <li>split the JavaScript to be client side, server side, and use fetch requests to communicate between the two</li>
  <li>generate options such as which dentist, appointment type, and available time slots on the "Book Appointment" page by requesting data from the backend rather than hard coding in those options</li>
</ul>


