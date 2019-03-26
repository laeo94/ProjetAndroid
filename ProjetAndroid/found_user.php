<?php
include 'db_connect.php';
$pseudoArray = array();
$response = array();
$result = array();
if(isset($_GET['rech'])){
	$rech = $_GET['rech'];
	$query="SELECT uid,pseudo FROM user WHERE pseudo LIKE '%$rech%'";
	if($stmt = $con->prepare($query)){
		$stmt->execute();
		$stmt->bind_result($uid,$pseudo);
		while($stmt->fetch()){
			$pseudoArray["uid"] = $uid;
			$pseudoArray["pseudo"] = $pseudo;
			$result[]=$pseudoArray;
		}
		$stmt->close();
		$response["success"] = 1;
		$response["data"] = $result;
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
	
}else{
	$response["success"] = 0;
	$response["message"] = "Error missing rech parametre ";
}
//Display JSON response
echo json_encode($response);
?>
