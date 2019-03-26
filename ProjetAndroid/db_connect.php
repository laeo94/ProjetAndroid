<?php
//Attention il gaudrai peut etre change le mot de passe
define('DB_USER', "u21505006"); // db user
define('DB_PASSWORD',"434p81IYzTGu"); // db password (mention your db password here)
define('DB_DATABASE', "u21505006"); // database name
define('DB_SERVER', "localhost"); // db server
 
$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE);
 
// Check connection
if(mysqli_connect_errno()){
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
?>