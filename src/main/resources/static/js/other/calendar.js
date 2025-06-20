const calendar = document.querySelector(".calendar"),
  date = document.querySelector(".date"),
  daysContainer = document.querySelector(".days"),
  prev = document.querySelector(".prev"),
  next = document.querySelector(".next"),
  todayBtn = document.querySelector(".today-btn"),
  gotoBtn = document.querySelector(".goto-btn"),
  dateInput = document.querySelector(".date-input"),
  eventDay = document.querySelector(".event-day"),
  eventDate = document.querySelector(".event-date"),
  eventsContainer = document.querySelector(".events"),
  addEventBtn = document.querySelector(".add-event"),
  addEventWrapper = document.querySelector(".add-event-wrapper"),
  addEventCloseBtn = document.querySelector(".close"),
  addEventTitle = document.querySelector(".event-name"),
  addEventFrom = document.querySelector(".event-time-from"),
  addEventTo = document.querySelector(".event-time-to"),
  addEventSubmit = document.querySelector(".add-event-btn");

let today = new Date();
let activeDay;
let month = today.getMonth();
let year = today.getFullYear();

const months = [
  "Януари",
  "Февруари",
  "Март",
  "Април",
  "Май",
  "Юни",
  "Юли",
  "Август",
  "Септември",
  "Октомври",
  "Ноември",
  "Декември",
];


const eventsArr = [];


lectionEvents.forEach(event => {
  const eventDate = new Date(event.date);

  const newEvent = {
    day: eventDate.getDate(),
    month: eventDate.getMonth() + 1,
    year: eventDate.getFullYear(),
    events: [{
      title: event.title,
      time: "Цял ден",
    }],
  };


  let existingEvent = eventsArr.find(e => e.day === newEvent.day && e.month === newEvent.month && e.year === newEvent.year);
  if (existingEvent) {
    existingEvent.events.push(newEvent.events[0]);
  } else {
    eventsArr.push(newEvent);
  }
});

initCalendar();

function initCalendar() {
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const prevLastDay = new Date(year, month, 0);
  const prevDays = prevLastDay.getDate();
  const lastDate = lastDay.getDate();
  let day = firstDay.getDay();


  day = day === 0 ? 6 : day - 1;

  const nextDays = 7 - (lastDay.getDay() === 0 ? 7 : lastDay.getDay());

  date.innerHTML = months[month] + " " + year;

  let days = "";


  for (let x = day; x > 0; x--) {
    days += `<div class="day prev-date">${prevDays - x + 1}</div>`;
  }


  for (let i = 1; i <= lastDate; i++) {
    let event = false;
    eventsArr.forEach(eventObj => {
      if (eventObj.day === i && eventObj.month === month + 1 && eventObj.year === year) {
        event = true;
      }
    });

    if (i === new Date().getDate() && year === new Date().getFullYear() && month === new Date().getMonth()) {
      activeDay = i;
      getActiveDay(i);
      updateEvents(i);
      days += event ? `<div class="day today active event">${i}</div>` : `<div class="day today active">${i}</div>`;
    } else {
      days += event ? `<div class="day event">${i}</div>` : `<div class="day">${i}</div>`;
    }
  }


  for (let j = 1; j <= nextDays; j++) {
    days += `<div class="day next-date">${j}</div>`;
  }

  daysContainer.innerHTML = days;
  addListner();
}

function prevMonth() {
  month--;
  if (month < 0) {
    month = 11;
    year--;
  }
  initCalendar();
}

function nextMonth() {
  month++;
  if (month > 11) {
    month = 0;
    year++;
  }
  initCalendar();
}

prev.addEventListener("click", prevMonth);
next.addEventListener("click", nextMonth);

