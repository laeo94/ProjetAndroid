<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['aid']) && isset($_POST['idfrom']) && isset($_POST['idto']) && isset($_POST['somme']) &&isset ($_POST['detail'])){
	$aid =$_POST['aid'];
	$idfrom = $_POST['idfrom'];
	$idto = $_POST['idto'];
	$somme = $_POST['somme'];
	$detail = $_POST['detail'];
	$statut="in_progress";

	$query = "INSERT INTO depense(aid,idfrom,idto,somme,statut,detail) VALUES (?,?,?,?,?,?)";
	
	if($stmt = $con->prepare($query)){
	
		$stmt->bind_param("iiiiss",$aid,$idfrom,$idto,$somme,$statut,$detail);
		$stmt->execute();
		
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Depense has been had";			
		}else{
			$response["success"] = 0;
			$response["message"] = "Error while adding depense";
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