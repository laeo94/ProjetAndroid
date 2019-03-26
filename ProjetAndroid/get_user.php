<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['uid'])){
	$uid = $_GET['uid'];
	$query = "SELECT pseudo FROM user WHERE uid=$uid";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("i",$uid);
		$stmt->execute();
		$stmt->bind_result($pseudo);	
		while($stmt->fetch()){
			$personArray["pseudo"] = $pseudo;
			$result[]=$personArray;
		}
		$response["success"] = 1;
		$response["data"] = $result;
	
		$stmt->close();
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);	
	}
 
}else{
	//When the mandatory parameter movie_id is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter uid";
}
//Display JSON response
echo json_encode($response);
?>