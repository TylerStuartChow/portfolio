/*Tyler Chow
tsc221
1129 8048
*/

/*
some of the things I would change if I had the time
- move the functions for booking an appointment to a server and communicate with it using fetch requests
- change the book appointment page to fetch a list of dentists, appointment types, valid/free appointment slots from a server rather then it being hard coded
- automatically create the days to be stored for possible appointments rather than the days being created when a user tries to book an appointment on that day
- get the webpage to display if a time slot is free before the user presses "submit"
*/

var carouselImageList = ["./pictures/carousel/office 1.jpg", "./pictures/carousel/office 2.jpg", "./pictures/carousel/office 3.jpg", 
"./pictures/carousel/tools 1.jpg", "./pictures/carousel/tools 2.jpg"];

var carouselIndex = 0;


var employeeDictionary;




/*
Stores the information clients used to book an appointment
*/
class Appointment{
    constructor(firstName, lastName, email, date, appointmentType, appointmentTime){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        this.date = date;
        this.appointmentType = appointmentType;
        this.appointmentTime = appointmentTime;
    }
}




/*
stores information about an employee and the appointments booked for them
*/
class Employee{


    constructor(name){
        this.name = name;
        this.dayDictionary = {};
    }

    AddAppointment(firstName, lastName, email, date, appointmentType, appointmentTime){
        //I know I should have the days premade in like a database rather then creating days depending on user input

        if (this.dayDictionary[date] == null){//check if day was already created
            this.dayDictionary[date] = {
                "9am - 10am": null,
                "10am - 11am": null,
                "11am - 12pm": null,
                "12pm - 1pm": null,
                "1pm - 2pm": null,
                "2pm - 3pm": null,
                "3pm - 4pm": null,
                "4pm - 5pm": null
            }


            this.dayDictionary[date][appointmentTime] = new Appointment(firstName, lastName, email, date, appointmentType, appointmentTime);
            alert("success: appointment was booked");
        }
        else if (this.dayDictionary[date][appointmentTime] == null){
            this.dayDictionary[date][appointmentTime] = new Appointment(firstName, lastName, email, date, appointmentType, appointmentTime);
            alert("success: appointment was booked");

        }
        else{
            alert("that time is already taken");
        }

    }


}






function ChangeImageLeft(imageID){
    carouselIndex -= 1;
    if (carouselIndex < 0){
        carouselIndex = carouselImageList.length - 1;
    }
    

    let image = document.getElementById(imageID);
    image.src = carouselImageList[carouselIndex];
}

function changeImageRight(imageID){
    carouselIndex = (carouselIndex + 1) % carouselImageList.length;
    
    let image = document.getElementById(imageID);
    image.src = carouselImageList[carouselIndex];
}



/*
I know I should be checking for valid input
*/
function BookAppointment(){

    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;

    let email = document.getElementById("email").value;

    let dentist = document.getElementById("dentist").value;
    let appointmentType = document.getElementById("appointmentType").value;

    let date = document.getElementById("appointmentDate").value;
    let appointmentTime = document.getElementById("appointmentTime").value;
    
    document.getElementById("temp").innerHTML = date;

    if (date === ""){return;}
    

    employeeDictionary[dentist].AddAppointment(firstName, lastName, email, date, appointmentType, appointmentTime);
}




employeeDictionary = {
    "Dr. one": new Employee("Dr. one"),
    "Dr. two": new Employee("Dr. two"),
    "Dr. three:": new Employee("Dr. three")
}


setInterval(changeImageRight, 5 * 1000, "carouselDisplay");