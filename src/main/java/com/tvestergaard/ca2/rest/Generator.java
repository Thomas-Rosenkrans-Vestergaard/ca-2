
package com.tvestergaard.ca2.rest;

import java.io.*;
import java.util.Random;

/**
 * @author Dradrach
 */
public class Generator
{

    public static void main(String[] args) throws IOException
    {
        generate(1000);
    }

    public static void generate(int samples) throws IOException
    {
        String            citiesPath   = Generator.class.getResource("/cities.sql").getFile().replaceAll("%20", " ");
        FileInputStream   citiesStream = new FileInputStream(citiesPath);
        InputStreamReader citiesReader = new InputStreamReader(citiesStream, "UTF-8");
        BufferedReader    reader       = new BufferedReader(citiesReader);

        File       outputPath = new File("./data.sql");
        FileWriter writer     = new FileWriter(outputPath);

        // Write cities information
        String line;
        while ((line = reader.readLine()) != null)
            writer.append(line);

        //Person
        String[] firstNames        = {"Bob", "Mikkel", "Alexander", "Filip", "Kristian", "Thomas", "Jonas", "Steven", "Maria", "Isabella", "Ida", "Rune", "Mia", "Kim", "Kirsten", "Solveig", "Alexa", "Kasper", "Christoffer", "Simon"};
        String[] lastNames         = {"Bobsen", "Mikkelsen", "Alexandersen", "Filipsen", "Kristiansen", "Thomassen", "Jonassen", "Stevensen", "Runesen", "Kaspersen", "Christoffersen", "Simonsen", "Vestergaard", "Filipovic", "Behrendt"};
        String[] emailEndings      = {"hotmail", "outlook", "gmail", "yahoo", "protonmail", "zoho"};
        String[] emailCountryCodes = {"dk", "com", "eu"};

        //Phone
        String[] phoneDescriptions = {"home", "work", "mobile"};

        //Address
        String[] streetNames           = {"Kullevej", "Kronhjortevej", "Møllevangen", "Hjortekærsvej", "Ryllevej", "Østgaardsvej", "Milanovicvej"};
        String[] additionalStreetInfos = {"stuen", "1. tv", "1. th", "2. tv", "2. th"};

        //Hobby
        String[] hobbies           = {"Fodbold", "Håndbold", "Esport", "Fiskeri", "Løb"};
        String[] hobbyDescriptions = {"Boldspil med fødder", "Boldspil med hænder", "Computerspil", "Fiske", "Populærsport"};


        Random r = new Random();


        for (int i = 0; i < hobbies.length; i++) {
            //Hobby
            String hobbyDescription = hobbyDescriptions[i];
            String hobby            = hobbies[i];

            String hobbySQL = "INSERT INTO `hobby` (`DESCRIPTION`, `NAME`) VALUES ('" + hobbyDescription + "', '" + hobby + "');";
            writer.append(hobbySQL + "\n");
        }

        for (int i = 0; i < samples; i++) {

            //Address
            String streetName = streetNames[r.nextInt(streetNames.length)];


            int cityInfoID = r.nextInt(1352); //Hardcoded from populate.sql info

            String addressSQL;

            //Only adds additional info to address every 5th time
            if (samples % 5 == 0) {
                String additionalStreetInfo = additionalStreetInfos[r.nextInt(additionalStreetInfos.length)];

                addressSQL =
                        "INSERT INTO `address` (`INFORMATION`, `STREET`, `CITY_ID`) VALUES ('" + additionalStreetInfo +
                                "', '" + streetName + "', " + cityInfoID + ");";
            } else {
                addressSQL = "INSERT INTO `address` (`STREET`, `CITY_ID`) VALUES ('" + streetName + "', " + cityInfoID + ");";
            }
            writer.append(addressSQL + "\n");

            //Person
            String firstName = firstNames[r.nextInt(firstNames.length)];
            String lastName  = lastNames[r.nextInt(lastNames.length)];

            String emailEnding      = emailEndings[r.nextInt(emailEndings.length)];
            String emailCountryCode = emailCountryCodes[r.nextInt(emailCountryCodes.length)];

            String email = firstName + "@" + emailEnding + "." + emailCountryCode;

            int addressID = i + 1;
            int    personID         = i + 1;

            String personSQL =
                    "INSERT INTO `person` (`ID`, `EMAIL`, `FIRSTNAME`, `LASTNAME`, `ADDRESS_ID`) VALUES (" + personID +
                            ",'" + email +
                            "', '" + firstName + "', '" + lastName + "', " + addressID + ");";
            writer.append(personSQL + "\n");

            //Phone
            String phoneDescription = phoneDescriptions[r.nextInt(phoneDescriptions.length)];
            int    phoneNumber      = r.nextInt(99999999);

            String phoneSQL =
                    "INSERT INTO `phone` (`DESCRIPTION`, `NUMBER`, `OWNER_ID`) VALUES ('" + phoneDescription + "', '" + phoneNumber + "', " + personID + ");";
            writer.append(phoneSQL + "\n");

            //Person-Hobby join table:
            int    hobbyID          = r.nextInt(hobbies.length) + 1;
            String personHobbiesSQL =
                    "INSERT INTO `hobby_person` (`hobbies_ID`, `persons_ID`) VALUES (" + hobbyID + ", " + personID + ");";
            writer.append(personHobbiesSQL + "\n");
        }

        writer.flush();
    }
}
