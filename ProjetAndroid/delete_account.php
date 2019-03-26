<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['aid'])){
	$accountId = $_POST['aid'];
	$query = "DELETE FROM account WHERE aid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("i",$accountId);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Account got deleted successfully";	
		}else{
			$response["success"] = 0;
			$response["message"] = "Account not found";
		}					
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
}else{
	$response["success"] = 0;
	$response["message"] = "missing parameter movie_id";
}
echo json_encode($response);
?>