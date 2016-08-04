<?php
    $host = "127.0.0.1";
    $user = "root";
    $passwd = "";
    $base = "CCQFMission";
    $conn = mysqli_connect($host, $user, $passwd, $base) or die("Connexion impossible");
global $conn;

    if (!$conn) {
        echo "Error: Unable to connect to MySQL." . PHP_EOL;
        echo "Debugging errno: " . mysqli_connect_errno() . PHP_EOL;
        echo "Debugging error: " . mysqli_connect_error() . PHP_EOL;
        exit;
    }


function doQuery($query){
    global $conn;
    $result = mysqli_query($conn, $query);

    return $result;
}

function createStatus($status){
    global $conn;
    $id = mysqli_insert_id($conn) ;

    echo "{\"Status\" : " ;
    if($status > 0)
    {
        echo "\"Success\"";
        echo ", \"Id\" : \"";
        echo  $id;
        echo "\"";
    }
    else
        echo "\"Fail\"";

    echo "}";
    return $id;
}

function returnFail($msg){
    echo "{\"Status\" : \"Fail\", \"errMsg\" : \"$msg\"}";
}

?>
