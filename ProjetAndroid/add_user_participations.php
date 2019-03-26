<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['aid']) && isset($_POST['uid'])){
	$aid =$_POST['aid'];
	$uid = $_POST['uid'];
	$query = "SELECT aid, uid FROM participations WHERE aid=? AND uid=?";
	$stmt = $con->prepare($query);
	$stmt->bind_param("ii",$aid,$uid);
	$stmt->execute();
	if($stmt->fetch()){
		$response["success"] = 2;			
		$response["message"] = "Person is already been had into participations";	
	}else{
	$query = "INSERT INTO participations (aid,uid) VALUES (?,?)";

	if($stmt = $con->prepare($query)){
		
		$stmt->bind_param("ii",$aid,$uid);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Person has been had into participations";			
		}else{
			$response["success"] = 0;
			$response["message"] = "Error while adding person into participations";
		}					
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
}
}else{
	$response["success"] = 0;
	$response["message"] = "missing uid or/and aid parameters";
}
//Displaying JSON response
echo json_encode($response);
?>