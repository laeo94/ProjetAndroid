<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['aid'])&&isset($_POST['title'])&&isset($_POST['description'])&&isset($_POST['device'])){
	
	$aid = $_POST['aid'];
	$title = $_POST['title'];
	$description = $_POST['description'];
	$device = $_POST['device'];

	$query = "UPDATE account SET title=?,description=?,device=? WHERE aid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("sssi",$title,$description,$device,$aid);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Account successfully updated";
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
	$response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);
?>