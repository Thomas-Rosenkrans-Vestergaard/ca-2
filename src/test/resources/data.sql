INSERT INTO city (id, name, zipCode) VALUES (1, "city1", "zip1"),(2, "city2", "zip2"),(3, "city3", "zip3"),(4, "city4", "zip4"),(5, "city5", "zip5"),(6, "city6", "zip6"),(7, "city7", "zip7"),(8, "city8", "zip8"),(9, "city9", "zip9");

INSERT INTO address (id, information, street, city_id) VALUES (1, "information1", "street1", 1),(2, "information2", "street2", 2),(3, "information3", "street3", 3),(4, "information4", "street4", 4),(5, "information5", "street5", 5);

INSERT INTO person (id, email, address_id, firstName, lastName) VALUES (1, "email1@email.com", 1, "first1", "last1"),(2, "email2@email.com", 2, "first2", "last2"),(3, "email3@email.com", 3, "first3", "last3"),(4, "email4@email.com", 4, "first4", "last4"),(5, "email5@email.com", 5, "first5", "last5");

INSERT INTO company (id, email, address_id, cvr, description, marketValue, name, numberOfEmployees)VALUES (6, "company1@email.com", 1, "cvr1", "description1", 1, "name1", 1),(7, "company2@email.com", 2, "cvr2", "description2", 2, "name2", 2),(8, "company3@email.com", 3, "cvr3", "description3", 3, "name3", 3),(9, "company4@email.com", 4, "cvr4", "description4", 4, "name4", 4),(10, "company5@email.com", 5, "cvr5", "description5", 5, "name5", 5);

INSERT INTO phone (id, description, `number`, owner_id)VALUES (1, "description1", "number1", 1),(2, "description2", "number2", 2),(3, "description3", "number3", 3),(4, "description4", "number4", 4),(5, "description5", "number5", 5),(6, "description6", "number6", 6),(7, "description7", "number7", 7),(8, "description8", "number8", 8),(9, "description9", "number9", 9),(10, "description10", "number10", 10);

INSERT INTO hobby (id, `name`, description) VALUES (1, "name1", "description1"),(2, "name2", "description2"),(3, "name3", "description3"),(4, "name4", "description4"),(5, "name5", "description5");

INSERT INTO hobby_person (hobbies_id, persons_id) VALUES (1, 1),(2, 2),(3, 3),(4, 4),(4,5),(5, 5);