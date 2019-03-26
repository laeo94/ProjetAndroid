<?php
include 'db_connect.php';

$response = array();
if(isset($_POST['uid'])&&isset($_POST['pseudo'])){
	$uid = $_POST['uid'];
    $pseudo = $_POST['pseudo'];
    $query ="SELECT pseudo FROM user WHERE pseudo='$pseudo'";
	$res = $con->prepare($query);
	$res->execute();
	if($res->fetch()){
		 $response["success"] = 2;
         $response["message"] = "pseudo already taken"; 
         $res->close();
	}else{
        $query = "UPDATE user SET pseudo=?  WHERE uid=?";
        if($stmt = $con->prepare($query)){
			$stmt->bind_param("si",$pseudo,$uid);
			$stmt->execute();
			if($stmt->affected_rows == 1){
				$response["success"] = 1;			
				$response["message"] = "user successfully updated";	
			}else{
			$response["success"] = 0;
			$response["message"] = "user not found";
			}				
      	}else{
            $response["success"] = 0;
            $response["message"] = mysqli_error($con);
        }
    }
}else{
    $response["success"] = 0;
    $response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);

?>