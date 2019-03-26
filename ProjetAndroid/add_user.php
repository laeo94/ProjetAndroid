<?php
include 'db_connect.php';
$response = array();
if(isset($_POST['pseudo'])&&isset($_POST['mdp'])){
    $pseudo = $_POST['pseudo'];
    $mdp = PASSWORD_HASH($_POST["mdp"], PASSWORD_DEFAULT);
    $query ="SELECT pseudo FROM user WHERE pseudo='$pseudo'";
	$res = $con->prepare($query);
	$res->execute();
	if($res->fetch()){
		 $response["success"] = 2;
         $response["message"] = "pseudo already taken"; 
         $res->close();
	}else{
		$query = "INSERT INTO user(pseudo,mdp) VALUES (?,?)";
        if($stmt = $con->prepare($query)){
        	$stmt->bind_param("ss",$pseudo,$mdp);
            $stmt->execute();
            if($stmt->affected_rows == 1){
                $response["success"] = 1;           
                $response["message"] = "user has been had";            
            }else{
                $response["success"] = 0;
                $response["message"] = "Error while adding compte";
            }                   
        }else{
            $response["success"] = 0;
            $response["message"] = mysqli_error($con);
        }
    }
}else{
    $response["success"] = 0;
    $response["message"] = "missing pseudo or/and mdp parameters";
}
//Displaying JSON response
echo json_encode($response);

?>