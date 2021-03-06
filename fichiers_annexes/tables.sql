//drop database CCQFMission;
//create database CCQFMission;

use CCQFMission;
drop table MessageQueue;
drop table MessagePacket;
drop table ConversationThread;
drop table Utilisateur;
drop table SurveyForm;
drop table SurveyResponse;
drop table SurveyQuestion;
drop table event;

create table Utilisateur (
    user_id int NOT NULL AUTO_INCREMENT,
    nom text NOT NULL,
    prenom text NOT NULL,
    compagnie text NOT NULL,
    privilege  enum ('', 'admin'),
    PRIMARY KEY(user_id)
);

create table ConversationThread(
    conversation_id int NOT NULL AUTO_INCREMENT,
    groupe_ids text,
    PRIMARY KEY(conversation_id)
);

create table MessagePacket(
    id_msg int NOT NULL AUTO_INCREMENT,
    source int NOT NULL,
    conversation_id int NOT NULL,
    message text,
    time DATETIME NOT NULL,
    attachement text,
    PRIMARY KEY(id_msg),
    FOREIGN KEY(conversation_id) REFERENCES ConversationThread (conversation_id)
);

create table MessageQueue(
    id_msg int NOT NULL,
    destinataire int NOT NULL,
    FOREIGN KEY(id_msg) REFERENCES MessagePacket (id_msg),
    FOREIGN KEY(destinataire) REFERENCES Utilisateur (user_id)
);

create table SurveyQuestion(
    id_question int NOT NULL AUTO_INCREMENT,
    question text, 
    type int,
    response_list text,
    PRIMARY KEY(id_question)
);

create table SurveyResponse(
    id_question int NOT NULL,
    source int NOT NULL, 
    response_int int,
    response_text text,
    FOREIGN KEY(id_question) REFERENCES SurveyQuestion (id_question)
);

create table SurveyForm(
    id_survey int NOT NULL AUTO_INCREMENT,
    question_list text,
    dateLimite  DATETIME,
    PRIMARY KEY(id_survey)
);


create table event (
    id_event int NOT NULL AUTO_INCREMENT,
    destinataire text NOT NULL,
    hDebut text,
    hFin text,
    compagnie text,
    nom text,
    poste text,
    telephone text,
    email text,
    adresse text,
    batiment boolean,
    PRIMARY KEY(id_event)
);

insert into Utilisateur values(0, 'Racicot', 'Vanessa', 'ccqf', 'admin');


//insert into Utilisateur values(0, 'St-Pierre', 'Thierry', 'AEC', '');
//insert into Utilisateur values(0, 'Bleau', 'Jonathan', 'G', '');


