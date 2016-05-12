<?php
//header ("Access-Control-Allow-Origin : localhost");
require_once("connexion.php");

function getPrivilege(){
    $userID = $_POST["userID"];
    if(($userID != "") && is_numeric($userID)){
        $req = "SELECT privilege from Utilisateur where user_id=$userID";
        $result = doQuery($req);
        
        $row = mysqli_num_row($result);
        echo "{\"status\" : ";
        if($row > 0){
            echo "\"Success\","; 
            if($ligne=mysqli_fetch_object($result)){
                echo "\"privilege\" : \"";
                if($ligne->privilege != "")
                    echo $ligne->privilege;
                else
                    echo "nil";
                echo "\"";
            }
        }
        else
            echo "\"Fail\"";
        echo "}";
    }
}


function validateLogin(){
    $userName=$_POST["userName"];
    $userPass=$_POST["userPass"];
    $userID = -1;
    if(($userName != "") && ($userPass != "")){
        
        if(success){
            echo "{\"Status\" : \"Success\", \"login\" :";
            $localReq = "SELECT user_id FROM Utilisateur WHERE userName = '$userName'";
            $result = doQuery(localReq);
            $row = mysqli_num_row($result);
            if($row>0){
                $ligne = mysqli_fetch_object($result));
                $userID = $ligne->user_id;
                mysql_free_result($result);
            }
            else{
                $localReq = "INSERT INTO Utilisateur VALUEs (0, '$userName', '')";
                $result = doQuery($localReq);
                $userID = mysqli_insert_id($conn)
            }
            echo "{ \"id\" : \"$userID\" }}";
        }
        else
            returnFail("bad user or pass")
    }
    else
        returnFail("empty user or pass")

}

function getUserList(){
    $req="SELECT user_id, userName from Utilisateur";
    $result = doQuery($req);
    $row = mysqli_num_row($result);
    if($row>0){
        echo "{\"Status\" : \"Success\", ";
        echo "\"users\" : [";
        while($ligne=mysqli_fetch_object($result)){
            echo "{\"id\" : \"$ligne->user_id\", \"name\" : \"$ligne->userName\"}"
            if(--$row>0) echo ",";
        }
        echo "]}";
    }
    else
        returnFail("no result");
}

function sendMessage(){
    global $conn;
    $message=$_POST["messageContent"];
    $msgSource=$_POST["msgSource"];
    $destinataires=$_POST["destinataires"];
    $timeStamp=$_POST["timeStamp"];
    $attachement=$_POST["attachement"];
    
    if(($message != "") && ($msgSource != "") && ($timeStamp != "")){
        if(!preg_match("[0-9, ]", $destinataires) || !is_numeric($msgSource))
            returnFail("bad field values");
        else if(!validstamp(timeStamp))
            returnFail("bad time stamp");
        else{
            $destList = explode(',', trim($destinataires, " \n\r"));
            $req = "INSERT INTO MessagePacket values(0, $msgSource, ".
                   "'$destinataires', '$message', $timeStamp, '$attachement'";
            $result = doQuery(req);
            $msgId = mysqli_insert_id($conn)
            foreach($destList as $dest){
                $req2="INSERT INTO MessageQueue VALUES ($msgId, $dest, false)";
                $result2=doQuery(req2);
            }
            echo "{\"Status\" : \"Success\", ";
            echo "\"Id\" : \"$msgId\"}";
        }
    }
    else
        returnFail("missing field values");
}

function readMessages(){
    $userId=$_POST["userID"];
    $lastMsgId=$_POST["msgID"];
    
    if(!is_numeric($lastMsgId) && ($lastMsgId != ""))
        returnFail("Bad msgID");
    else if($lastMsgId == "")
        $lastMsgId = -1;
        
    if(is_numeric($userID){
        $req = "SELECT * FROM MessagePacket INNER JOIN MessageQueue ON ".
               "MessageQueue.id_msg = MessagePacket.id_msg WHERE ".
               "MessageQueue.destinataire=$userID and MessagePacket.msgID > $lastMsgId";
        $result = doQuery($req);
        $row = mysqli_num_rows(result);
        if($row>0){
            echo "{\"Status\" : \"Success\", ";
            echo "\"msgQueue\" : [";
            while ($ligne= mysqli_fetch_object($result)){
                echo "{\"msgId\" : \"$ligne->id_msg\", \"source\" : \"$ligne->source\",".
                     "\"destinataires\" : \"$ligne->destinataires\", ".
                     "\"timeStamp\" : \"$ligne->time\", ".
                     "\"message\" : \"$ligne->message\", \"attachement\" : "
                     "\"$ligne->attachement\"}"
                if(--$row > 0) echo ",";                
            }
            echo"]}";
        }
        else
            returnFail("no result");
    }
    else
        returnFail("bad UserID");
}

function sendSurvey(){
//    $idSurvey=$_POST["id_survey"];
    
}

function readSurvey(){
    $idSurvey=$_POST["surveyID"];
    
    if(is_numeric($idSurvey)){
        $req="SELECT * FROM SurveyForm WHERE id_survey=$idSurvey";
        $result = $doQuery($req);
        $row =  mysqli_num_rows($result);
        if($row>0){
            if($ligne = mysqli_fetch_object($result)){
                $questionList = $ligne->question_list;
                $limite = $ligne->dateLimite;
                mysql_free_result($result);
                echo "{\"Status\" : \"Success\", ";
                echo "\"survey\" : { \"id\" : \"$idSurvey\", ".
                     "\"dueTime\" : \"$limite\", \"questions\" : [";
                $qList = explode(',',$questionList);
                $qCount=count($qList);
                foreach($qList as $questionId){
                    $req = "SELECT * FROM SurveyQuestion WHERE id_question = $questionId";
                    $result = doQuery($req);
                    if(mysqli_num_rows($result) > 0){
                        $ligne=mysqli_fetch_object($result))
                        echo "\"id\" : \"$ligne->id_question\", \"question\" : ".
                            "\"$ligne->question\", \"type\" : \"$ligne->type\", ".
                            "\"choix\" : \"$ligne->response_list\"}";
                        if($qCount > 0) echo ",";
                    }
                    --$qCount;
                }
                echo "]}}";
            }
            else
                returnFail("no result")
        }
        else
            returnFail("no result")
    }
    else
        returnFail("bad ID")
}

