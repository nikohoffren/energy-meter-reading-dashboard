let stompClient = null;
let readingsChart = null;
const MAX_DATA_POINTS = 20;
const readings = [];
const timestamps = [];
let reconnectAttempts = 0;
const MAX_RECONNECT_ATTEMPTS = 5;

function connect() {
  const socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);

  // Disable debug logging
  stompClient.debug = null;

  stompClient.connect(
    {},
    // Success callback
    function (frame) {
      console.log("Connected: " + frame);
      reconnectAttempts = 0;

      // Subscribe to meter readings
      stompClient.subscribe("/topic/meter-readings", function (message) {
        try {
          const reading = JSON.parse(message.body);
          updateReadingsChart(reading);
          updateReadingsTable(reading);
        } catch (error) {
          console.error("Error processing meter reading:", error);
        }
      });

      // Subscribe to anomaly alerts
      stompClient.subscribe("/topic/anomalies", function (message) {
        try {
          const alert = JSON.parse(message.body);
          showAnomalyAlert(alert);
        } catch (error) {
          console.error("Error processing anomaly alert:", error);
        }
      });
    },
    // Error callback
    function (error) {
      console.error("WebSocket connection error:", error);
      if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
        reconnectAttempts++;
        console.log(
          `Attempting to reconnect (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})...`
        );
        setTimeout(connect, 5000);
      } else {
        console.error(
          "Max reconnection attempts reached. Please refresh the page."
        );
        showConnectionError();
      }
    }
  );
}

function showConnectionError() {
  const alertsDiv = document.getElementById("alerts");
  const errorElement = document.createElement("div");
  errorElement.className = "alert-item high";
  errorElement.innerHTML = `
    <strong>Connection Error</strong><br>
    Unable to connect to the server. Please refresh the page.
  `;
  alertsDiv.insertBefore(errorElement, alertsDiv.firstChild);
}

function updateReadingsChart(reading) {
  try {
    const timestamp = new Date(reading.timestamp).toLocaleTimeString();

    readings.push(reading.value);
    timestamps.push(timestamp);

    if (readings.length > MAX_DATA_POINTS) {
      readings.shift();
      timestamps.shift();
    }

    readingsChart.data.labels = timestamps;
    readingsChart.data.datasets[0].data = readings;
    readingsChart.update();
  } catch (error) {
    console.error("Error updating chart:", error);
  }
}

function updateReadingsTable(reading) {
  try {
    const tbody = document.getElementById("readingsTable");
    const row = document.createElement("tr");

    row.innerHTML = `
      <td>${reading.id}</td>
      <td>${new Date(reading.timestamp).toLocaleString()}</td>
      <td>${reading.value.toFixed(2)}</td>
      <td>${reading.anomalyScore ? reading.anomalyScore.toFixed(2) : "N/A"}</td>
    `;

    tbody.insertBefore(row, tbody.firstChild);

    if (tbody.children.length > MAX_DATA_POINTS) {
      tbody.removeChild(tbody.lastChild);
    }
  } catch (error) {
    console.error("Error updating table:", error);
  }
}

function showAnomalyAlert(alert) {
  try {
    if (!alert || !alert.reading) {
      console.warn("Received invalid anomaly alert:", alert);
      return;
    }

    const alertsDiv = document.getElementById("alerts");
    const alertElement = document.createElement("div");
    alertElement.className = `alert-item ${
      alert.anomalyScore > 0.9 ? "high" : ""
    }`;

    alertElement.innerHTML = `
      <strong>Anomaly Detected!</strong><br>
      Meter ID: ${alert.reading.id}<br>
      Value: ${alert.reading.value.toFixed(2)}<br>
      Anomaly Score: ${alert.anomalyScore.toFixed(2)}<br>
      Time: ${new Date(alert.timestamp).toLocaleString()}
    `;

    alertsDiv.insertBefore(alertElement, alertsDiv.firstChild);

    if (alertsDiv.children.length > 5) {
      alertsDiv.removeChild(alertsDiv.lastChild);
    }
  } catch (error) {
    console.error("Error showing anomaly alert:", error);
  }
}

function initChart() {
  try {
    const ctx = document.getElementById("readingsChart").getContext("2d");
    readingsChart = new Chart(ctx, {
      type: "line",
      data: {
        labels: [],
        datasets: [
          {
            label: "Meter Reading",
            data: [],
            borderColor: "rgb(75, 192, 192)",
            tension: 0.1,
            fill: false,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: "Value",
            },
          },
          x: {
            title: {
              display: true,
              text: "Time",
            },
          },
        },
      },
    });
  } catch (error) {
    console.error("Error initializing chart:", error);
  }
}

// Initialize the dashboard
window.onload = function () {
  try {
    initChart();
    connect();
  } catch (error) {
    console.error("Error initializing dashboard:", error);
    showConnectionError();
  }
};
