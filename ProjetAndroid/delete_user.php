<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['uid'])){
	$uid = $_POST['uid'];
	$query = "DELETE FROM user WHERE uid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("i",$uid);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "User got deleted successfully";
		}else{
			$response["success"] = 0;
			$response["message"] = "User not found";
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