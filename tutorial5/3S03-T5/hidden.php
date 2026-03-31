<?php
session_start();
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    echo "<p style='color: red; font-size: 18px; text-align: center;'>Cannot access without an authorized account.</p>";
    echo "<p style='text-align: center;'><a href='login.php' style='text-decoration: none; color: white; background-color: #4caf50; padding: 10px 15px; border-radius: 5px;'>Go to Login</a></p>";
    exit;
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>University Server Request Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #e8f5e9;
            margin: 0;
            padding: 0;
        }
        .container {
            background: white;
            padding: 25px;
            border-radius: 10px;
            width: 90%;
            margin: auto;
            margin-top: 40px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            display: flex;
            justify-content: center;
            align-items: flex-start;
            position: relative;
            border: 2px solid #2e7d32;
            transition: justify-content 1s ease-in-out;
            gap: 30px;
        }
        #loadingScreen {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            z-index: 1000;
        }
        .chart-container {
            width: 60%;
            max-width: 700px;
            height: 400px;
            padding: 20px;
            transition: width 1s ease-in-out, transform 1s ease-in-out;
        }
        #dataPanel {
            background: #2e7d32;
            color: white;
            padding: 12px;
            border-radius: 5px;
            font-size: 16px;
            margin-top: 10px;
            font-weight: bold;
            /* display: inline-block; */
            width: auto;
            display: none;
        }
        .divider {
            width: 3px;
            height: 90%;
            background-color: #2e7d32;
            display: none;
            transition: opacity 1s ease-in-out;
        }
        #history {
            width: 35%;
            max-width: 400px;
            display: none;
            flex-direction: column;
            align-items: center;
            padding: 20px;
            font-size: 12px;
            background: white;
            transition: opacity 1s ease-in-out, width 1s ease-in-out;
        }
        #historyContent {
            display: flex;
            flex-direction: column;
            gap: 5px;
            max-height: 350px;
            overflow-y: auto;
            border: 2px solid #2e7d32;
            padding: 10px;
            border-radius: 5px;
            background: white;
            width: 100%;
        }
        .history-header, .history-item {
            display: flex;
            justify-content: center;
            padding: 6px;
            font-weight: bold;
            border-bottom: 1px solid #4caf50;
        }
        .history-item {
            font-weight: normal;
        }
        .history-column {
            flex: 1;
            text-align: center;
        }
        .back-button {
            position: absolute;
            top: 15px;
            left: 15px;
            padding: 10px 15px;
            background-color: #2e7d32;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 16px;
            box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.2);
            z-index: 1000;
        }
        .back-button:hover {
            background-color: #1b5e20;
        }
    </style>
</head>
<body>

<a href="index.php" class="back-button">Back to Home</a>

<div id="loadingScreen">Loading dashboard...</div>

<div class="container" id="dashboard">
    <div class="chart-container" id="chartWrapper">
        <h2 style="color:#2e7d32;">University Server Request Dashboard</h2>
        <canvas id="lineChart"></canvas>
        <div id="dataPanel">
            <strong>Most Recent Update:</strong> <br>
            <span id="latestTime">0:00:00 AM</span> - 
            <span id="latestRequests">0</span> Request
        </div>

    </div>

    <div class="divider" id="divider"></div>

    <div id="history">
        <h3 style="text-align:center; color:#2e7d32;">Request History</h3>
        <div id="historyContent">
            <div class="history-header">
                <div class="history-column">Time</div>
                <div class="history-column">Origin</div>
                <div class="history-column">Request Type</div>
                <div class="history-column">Processing Time (ms)</div>
                <div class="history-column">Status</div>
            </div>
        </div>
    </div>
</div>

<script>
    // Simulate a loading screen with a random delay between 2s and 10s
    const loadingTime = Math.floor(Math.random() * (10000 - 2000 + 1)) + 2000;
    const origins = ["Hamilton", "Brampton", "Barrie", "Ottawa", "Toronto", "Mississauga", "Vaughan", "Guelph", "Gatineau", "Montreal"];
    const requestTypes = ["Login", "Search", "Download", "Form Submission", "API Call"];
    const statuses = ["Success", "Failed", "Timeout", "Queued"];

    setTimeout(() => {
        document.getElementById('loadingScreen').style.display = 'none';
        let dashboard = document.getElementById('dashboard');
        dashboard.style.display = 'flex';
        document.getElementById('dataPanel').style.display = 'block';

        requestAnimationFrame(() => {
            startDashboard();
        });

    }, loadingTime);

    function startDashboard() {
        let ctx = document.getElementById('lineChart').getContext('2d');
        let chartData = {
            labels: [],
            datasets: [{
                label: 'Total Requests',
                borderColor: 'rgb(56, 142, 60)',
                backgroundColor: 'rgba(56, 142, 60, 0.2)',
                data: [],
                fill: false,
                tension: 0
            }]
        };

        let lineChart = new Chart(ctx, 
        { 
            type: 'line', 
            data: chartData,
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    x: { title: { display: true, text: 'Time' } },
                    y: { 
                        title: { display: true, text: 'Request Count' },
                        ticks: {
                            stepSize: 1,
                            callback: function(value, index, values) {
                                return Number.isInteger(value) ? value : null; 
                            }
                        }
                    }
                }}
        });

        let historyDiv = document.getElementById('history');
        let historyContent = document.getElementById('historyContent');
        let divider = document.getElementById('divider');

        let totalDataPoints = 0;

        function updateDashboard() {
            let newTime = new Date().toLocaleTimeString();
            let requestsGenerated = Math.floor(Math.random() * 5) + 1;
            totalDataPoints += requestsGenerated;

            chartData.labels.push(newTime);
            chartData.datasets[0].data.push(requestsGenerated);

            // Limit chart to 20 data points to make it easier to read
            if (chartData.labels.length > 20) {
                    chartData.labels.shift();
                    chartData.datasets[0].data.shift();
                }
            lineChart.update();

            // Update box showing latest entries
            document.getElementById('latestTime').innerText = newTime;
            document.getElementById('latestRequests').innerText = requestsGenerated;


            for (let i = 0; i < requestsGenerated; i++) {
                // Randomly generate the data for the entries
                let origin = origins[Math.floor(Math.random() * origins.length)];
                let requestType = requestTypes[Math.floor(Math.random() * requestTypes.length)];
                let processingTime = Math.floor(Math.random() * 500) + 100;
                let status = statuses[Math.floor(Math.random() * statuses.length)];

                historyContent.innerHTML += `
                    <div class="history-item">
                        <div class="history-column">${newTime}</div>
                        <div class="history-column">${origin}</div>
                        <div class="history-column">${requestType}</div>
                        <div class="history-column">${processingTime} ms</div>
                        <div class="history-column">${status}</div>
                    </div>`;
            }

            // Simulates a delay, only shows history after atleast 5 data points exist
            if (totalDataPoints >= 5) {
                historyDiv.style.display = 'flex';
                divider.style.display = "block";
                // Scroll to most recent entries
                setTimeout(() => { historyContent.scrollTop = historyContent.scrollHeight; }, 100);
            }
        }

        setInterval(updateDashboard, 2000);
    }
</script>

</body>
</html>
