<?php
session_start();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = trim($_POST['username']);
    $password = trim($_POST['password']);
    if (empty($username) || empty($password)) {
        $error = "Fields cannot be empty.";
    } elseif ($username == 'test' && $password == 'test') {
        $_SESSION['loggedin'] = true;
        header("Location: index.php");
        exit();
    } else {
        $error = "Invalid credentials.";
    }
}
?>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #e8f5e9; text-align: center; display: flex; justify-content: center; align-items: center; height: 100vh; }
        .login-container { background: white; padding: 40px; border-radius: 15px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); width: 320px; }
        h1 { color: #2e7d32; font-size: 32px; margin-bottom: 20px; }
        .logo { font-size: 50px; margin-bottom: 20px; }
        .input-group { display: flex; align-items: center; border: 1px solid #4caf50; border-radius: 25px; padding: 10px; margin-bottom: 15px; }
        .input-group input { border: none; outline: none; flex: 1; padding: 8px; font-size: 16px; }
        .input-group i { margin-right: 10px; color: #4caf50; }
        button { background-color: #4caf50; color: white; border: none; padding: 12px; width: 100%; border-radius: 25px; font-size: 18px; cursor: pointer; margin-top: 10px; }
        .error { color: red; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Welcome!</h1>
        <div class="logo">🎓</div>
        <form method="POST">
            <div class="input-group">
                <input type="text" name="username" placeholder="Username" required>
            </div>
            <div class="input-group">
                <input type="password" name="password" placeholder="Password" required>
            </div>
            <button type="submit">LOGIN</button>
        </form>
        <?php if (!empty($error)) echo "<p class='error'>$error</p>"; ?>
    </div>
</body>
</html>