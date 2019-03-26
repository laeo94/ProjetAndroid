<?php
include 'db_connect.php';
$accountArray = array();
$response = array();
if(isset($_GET['aid'])){
	$aid = $_GET['aid'];
	$requet = "SELECT SUM(somme) FROM depense WHERE aid=?";
	$stmt= $con->prepare($requet);
	$stmt->bind_param("i",$aid);
	$stmt->execute();
	$stmt->bind_result($somme);
	$stmt->fetch();
	$accountArray["somme"] = $somme;
	$stmt->close();
	$query = "SELECT title,device,description FROM account WHERE aid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("i",$aid);
		$stmt->execute();
		$stmt->bind_result($title,$device,$description);	
		if($stmt->fetch()){
			$accountArray["aid"] = $aid;
			$accountArray["title"] = $title;
			$accountArray["device"] = $device;
			$accountArray["description"] = $description;
			
			$response["success"] = 1;
			$response["data"] = $accountArray;
		
		}else{
			$response["success"] = 0;
			$response["message"] = "Account not found";
		}
		$stmt->close();
 
 
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}
 
}else{
	$response["success"] = 0;
	$response["message"] = "missing parameter aid";
}
//Display JSON response
echo json_encode($response);
?>