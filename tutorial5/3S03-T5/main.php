<?php
session_start();
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    echo "<p style='color: red; font-size: 18px; text-align: center;'>Cannot access without an authorized account.</p>";
    echo "<p style='text-align: center;'><a href='login.php' style='text-decoration: none; color: white; background-color: #4caf50; padding: 10px 15px; border-radius: 5px;'>Go to Login</a></p>";
    exit;
}

$data = [];
$file = 'data/department_data.csv';
$nextId = 1;
if (file_exists($file)) {
    $fileHandle = fopen($file, "r");
    while (($row = fgetcsv($fileHandle, 1000, ",")) !== FALSE) {
        $data[] = $row;
        $nextId = max($nextId, (int)$row[0] + 1); 
    }
    fclose($fileHandle);
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <style>
        body { font-family: Arial, sans-serif; text-align: center; background-color: #e8f5e9; }
        .container { background: white; padding: 20px; border-radius: 10px; width: 90%; margin: auto; margin-top: 50px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); display: flex; align-items: center; }
        .form-container { width: 30%; padding-right: 20px; display: flex; flex-direction: column; align-items: center; }
        .table-container { width: 70%; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; border: 1px solid #ddd; }
        th { background-color: #2e7d32; color: white; }
        .form-group { margin: 10px 0; }
        input { padding: 8px; width: 90%; max-width: 300px; }
        button { background-color: #4caf50; color: white; border: none; padding: 10px; border-radius: 5px; cursor: pointer; }
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
        }

        .back-button:hover {
            background-color: #1b5e20;
        }

    </style>
    <script>
        $(document).ready(function() {
        // Specify functionality of table
        let table = $('#infoTable').DataTable({
            "paging": true,
            "searching": true,
            "ordering": true,
            "info": true
        });

        $('#addEntry').on('click', function() {
            let name = $('#name').val().trim();
            let age = $('#age').val().trim();
            let department = $('#department').val().trim();
            let gpa = $('#gpa').val().trim();
            
            // Validating Input Data
            if (!name || !age || !department || !gpa) {
                alert("All fields must be filled out.");
                return;
            }

            let name_components = name.split(" ")
            if (name_components.length < 2) {
                alert("Must specify first and last name")
                return
            }

            if (isNaN(age) || isNaN(gpa)) {
                alert("Age and GPA must be numbers.");
                return;
            }
            age = parseInt(age);
            gpa = parseFloat(gpa);
            if (age < 15) {
                alert("Age must be 15 or older.");
                return;
            }
            if (gpa < 0 || gpa > 12) {
                alert("GPA must be between 0 and 12.");
                return;
            }

            // Send data to save_entry.php to update CSV, persist data updates
            $.post('save_entry.php', { name, age, department, gpa }, function(response) {
                let result = JSON.parse(response);

                if (result.status === "success") {
                    // Add row to table
                    table.row.add([result.id, result.name, result.age, result.department, result.gpa]).draw(false);

                    // Clear input fields
                    $('#name, #age, #department, #gpa').val('');
                } else {
                    alert(result.message);
                }
            }).fail(function() {
                alert("Error: Could not save entry.");
            });
        });
    });

    </script>
</head>
<a href="index.php" class="back-button">Back to Home</a>

<body>
    <div class="container">
        <div class="form-container">
            <h3>Add New Entry</h3>
            <div class="form-group"><input type="text" id="name" placeholder="Name"></div>
            <div class="form-group"><input type="text" id="age" placeholder="Age"></div>
            <div class="form-group"><input type="text" id="department" placeholder="Department"></div>
            <div class="form-group"><input type="text" id="gpa" placeholder="GPA"></div>
            <button id="addEntry">Add Entry</button>
        </div>
        <div class="table-container">
            <h1>Department Data</h1>
            <table id="infoTable" class="display">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Age</th>
                        <th>Department</th>
                        <th>GPA</th>
                    </tr>
                </thead>
                <tbody>
                    <?php
                    foreach ($data as $row) {
                        echo "<tr>";
                        echo "<td>" . htmlspecialchars($row[0]) . "</td>";
                        echo "<td>" . htmlspecialchars($row[1]) . "</td>";
                        echo "<td>" . htmlspecialchars($row[2]) . "</td>";
                        echo "<td>" . htmlspecialchars($row[3]) . "</td>";
                        echo "<td>" . htmlspecialchars($row[4]) . "</td>";
                        echo "</tr>";
                    }
                    ?>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>