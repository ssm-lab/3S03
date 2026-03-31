<?php
session_start();
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    echo "<p style='color: red; font-size: 18px; text-align: center;'>Cannot access without an authorized account.</p>";
    echo "<p style='text-align: center;'><a href='login.php' style='text-decoration: none; color: white; background-color: #4caf50; padding: 10px 15px; border-radius: 5px;'>Go to Login</a></p>";
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Homepage</title>
    <style>
        body { 
            font-family: 'Arial', sans-serif; 
            text-align: center; 
            background-color: #e8f5e9; 
            color: #2e7d32; 
        }

        .container {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 50px;
            flex-wrap: wrap;
        }

        .preview-box {
            position: relative;
            width: 375px;
            height: 250px;
            border: 3px solid #4caf50;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.3);
            background: #ffffff;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }

        .preview-box a {
            position: absolute;
            top: 15px;
            left: 15px;
            padding: 12px 18px;
            background-color: #4caf50;
            color: white;
            border-radius: 8px;
            text-decoration: none;
            font-size: 18px;
            font-weight: bold;
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
            z-index: 2;
        }

        .preview {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: opacity 0.3s ease-in-out;
        }

    </style>
</head>
<body>
    <h1>Welcome to Our System</h1>
    
    <div class="container">
        <div class="preview-box" data-hover="false">
            <a href="logout.php" class="preview-btn">Log Out</a>
            <img src="data/preview/login_prev_still.png" data-gif="data/preview/login_prev.gif" class="preview">
        </div>
        <div class="preview-box" data-hover="false">
            <a href="main.php" class="preview-btn">Main Page</a>
            <img src="data/preview/main_prev_still.png" data-gif="data/preview/main_prev.gif" class="preview">
        </div>
        <div class="preview-box" data-hover="false">
            <a href="hidden.php" class="preview-btn">Hidden Content</a>
            <img src="data/preview/hidden_prev_still.png" data-gif="data/preview/hidden_prev.gif" class="preview">
        </div>
    </div>

    <script>
        document.querySelectorAll('.preview-box').forEach(box => {
            let img = box.querySelector('.preview');
            let btn = box.querySelector('.preview-btn');
            let staticSrc = img.src;
            let gifSrc = img.getAttribute("data-gif");

            function startGif() {
                img.src = gifSrc; 
            }

            function pauseGif() {
                img.src = staticSrc; 
            }

            box.addEventListener("mouseenter", startGif);
            box.addEventListener("mouseleave", pauseGif);
            btn.addEventListener("mouseenter", startGif);
            btn.addEventListener("mouseleave", pauseGif);
        });
    </script>
</body>
</html>
