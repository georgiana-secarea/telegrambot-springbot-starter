INSERT INTO advice (id,language,condition,message) VALUES ('1','EN','Clear','It is time for a picnic. A clear blue sky is waiting for you.') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('2','EN','Few clouds','The sun is hiding from you momentarily, yet life can still be enjoyable.') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('3','EN','Clouds','Ergh, it could have been worse, just some clouds at the moment.') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('4','EN','Rain','Do not forget the shower gel, as you are going to take a bath today.') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('5','EN','Thunderstorm','Thunderstruck! Oh, sorry, too much AC/DC ruined my mind. In a positive way, that is. Nevermind, I can report a thunderstom as we ... speak') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('6','EN','Mist','Please drive carefully, the mist is here') ON CONFLICT DO NOTHING;
INSERT INTO advice (id,language,condition,message) VALUES ('7','EN','Snow','The snow is snowing, enjoy the flakes!') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('1','askLocation','Please choose your location first, by using the format "/loc yourLocation"') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('2','applicationDevelopers','Developers: Georgiana Secarea and Mircea Stan.
Special thanks to Vlad for his continuous support throughout this project!') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('3','mainMenu','Below you can find my main menu.') ON CONFLICT DO NOTHING;
INSERT INTO message (id,meaning,message) VALUES ('4','deny','Alright, got it. Now, what would you wish to know?') ON CONFLICT DO NOTHING;
INSERT INTO message (id,meaning,message) VALUES ('5','noLocationReceived','You must enter a location to receive the desired information!') ON CONFLICT DO NOTHING;
INSERT INTO message (id,meaning,message) VALUES ('6','processingException','Your location could not be processed. 
Please try again and make sure you enter an existing one!') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('7','userSearchedLocationsList','Your search list history is the following one: %s') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('8','noLocationSearched','You did not search any location until now. 
It is never too late, though.') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('9','savedNotificationHour','Your desired notification hour has been saved internally (%s).
Now please enter your current timezone to receive these notifications at the appropriate time, using the format "/cet yourTimeZone"') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('10','savedContactDetails','Your contact details have been saved internally (%s).
Now please enter your favorite notification hour to receive the current weather of a location that you will choose momentarily, using the format "/hour yourHour". 
Please choose a number between 0-23.') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('11','invalidNotificationHourValue','Your desired notification hour does not have a range between 0-23 (%s). 
Please enter a valid hour using the format "/hour desiredHour", between 0 and 23') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('12','invalidNotificationHourFormat','Your desired notification hour is not a numeric value (%s). 
Please enter a valid hour using the format "/hour desiredHour", between 0 and 23') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('13','conversionException','The location could not be parsed due to a conversion error, please try again later!') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('14','start','Welcome back, %s %s. What would you wish to check right now?') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('15','savedTimeZone','Your timezone has been saved internally (CET %s).\n" 
Now please enter your favorite location for which you want your notifications to be received, using the format "/fav yourLocation"') ON CONFLICT DO NOTHING;

INSERT INTO message (id,meaning,message) VALUES ('16','unknown','Uhm ... I do not quite understand what you are telling me.
Please use one (or more) of my functionalities from the menu below') ON CONFLICT DO NOTHING;