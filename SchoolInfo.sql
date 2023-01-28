create database SchoolInfo;
use SchoolInfo;
create table Students(
id int(5) primary key not null unique,
first_name varchar(12) not null,
last_name varchar(15) not null,
email varchar(40),
gender varchar(15),
ip_address varchar(15),
cm_height int(3),
age int(2),
has_car bool default false,
car_color varchar(12),
grade int(1),
grade_avg float(4),
identification_card int(10) not null unique
);
 
use SchoolInfo;
create table Friendships(
id int(5) primary key,
friend_id int(4) references Students(id),
other_friend_id int(4)
);

use SchoolInfo;
create view Averages as select identification_card, grade_avg from Students;