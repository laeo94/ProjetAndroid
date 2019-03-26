<?php
include 'db_connect.php';
use Pw\Auth\AuthenticatorInterface;
$userArray = array();
$response = array();
$result = array();
//Check for mandatory parameter movie_id
if(isset($_GET['pseudo'])&&isset($_GET['mdp'])){
	$pseudo = $_GET['pseudo'];
	$mdpuser =$_GET['mdp'];

	//Query to fetch account details
	$query = "SELECT uid,mdp FROM user WHERE pseudo =?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("s",$pseudo);
		$stmt->execute();
		//Bind fetched result to variables $title, $genre, $year and $rating
		$stmt->bind_result($uid,$mdp);
		//Check for results		
		if($stmt->fetch()){
		 	//Populate the movie array
		 	$mdphash =$mdp;
		 	if (password_verify($mdpuser, $mdphash)) {
			$userArray["uid"] = $uid;
			$result[]=$userArray;
			$response["success"] = 1;
			$response["data"] = $result;
			$stmt->close();
			}else{
				$response["success"] = 2;
				$response["data"] ="mdp not valide";
			}
		}
	}else{
		//Whe some error occurs
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}
 
}else{
	//When the mandatory parameter movie_id is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter aid";
}
//Display JSON response
echo json_encode($response);
?>