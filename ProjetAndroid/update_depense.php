<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['did'])){
	$did = $_POST['did'];
	$statut="paid";
	$query = "UPDATE depense SET statut=? WHERE did=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("si",$statut,$did);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Depense successfully updated";
		}else{
			$response["success"] = 0;
			$response["message"] = "Depense not found";
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