<?php
include 'db_connect.php';
$depenseArray = array();
$response = array();
$result = array();
if(isset($_GET['aid']) && isset($_GET['idfrom']) && isset($_GET['idto'])){
	$aid = $_GET['aid'];
	$idto = $_GET['idto'];
	$idfrom = $_GET['idfrom'];
	$query = "SELECT did,idto,somme, detail , statut FROM depense WHERE aid =? AND ((idto=? OR idto=?) AND (idfrom=? OR idfrom =?))";
	if($stmt = $con->prepare($query)){
		$stmt->bind_param("iiiii",$aid,$idto,$idfrom,$idfrom,$idto);
		$stmt->execute();
		$stmt->bind_result($did,$idto,$somme,$detail,$statut);
		while($stmt->fetch()){
			$depenseArray["did"] = $did;
			$depenseArray["idto"] = $idto;
			$depenseArray["somme"] = $somme;
			$depenseArray["detail"] = $detail;
			$depenseArray["statut"] = $statut;
			$result[]=$depenseArray;
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
	$response["message"] = "missing parameter";
}
//Display JSON response
echo json_encode($response);
?>
