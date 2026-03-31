<?php
session_start();
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    echo "<p style='color: red; font-size: 18px; text-align: center;'>Cannot access without an authorized account.</p>";
    echo "<p style='text-align: center;'><a href='login.php' style='text-decoration: none; color: white; background-color: #4caf50; padding: 10px 15px; border-radius: 5px;'>Go to Login</a></p>";
    exit;
}
$file = 'data/department_data.csv';
$nextId = 1;

// Check if file exist and grab the next ID
if (file_exists($file)) {
    $fileHandle = fopen($file, "r");
    while (($row = fgetcsv($fileHandle, 1000, ",")) !== FALSE) {
        $nextId = max($nextId, (int)$row[0] + 1);
    }
    fclose($fileHandle);
}

$name = $_POST['name'];
$age = $_POST['age'];
$department = $_POST['department'];
$gpa = $_POST['gpa'];

// Append data to CSV file
$fileHandle = fopen($file, "a");
fputcsv($fileHandle, [$nextId, $name, $age, $department, $gpa]);
fclose($fileHandle);

// return success response
echo json_encode([
    "status" => "success",
    "id" => $nextId,
    "name" => $name,
    "age" => $age,
    "department" => $department,
    "gpa" => number_format($gpa, 2)
]);
?>
