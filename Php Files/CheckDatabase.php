<?php
   $con=mysqli_connect("localhost", "root", "", "fyp");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
   
   $codeReceived = $_GET['code'];
   $searchByName = $_GET['search'];
   
   if ($searchByName == 0) {
	   $result = mysqli_query($con, "SELECT * FROM rooms WHERE Room = '$codeReceived';");
	   if(mysqli_num_rows($result) == 0){
		   $result = mysqli_query($con, "SELECT * FROM module WHERE Module_Code = '$codeReceived';");
		   while($row=mysqli_fetch_assoc($result)) {
				$resultset[] = $row;
				echo $row["Module_Code"];
				echo ";";
				echo $row["Module_Title"];
			}
	   }
	   else {
			while($row=mysqli_fetch_assoc($result)) {
				$resultset[] = $row;
				echo $row["Room"];
				echo ";";
				echo $row["Building"];
				echo ";";
				echo $row["Description"];
			}
	   }
   }
   else {
	     $result = mysqli_query($con, "SELECT * FROM rooms WHERE Description LIKE '%$codeReceived%' or Room LIKE '%$codeReceived%';");
	   if(mysqli_num_rows($result) == 0){
		   $result = mysqli_query($con, "SELECT * FROM module WHERE Module_Code LIKE '$codeReceived';");
		   while($row=mysqli_fetch_assoc($result)) {
				$resultset[] = $row;
				echo $row["Module_Code"];
				echo ";";
				echo $row["Module_Title"];
			}
	   }
	   else {
			while($row=mysqli_fetch_assoc($result)) {
				$resultset[] = $row;
				echo $row["Room"];
				echo ";";
				echo $row["Building"];
				echo ";";
				echo $row["Description"];
			}
	   }
   }
	

   mysqli_close($con);
?>