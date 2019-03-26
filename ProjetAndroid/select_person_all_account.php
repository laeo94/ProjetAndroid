<?php
include 'db_connect.php';
$personArray = array();
$response = array();
$result = array();
if(isset($_GET['uid'])){
	$uid = $_GET['uid'];
	$query = "SELECT account.aid,account.title FROM account INNER JOIN participations ON participations.aid = account.aid INNER JOIN user ON user.uid = participations.uid WHERE participations.uid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("i",$uid);
		$stmt->execute();
		$stmt->bind_result($aid,$title);	
		while($stmt->fetch()){
			$personArray["aid"] = $aid;
			$personArray["title"] = $title;
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
	$response["message"] = "missing parameter uid";
}
//Display JSON response
echo json_encode($response);
?>