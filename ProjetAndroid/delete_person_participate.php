<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['idto']) && isset ($_POST['aid'])){
	$personId = $_POST['idto'];
	$aid= $_POST['aid'];
	$query= "DELETE FROM depense WHERE aid=? AND (idto =? OR idfrom=?)";
			$stmt= $con->prepare($query);
			$stmt->bind_param("iii",$aid,$personId,$personId);
			$stmt->execute();
			$stmt->close();
	$query = "DELETE FROM participations WHERE uid=? AND aid=?";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("ii",$personId,$aid);
		$stmt->execute();
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "person got deleted successfully from this participations";
		}else{
			$response["success"] = 0;
			$response["message"] = "person/account not found";
		}					
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
 
}else{
	$response["success"] = 0;
	$response["message"] = "missing parameter ";
}
echo json_encode($response);
?>