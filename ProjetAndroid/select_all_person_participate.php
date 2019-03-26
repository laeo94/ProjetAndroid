<?php
include 'db_connect.php';
$personArray = array();
$response = array();
$result = array();
//Select all person in the account without the user himself
if(isset($_GET['aid']) && isset($_GET['uid'])){
	$aid = $_GET['aid'];
	$uid=$_GET['uid'];
	$query = "SELECT user.uid,user.pseudo FROM user INNER JOIN participations ON participations.uid =user.uid INNER JOIN account ON account.aid =participations.aid WHERE account.aid=? AND user.uid<>?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("ii",$aid,$uid);
		$stmt->execute();
		$stmt->bind_result($uid,$pseudo);
		while($stmt->fetch()){
			$personArray["uid"] = $uid;
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
	$response["success"] = 0;
	$response["message"] = "missing parameter aid";
}
//Display JSON response
echo json_encode($response);
?>