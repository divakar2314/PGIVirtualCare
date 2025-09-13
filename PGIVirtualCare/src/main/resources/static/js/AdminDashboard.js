
const visitChart = new Chart(document.getElementById("visitChart"), {
      type: "line",
      data: {
        labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        datasets: [{
          label: "Visits",
          data: [120, 150, 180, 130, 170, 90, 100],
          borderColor: "blue",
          backgroundColor: "rgba(13,110,253,0.1)",
          fill: true
        }]
      }
    });
	

    const doctorChart = new Chart(document.getElementById("doctorChart"), {
      type: "bar",
      data: {
        labels: ["Cardio", "Neuro", "ENT", "Ortho", "Pediatrics"],
        datasets: [{
          label: "Doctors Available",
          data: [10, 8, 6, 12, 9],
          backgroundColor: "#20c997"
        }]
      }
    });