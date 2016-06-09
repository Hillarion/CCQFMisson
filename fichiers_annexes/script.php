<?php
//header ("Access-Control-Allow-Origin : localhost");
require_once("connexion.php");

/* Routine pour valider le format de la date */
function validateDate($date, $format = 'Y-m-d H:i:s')
{
    $d = DateTime::createFromFormat($format, $date);
    return $d && $d->format($format) == $date;
}

/* Routine pour valider le format de l'adresse courriel */
function validerCourriel($courriel){
    return filter_var($courriel, FILTER_VALIDATE_EMAIL);
}

/* Routine qui retourne les droits administratifs d'un usager
   reçoit : userID, un entier

    retourne, en cas de succès :

    { "Status" : "Success",
       "privilege" : "typePrivilege"
    }

    ou :  typePrivilege peur être "admin" ou "nil"      */
function getPrivilege(){
    $userID = $_POST["userID"];
    if(($userID != "") && is_numeric($userID)){
        $req = "SELECT privilege from Utilisateur where user_id=$userID";
        $result = doQuery($req);
        
        $row = mysqli_num_rows($result);
        echo "{\"Status\" : ";
        if($row > 0) {
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

/* Routine qui retourne la liste des usager.

    retourne, en cas de succès :

{"Status" : "Success", 
  "users" :
  [
     {"user_id" : "id", 
      "nom" : "NomUsager", 
      "prenom" : "PrenomUsager", 
      "compagnie" : "nomCompagnie"},
	 {}, ...
  ]
}
*/
function getUserList(){
    $userId = -1;

    $req = "SELECT user_id, nom, prenom, compagnie FROM Utilisateur";
    $result = doQuery($req);
    $row = mysqli_num_rows($result);
    if($row > 0){
        echo "{\"Status\" : \"Success\", \"users\" :[";
        while($ligne = mysqli_fetch_object($result))
        {
            echo "{\"user_id\" : \"$ligne->user_id\", ";
            echo "\"nom\" : \"$ligne->nom\", ";
            echo "\"prenom\" : \"$ligne->prenom\", ";
            echo "\"compagnie\" : \"$ligne->compagnie\"}";
            if(--$row>0) echo ",";
        }
        echo "]}";
    }
    else
        returnFail("No Data found");
}

/* Routine qui sert à inscrire un nouvel usager sur le réseau privé.
   Reçoit : le nom, prénom de l'usager ainsi que le nom de la compagnie dont l'usager est membre.
   Retourne, en cas de succès :

    {
      "Status" : "Success", 
      "Id" : "IdentifiantUniqueDeLUsager"
    }

    La routine vérifie si une fiche contenant les informations de l'usager existe dans la base de données.
        Si elle existe, la routine retourne l'identifiant de la fiche.
        Si aucune fiche correspondant n'existe, une nouvelle fiche est créée et l'identifiant correspondant est retourné.

*/
function registerUser(){
    $nom = $_POST["nom"];
    $prenom = $_POST["prenom"];
    $compagnie = $_POST["compagnie"];
    $userId = -1;
global $conn;

    if(($nom == "") || ($prenom == "") ||($compagnie == ""))
        returnFail("fields must not be empty");
    else {
        $req = "SELECT user_id FROM Utilisateur WHERE nom='$nom' AND prenom='$prenom'".
            " AND compagnie='$compagnie'";
        $result = doQuery($req);
        $row = mysqli_num_rows($result);
        if($row > 0){
            $ligne = mysqli_fetch_object($result);
            $userId = $ligne->user_id;
        }
        else{
            $req = "INSERT INTO Utilisateur VALUES (0, '$nom','$prenom', '$compagnie', '')";
            $result = doQuery($req);
            $userId = mysqli_insert_id($conn);
        }
        if($userId > 0){
            echo "{\"Status\" : ";
            echo "\"Success\"";
            echo ", \"Id\" : \"$userId\"}";
        }
        else
            returnFail("");
    }
}

/* Routine qui envois un nouveau message sur le fil de conversations de l'application.

   Retourne, en cas de succès :

    {
      "Status" : "Success", 
      "Id" : "IdentifiantDuMessage"
    }

*/
function sendMessage(){
    global $conn;
    $message=$_POST["messageContent"];
    $msgSource=$_POST["msgSource"];
    $destinataires=$_POST["destinataires"];
    $timeStamp=$_POST["timeStamp"];
    $attachement=$_POST["attachement"];
    $convId = $_POST["conversationID"];

    if(($message != "") && ($msgSource != "") && ($timeStamp != "")){
        if(!is_numeric($msgSource))
            returnFail("bad source values src = '$msgSource'");
        else if(  ($tag = preg_match('/^\d+(?:,\d+)*$/', $destinataires, $matches)) != 1){
            returnFail("bad field values dest = $destinataires ($matches[1])");
            }
        else if(!validateDate($timeStamp))
            returnFail("bad time stamp $timeStamp");
        else{
            if($convId <= 0){  // vérifier si une conversation existe avec le même groupe d'usagers
                $group_id = $destinataires . "," .$msgSource;
                $gList = explode(',', trim($group_id, " \n\r"));
                sort($gList);
                $groupList = implode(",", $gList);
                $req = "SELECT conversation_id FROM ConversationThread WHERE groupe_ids='$groupList'";
                $result = doQuery($req);
                $row = mysqli_num_rows($result);
                if($row>0){// s'il existe déjà un econversation, utiliser son ID
                    $ligne = mysqli_fetch_object($result);
                    $convId = $ligne->conversation_id;
                }
                else{ // s'il n'existe pas de conversation avec ce groupe d'usager, en créer une.
                    $req = "INSERT INTO ConversationThread values (0, '$groupList')";
                    $result = doQuery($req);
                    $convId = mysqli_insert_id($conn);
                }
            }
            $destList = explode(',', trim($destinataires, " \n\r"));
            $req = "INSERT INTO MessagePacket values(0, $msgSource, $convId,".
                   "'$message', '$timeStamp', '$attachement')";
            $result = doQuery($req);
            $msgId = mysqli_insert_id($conn);
            if($msgId > 0){
                foreach($destList as $dest){
                    $req2="INSERT INTO MessageQueue VALUES ($msgId, $dest, false)";
                    $result2=doQuery($req2);
                }
            }
            echo "{\"Status\" : \"Success\", ";
            echo "\"Id\" : \"$msgId\", ";
            echo "\"conversationID\" : \"$convId\"}";
        }
    }
    else
        returnFail("missing field values");
}

/*  Routine qui permet de retrouver les messages destinés à un usager.
    Reçoit "userID" l'identifiant de l'usager,  "msgID" l'identifiant du dernier message lu par l'usager.

   Retourne, en cas de succès :

    {
      "Status" : "Success", 
      "msgQueue" : 
        [
            {"msgId" : "IdentifiantDuMessage",

            },
	 {}, ...

        ]
    }

*/
function readMessages(){
    $userId=$_POST["userID"];
    $lastMsgId=$_POST["msgID"];
    
    if(!is_numeric($lastMsgId) && ($lastMsgId != ""))
        returnFail("Bad msgID");
    else if($lastMsgId == "")
        $lastMsgId = -1;
        
    if(is_numeric($userId)){
        $req = "SELECT * FROM MessagePacket INNER JOIN MessageQueue ON ".
               "MessageQueue.id_msg = MessagePacket.id_msg JOIN ConversationThread on ".
               "ConversationThread.conversation_id = MessagePacket.conversation_id WHERE ".
               "MessageQueue.destinataire=$userId and MessagePacket.id_msg > $lastMsgId";
//        error_log("readMessages req = $req", 0);
        $result = doQuery($req);
        $row = mysqli_num_rows($result);
        if($row>0){
            echo "{\"Status\" : \"Success\", ";
            echo "\"msgQueue\" : [";
            while ($ligne= mysqli_fetch_object($result)){
                echo "{\"msgId\" : \"$ligne->id_msg\", \"source\" : \"$ligne->source\",".
                     "\"destinataires\" : \"$ligne->groupe_ids\", ".
                     "\"conversationID\" : \"$ligne->conversation_id\",".
                     "\"timeStamp\" : \"$ligne->time\", ".
                     "\"message\" : \"$ligne->message\", \"attachement\" : ".
                     "\"$ligne->attachement\"}";
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

/* 
*  Cette méthode ne fait que créer l'entrée SurveyForm à laqulle se greffera,
*   subséquemment, les questions.
*/
function createSurveyForm(){
    $dateLimite=$_POST["dateLimite"];
    if(validateDate($dateLimite)) {
	$req = "INSERT INTO SurveyForm value(0, '', '$dateLimite')";
	createStatus(doQuery($req2));
    }
    else returnFail("Bad date format");
}

function sendSurveyQuestion(){
    $idSurvey=$_POST["id_survey"];
    $question=$_POST["question"];
    $type=$_POST["type"];
    $reponsesList=$_POST["listeReponses"];

    if(($idSurvey == "") || !is_numeric($idSurvey))
        returnFail("Bad Survey ID");
    else if($qestion == "")
        returnFail("question must not be empty");
    else if ($reponsesList == "")
        returnFail("response list must not be empty");
    else if (($type == "") || !is_numeric($type) || ($type > 2) || ($type < 0))
        returnFail("Bad QuestionType");
    else{
        $req = "INSERT INTO SurveyQuestion values(0, '$question', $type, '$reponsesList')";
	createStatus(doQuery($req));
        $msgId = mysqli_insert_id($conn);
	$req = "SELECT question_list FROM SurveyForm where id_survey = $idSurvey";
        $result = doQuery($req);
	$ligne = mysqli_fetch_object(doQuery($req));
        $qList = $ligne->question_list;
	if($qList == "")
            $qList = "$msgId";
        else
            $qList += ",$msgId";
	$req = "UPDATE question_list=$qList FROM SurveyForm where id_survey = $idSurvey";
        $result = doQuery($req);		
    }
}

function readSurvey(){
    $idSurvey=$_POST["surveyID"];
    
    if(is_numeric($idSurvey)){
        $req="SELECT * FROM SurveyForm WHERE id_survey=$idSurvey";
        $result = doQuery($req);
        $row =  mysqli_num_rows($result);
        if($row>0){
            if($ligne = mysqli_fetch_object($result)){
                $questionList = $ligne->question_list;
                $limite = $ligne->dateLimite;
//                mysql_free_result($result);
                echo "{\"Status\" : \"Success\", ";
                echo "\"survey\" : { \"id\" : \"$idSurvey\", ".
                     "\"dueTime\" : \"$limite\", \"questions\" : [";
                $qList = explode(',',$questionList);
                $qCount=count($qList);
                foreach($qList as $questionId){
                    $req = "SELECT * FROM SurveyQuestion WHERE id_question = $questionId";
                    $result = doQuery($req);
                    if(mysqli_num_rows($result) > 0){
                        $ligne = mysqli_fetch_object($result);
                        echo "{\"id\" : \"$ligne->id_question\", \"question\" : ".
                            "\"$ligne->question\", \"type\" : \"$ligne->type\", ".
                            "\"choix\" : \"$ligne->response_list\"}";
                        if($qCount > 1) echo ",";
                    }
                    --$qCount;
                }
                echo "]}}";
            }
            else
                returnFail("no result");
        }
        else
            returnFail("no result");
    }
    else
        returnFail("bad ID");
}

function answerSurveyQuestion(){
    $idQuestion=$_POST["questionID"];
    $idSource=$_POST["sourceID"];
    $reponseInt=$_POST["reponseInt"];
    $reponseText=$_POST["reponseText"];
    if(!is_numeric($idQuestion) || !is_numeric($reponseInt) || !is_numeric($idSource))
        returnFail("bad ID or bad Type");
    else{
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
                    " $reponseInt, '$reponseText')";
        if($req2!="")
            createStatus(doQuery($req2));
        else
            returnFail("");
    }
}

function readSurveyList(){
    $lastSurveyId=$_POST["surveyID"];

    $req = "SELECT id_survey FROM SurveyForm WHERE id_survey > $lastSurveyId";
    $result=doQuery($req);
    $row=mysqli_num_rows($result);
    if($row > 0){
        echo "{\"Status\" : \"Success\", ";
        echo "\"surveyList\" : [";
        while($ligne=mysqli_fetch_object($result)){
            echo "\"$ligne->id_survey\"";
            if(--$row>0) echo ",";
        }
        echo "]}";
    }
    else
        returnFail("no result");
}

function readSurveyResults(){
    $surveyID=$_POST["surveyID"];

    if(($surveyID != "") && !is_numeric($surveyID))
        returnFail("bad ID");
    else {
        $req = "SELECT question_list FROM SurveyForm WHERE id_survey = $surveyID";
//        echo "readSurveyResults req1 = $req<br/>";
        $result = doQuery($req);
        $row=mysqli_num_rows($result);
        $ligne=mysqli_fetch_object($result);
        if($ligne != ""){
            $question_list = $ligne->question_list;
            $question_tbl = explode(',', $question_list);
            mysql_free_result($result);
            echo "{\"Status\" : \"Success\", \"questions\" : [";
            for($qIdx=0; $qIdx< sizeof($question_tbl); $qIdx++){
                $req = "SELECT question, response_list FROM SurveyQuestion WHERE ".
                "id_question = $question_tbl[$qIdx]";
                $result = doQuery($req);
                $ligne=mysqli_fetch_object($result);
                if($ligne != ""){
                    $questionText = $ligne->question;
                    $responseText = $ligne->response_list;
                    $responseTable = explode(',', $responseText);
                    $response_list = $ligne->response_list;
                    $response_tbl = explode(',', $response_list);
                    $type = $ligne->type;
                    for($idx=0; $idx< sizeof($response_tbl); $idx++)
                        $response[$idx] = 0;
                    $req2 = "SELECT * FROM SurveyResponse WHERE id_question = $question_tbl[$qIdx]";
                    $result2 = doQuery($req2);
                    $row2=mysqli_num_rows($result2);
                    echo "{\"id_question\" : \"$question_tbl[$qIdx]\", ";
                    echo "\"question\" : \"$questionText\", ";
                    if($type < 2){
                        if($row2 > 0){
                            while($ligne=mysqli_fetch_object($result2))
                                $response[$ligne->response_int]++;
                            echo "\"reponses\" : [";
                            for($idx=0; $idx< sizeof($response_tbl); $idx++){
                                echo "{\"label\" : \"$response_tbl[$idx]\", \"hit\" : \"$response[$idx]\"}";
                                if($idx < sizeof($response_tbl)-1)
                                    echo", ";
                            }
                            echo "]}";
                        }
                        else{
                            echo "\"reponses\" : [";
                            for($idx=0; $idx< sizeof($response_tbl); $idx++){
                                echo "{\"label\" : \"$response_tbl[$idx]\", \"hit\" : \"0\"}";
                                if($idx < sizeof($response_tbl)-1)
                                    echo", ";
                            }
                            echo "]}";
                        }
                    }
                    if($qIdx < sizeof($question_tbl)-1)
                        echo", ";
                }
            }
            echo "]}";
        }
        else
            returnFail("no result");
    }
}

function registerEvent(){
    $destinataire = $_POST["destinataire"];
    $hDebut = $_POST["hDebut"];
    $hFin = $_POST["hFin"];
    $nom = $_POST["nom"];
    $poste = $_POST["poste"];
    $telephone = $_POST["telephone"];
    $email = $_POST["email"];
    $adresse = $_POST["adresse"];
    $batiment = $_POST["batiment"];
    $compagnie = $_POST["compagnie"];
    $id_event = -1;

    if(($destinataire == "") || ($hDebut == "") || ($hFin == "") || ($nom == "") ||
       ($poste == "") || ($email == "") || ($adresse == "")){
        return ("fields must not be empty");
    }else{
        $req = "SELECT id_event FROM event WHERE destinataire='$destinataire' AND".
        " hDebut = '$hDebut' AND hFin='$hFin' AND nom='$nom' AND poste = '$poste'".
        " AND telephone = '$telephone' AND email='$email' AND adresse='$adresse'".
        " AND batiment=$batiment";
        $result = doQuery($req);
        $row = mysqli_num_rows($result);
        if($row > 0){
            $ligne = mysqli_fetch_object($result);
            $id_event = $ligne->id_event;
        }
        else{
            mysqli_free_result($result);
            $req2 = "INSERT INTO event VALUES(0, '$destinataire', '$hDebut', '$hFin',".
            "'$compagnie', '$nom', '$poste','$telephone', '$email', '$adresse', $batiment)";
            $result2 = doQuery($req2);
//            $id_event = mysqli_insert_id($conn);
        }
        if($id_event > 0){
            echo "{\"Status\" : ";
            echo "\"Success\"";
            echo ", \"Id\" : \"$id_event\"}";
        }
        else
            returnFail("");
    }
}


function getB2BList(){
	$destinataire=$_POST["destinataire"];
	$req="SELECT * FROM event WHERE destinataire='$destinataire'";
	$result=doQuery($req);
        $row = mysqli_num_rows($result);

	if($row>0){
		echo "{\"Status\" : \"Success\",";
		echo "\"B2BList\" : [";
		while($ligne=mysqli_fetch_object($result)){
			echo "{\"destinataire\" : \"$ligne->destinataire\", ";
			echo "\"hDebut\" : \"$ligne->hDebut\", ";
			echo "\"hFin\" : \"$ligne->hFin\", ";
			echo "\"compagnie\" : \"$ligne->compagnie\", ";
			echo "\"nom\" : \"$ligne->nom\", ";
			echo "\"poste\" : \"$ligne->poste\", ";
			echo "\"telephone\" : \"$ligne->telephone\", ";
			echo "\"email\" : \"$ligne->email\", ";
			echo "\"adresse\" : \"$ligne->adresse\", ";
			echo "\"batiment\" : \"$ligne->batiment\"}";
			if(--$row>0){
				echo",";
			} 
		}
		echo"]}";
	}
	else
		returnFail("no result");
}

//Le controleur
$action=$_POST['action'];
switch ($action){
    case "getPrivilege" :
       getPrivilege();
    break;
    case "getUserList" :
       getUserList();
    break;
    case "registerUser" :
       registerUser();
    break;
    case "sendMessage" :
       sendMessage();
    break;
    case "readMessages" :
       readMessages();
    break;
    case "createSurveyForm":
        createSurveyForm();
    break;
    case "sendSurveyQuestion" :
       sendSurveyQuestion();
    break;
    case "readSurvey" :
       readSurvey();
    break;
    case "readSurveyList" :
       readSurveyList();
    break;
    case "readSurveyResults" :
        readSurveyResults();
    break;
    case "answerSurveyQuestion":
        answerSurveyQuestion();
    break;
    case "registerEvent":
        registerEvent();
    break;
    case "getB2BList":
        getB2BList();
    break;
    default :
        returnFail("Commande inconnue : $action");
    break;
}

mysqli_close($conn);
    
?>