function answerSurveyQuestion(){
    $idQuestion=$_POST["questionID"];
    $idSsource=$_POST["sourceID"];
    $reponseInt=$_POST["reponseInt"];
    $reponseText=$_POST["reponseText"];
    if(!is_numeric($idQuestion) || !is_numeric($qType) || !is_numeric($idSsource))
        returnFail("bad ID or bad Type");
    else(
        $req2="";
        if($reponseInt == "")
            $reponseInt = -1;
        // vérifions que la question n'as pas déjà été répondu
        $req = "SELECT * FROM SurveyResponse WHERE id_question = idQuestion AND source=idSource";
        $result = doQuery($req);
        if(mysqli_num_rows($result) > 0)
            $req2 = "UPDATE SurveyResponse set response_int=$reponseInt, ".
                    "response_text=$reponseText WHERE id_question = idQuestion ".
                    "AND source=idSource";
        else
            $req2 = "INSERT INTO SurveyResponse VALUES ($idQuestion, $idSource,".
                    " $reponseInt, '$reponseText'";
        if($req2!="")
            doQuery($req);
        else
            returnFail("");0
    }
}

function readSurveyList(){
    $lastSurveyId=$_POST["surveyID"];

    $req = "SELECT id_survey FROM SurveyForm WHERE id_survey > $lastSurveyID";
    $result=doQuery($req);
    $row=mysqli_num_rows($result);
    if($row > 0){
        echo "{\"Status\" : \"Success\", ";
        echo "\"surveyList\" : , [";
        while($ligne=mysqli_fetch_object($result)){
            echo "\"$ligne->id_survey\"";
            if(--$row>0) echo ",";
        }
        echo "]}";
    }
    else
        returnFail("no result");
}

function readSurveyResult(){
    $questionID=$_POST["questionID"];

    if(($questionID != "") && !is_numeric($questionID))
        returnFail("bad ID");
    else {
        $req = "SELECT * FROM SurveyQuestion WHERE id_question = $questionID";
        $result = doQuery($req); 
        $row=mysqli_num_rows($result);
	$ligne=mysqli_fetch_object($result)
        if($ligne != ""){
        $response_list = $ligne->response_list;
        $response_tbl = explode(',', $response_list);
        $type = $ligne->type;
        mysql_free_result($result);
        for($idx=0; $idx< size($response_tbl); $idx++)
            $response[idx] = 0;
        $req = "SELECT * FROM SurveyResponse WHERE id_question = $questionID";
        $result = doQuery($req); 
        $row=mysqli_num_rows($result);
        if($type < 2){
            if($row > 0){
                echo "{\"Status\" : \"Success\", ";
                while($ligne=mysqli_fetch_object($result))
                    $response[$ligne->response_int]++;
                echo "\"reponses\" : [";
                for($idx=0; $idx< size($response_tbl); $idx++){
                    echo "{\"$response_tbl[$idx]\" : \"$reponse[$idx]\"}";
                    if($idx < size($response_tbl)-1)
                        echo",";
                }
                echo "]}";
            }
            else{}
        }
        else
            returnFail("no result");
    }
}

//Le controleur
$action=$_POST['action'];
    switch ($action){
    case "getPrivilege" :
       getPrivilege();
    break;
    case "validateLogin" :
       validateLogin();
    break;
    case "sendMessage" :
       sendMessage();
    break;
    case "readMessages" :
       readMessages();
    break;
    case "sendSurvey" :
       sendSurvey();
    break;
    case "readSurvey" :
       readSurvey();
    break;
    case "readSurveyList" :
       readSurveyList();
    break;
    case "readSurveyResult" :
        readSurveyResult();
    break;
    case "answerSurveyQuestion":
        answerSurveyQuestion();
    break;
    default :
        returnFail("Commande inconnue : $action");
    break;
}

mysqli_close($conn);
    
?>
