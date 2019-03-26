<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['title'])&&isset($_POST['description'])&&isset($_POST['device'])&&isset($_POST['uid'])){
	$title = $_POST['title'];
	$description = $_POST['description'];
	$device= $_POST['device'];
	$uid= $_POST['uid'];
	
	$query = "INSERT INTO account(title,description,device) VALUES (?,?,?)";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("sss",$title,$description,$device);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "account has been had";		
			$stmt->close();
		}else{
			$response["success"] = 0;
			$response["message"] = "Error while adding account";
		}	
	}	
	$lastaid = "SELECT MAX(aid) FROM account ";
	if($stmt = $con->prepare($lastaid)){
		$stmt->execute();
		$stmt->bind_result($aid);
		$stmt->fetch();
		$aaid=aid;
		$stmt->close();
	}

	$query2= "INSERT INTO participations (aid,uid) VALUES (?,?)";
	if($stmt = $con->prepare($query2)){
		$stmt->bind_param("ii",$aid,$uid);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;
			$response["message"] = "accound had been had and into participations";		
			$stmt->close();
		}else{
			$response["success"] = 0;
			$response["message"] = "Error while adding compte dans parti";
		}	
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}

}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing title,description,device or uid parameters";
}
//Displaying JSON response
echo json_encode($response);
?>