function addListner() {
  const days = document.querySelectorAll(".day");
  days.forEach(day => {
    day.addEventListener("click", e => {
      getActiveDay(e.target.innerHTML);
      updateEvents(Number(e.target.innerHTML));
      activeDay = Number(e.target.innerHTML);
      days.forEach(day => {
        day.classList.remove("active");
      });
      if (e.target.classList.contains("prev-date")) {
        prevMonth();
        setTimeout(() => {
          const days = document.querySelectorAll(".day");
          days.forEach(day => {
            if (!day.classList.contains("prev-date") && day.innerHTML === e.target.innerHTML) {
              day.classList.add("active");
            }
          });
        }, 100);
      } else if (e.target.classList.contains("next-date")) {
        nextMonth();
        setTimeout(() => {
          const days = document.querySelectorAll(".day");
          days.forEach(day => {
            if (!day.classList.contains("next-date") && day.innerHTML === e.target.innerHTML) {
              day.classList.add("active");
            }
          });
        }, 100);
      } else {
        e.target.classList.add("active");
      }
    });
  });
}

todayBtn.addEventListener("click", () => {
  today = new Date();
  month = today.getMonth();
  year = today.getFullYear();
  initCalendar();
});

dateInput.addEventListener("input", (e) => {
  dateInput.value = dateInput.value.replace(/[^0-9/]/g, "");
  if (dateInput.value.length === 2) {
    dateInput.value += "/";
  }
  if (dateInput.value.length > 7) {
    dateInput.value = dateInput.value.slice(0, 7);
  }
  if (e.inputType === "deleteContentBackward") {
    if (dateInput.value.length === 3) {
      dateInput.value = dateInput.value.slice(0, 2);
    }
  }
});

gotoBtn.addEventListener("click", gotoDate);

function gotoDate() {
  const dateArr = dateInput.value.split("/");
  if (dateArr.length === 2) {
    if (dateArr[0] > 0 && dateArr[0] < 13 && dateArr[1].length === 4) {
      month = dateArr[0] - 1;
      year = dateArr[1];
      initCalendar();
      return;
    }
  }
  alert("Invalid Date");
}

function getActiveDay(date) {
  const day = new Date(year, month, date);
  const dayName = day.toString().split(" ")[0];
  eventDay.innerHTML = dayName;
  eventDate.innerHTML = date + " " + months[month] + " " + year;
}

function updateEvents(date) {
  let events = "";
  eventsArr.forEach(event => {
    if (date === event.day && month + 1 === event.month && year === event.year) {
      event.events.forEach(event => {
        events += `<div class="event">
            <div class="title-cldr">
              <i class="fas fa-circle"></i>
              <h3 class="event-title" style="color: #ffffff;">${event.title}</h3>
            </div>
            <div class="event-time">
              <span class="event-time">${event.time}</span>
            </div>
        </div>`;
      });
    }
  });
  if (events === "") {
    events = `<div class="no-event">
            <h3 style="color: #878895;">Няма лекции</h3>
        </div>`;
  }
  eventsContainer.innerHTML = events;
  saveEvents();
}

addEventBtn.addEventListener("click", () => {
  addEventWrapper.classList.toggle("active");
});

addEventCloseBtn.addEventListener("click", () => {
  addEventWrapper.classList.remove("active");
});

document.addEventListener("click", (e) => {
  if (e.target !== addEventBtn && !addEventWrapper.contains(e.target)) {
    addEventWrapper.classList.remove("active");
  }
});

addEventTitle.addEventListener("input", (e) => {
  addEventTitle.value = addEventTitle.value.slice(0, 60);
});

function saveEvents() {

}

addEventSubmit.addEventListener("click", (e) => {
  e.preventDefault();
  const title = addEventTitle.value;
  const from = addEventFrom.value;
  const to = addEventTo.value;
  if (title) {
    const newEvent = {
      title: title,
      time: `${from} - ${to}`,
    };

    let existingEvent = eventsArr.find(event => event.day === activeDay && event.month === month + 1 && event.year === year);
    if (existingEvent) {
      existingEvent.events.push(newEvent);
    } else {
      eventsArr.push({
        day: activeDay,
        month: month + 1,
        year: year,
        events: [newEvent],
      });
    }

    addEventWrapper.classList.remove("active");
    addEventTitle.value = "";
    addEventFrom.value = "";
    addEventTo.value = "";
    updateEvents(activeDay);
  }
});
