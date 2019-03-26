
<?php
include 'db_connect.php';
$query = "SELECT title FROM account";
$result = array();
$compteArray = array();
$response = array();

if($stmt = $con->prepare($query)){
	$stmt->execute();
	$stmt->bind_result($aid,$title);			
	while($stmt->fetch()){
		$compteArray["aid"] = $aid;
		$compteArray["title"] = $title;
		$result[]=$compteArray;
	}
	$stmt->close();
	$response["success"] = 1;
	$response["data"] = $result;
}else{
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
}
//Display JSON response
echo json_encode($response);
 
?>
