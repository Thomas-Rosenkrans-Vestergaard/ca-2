
package com.tvestergaard.ca2.rest.generation;

import java.io.*;
import java.util.*;

/**
 * @author Dradrach
 */
public class Generator
{

    public static void main(String[] args) throws Exception
    {
        generate(
                200,
                200,
                new Distribution<Integer>().with(3, 1).with(2, 2).with(1, 3),
                new Distribution<Integer>().with(3, 3).with(2, 2).with(1, 1),
                new Distribution<Integer>().with(8, 1).with(3, 2).with(1, 3),
                new Distribution<Integer>().with(3, 3).with(3, 2).with(1, 1),
                new Distribution<Integer>().with(3, 3).with(2, 2).with(1, 1));
    }

    private static final String OUTPUT_FILE = "./src/main/resources/data.sql";
    private static final String CITIES_FILE = "/cities.sql";
    private static final Random random      = new Random();

    private static int nextInfoEntity    = 1;
    private static int nextAddressID     = 1;
    private static int nextPhoneNumberID = 1;

    /**
     * Generates database initiation sql script based on the provided parameters. The script should only be executed
     * on a clean database.
     *
     * @param nPerson                     The number of people to generate.
     * @param nCompany                    The number of companies to generate.
     * @param numberOfPersonPhoneNumbers  The statistical distribution of the number of phone numbers a person has.
     * @param numberOfCompanyPhoneNumbers The statistical distribution of the number of phone numbers a company has.
     * @param numberOfPersonAddresses     The statistical distribution of the number of addresses a person has.
     * @param numberOfCompanyAddresses    The statistical distribution of the number of addresses a company has.
     * @param numberOfPersonHobbies       The statistical distribution of the number of hobbies a person has.
     * @throws IOException
     */
    public static void generate(
            int nPerson,
            int nCompany,
            Distribution<Integer> numberOfPersonPhoneNumbers,
            Distribution<Integer> numberOfCompanyPhoneNumbers,
            Distribution<Integer> numberOfPersonAddresses,
            Distribution<Integer> numberOfCompanyAddresses,
            Distribution<Integer> numberOfPersonHobbies) throws Exception
    {


        FileWriter writer           = new FileWriter(OUTPUT_FILE);
        int        generatedCities  = generateCities(writer);
        int        generatedHobbies = generateHobbies(writer);
        generatePersons(nPerson, numberOfPersonHobbies, numberOfPersonPhoneNumbers, generatedCities, writer);
        generateCompanies(nCompany, numberOfCompanyAddresses, numberOfCompanyPhoneNumbers, generatedCities, writer);

        writer.flush();
    }

    private static int generatePersons(
            int nPerson,
            Distribution<Integer> numberOfHobbiesDistribution,
            Distribution<Integer> numberOfPhoneNumbersDistribution,
            int cities,
            FileWriter writer) throws Exception
    {
        List<String> firstNames = Arrays.asList("Bob", "Mikkel", "Alexander", "Filip", "Kristian", "Thomas",
                "Jonas", "Steven", "Maria", "Isabella", "Ida", "Rune", "Mia", "Kim", "Kirsten", "Solveig", "Alexa",
                "Kasper", "Christoffer", "Simon", "Mathias", "Oliver", "Mads", "Tobias", "Frederik", "Sebastian",
                "Lukas", "Kristoffer", "Daniel", "Rasmus", "Lucas", "Jonas", "Marcus", "Nicolai", "Silas", "Oskar",
                "Henrik", "Poul", "Bertram", "Jacob", "Sigurd", "Johanne", "Emma", "Anna", "Katrine", "Julie", "Sofie",
                "Laura", "Mathilde", "Emilie", "Mille", "Caroline", "Camilla", "Louise", "Lærke", "Lea", "Maria",
                "Amanda");

        List<String> lastNames = Arrays.asList("Bobsen", "Mikkelsen", "Alexandersen", "Filipsen", "Kristiansen",
                "Thomassen", "Jonassen", "Stevensen", "Runesen", "Kaspersen", "Christoffersen", "Simonsen",
                "Vestergaard", "Filipovic", "Behrendt", "Jensen", "Nielsen", "Hansen", "Pedersen", "Andersen",
                "Larsen", "Olsen", "Thomsen", "Poulsen", "Knudsen", "Holm", "Eriksen", "Iversen", "Jeppesen", "Jessen");

        List<String> emailEndings = Arrays.asList("hotmail", "outlook", "gmail", "yahoo", "protonmail", "zoho");
        List<String> emailDomains = Arrays.asList("dk", "com", "eu", "io");

        for (int x = 0; x < nPerson; x++) {
            int personId  = nextInfoEntity;
            int addressID = generateAddress(cities, writer);


            String firstName = random(firstNames);
            String lastName  = random(lastNames);
            String email = String.format("%s-%s-%s@%s.%s",
                    firstName.toLowerCase(),
                    lastName.toLowerCase(),
                    personId,
                    random(emailEndings),
                    random(emailDomains));
            String personSQL = String.format(
                    "INSERT INTO `person` (`ID`, `EMAIL`, `FIRSTNAME`, `LASTNAME`, `ADDRESS_ID`) " +
                            "VALUES (%d, '%s', '%s', '%s', %d);",
                    personId,
                    email,
                    firstName,
                    lastName,
                    addressID);

            writer.append(personSQL);
            writer.append('\n');

            int numberOfPhoneNumbers = numberOfPhoneNumbersDistribution.getRandom();
            for (int i = 0; i < numberOfPhoneNumbers; i++)
                generatePhoneNumber(personId, writer);

            int         numberOfHobbies = numberOfHobbiesDistribution.getRandom();
            List<Hobby> hobbies         = getNHobbies(numberOfHobbies);
            for (Hobby hobby : hobbies) {
                String hobbySQL = String.format("INSERT INTO hobby_person (hobbies_id, persons_id) VALUES (%d, %d);",
                        hobby.id,
                        personId);

                writer.append(hobbySQL);
                writer.append('\n');
            }

            nextInfoEntity++;
        }

        return nPerson;
    }

    private static int generateCompanies(
            int nCompany,
            Distribution<Integer> numberOfCompanyAddresses,
            Distribution<Integer> numberOfPhoneNumbersDistribution,
            int generatedCities,
            FileWriter writer) throws Exception
    {

        List<String> emailEndings = Arrays.asList("hotmail", "outlook", "gmail", "yahoo", "protonmail", "zoho");
        List<String> emailDomains = Arrays.asList("dk", "com", "eu", "io");

        for (int x = 0; x < nCompany; x++) {

            int companyID = nextInfoEntity;
            int addressID = generateAddress(generatedCities, writer);

            String name = "Company #" + companyID;
            String email = String.format("company-%s@%s.%s",
                    companyID,
                    random(emailEndings),
                    random(emailDomains));
            String cvr = "#" + String.valueOf(random.nextInt(99_99_99_99) + 1);
            String description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nam quis nulla. Sed vel " +
                    "lectus. Donec odio tempus molestie, porttitor ut, iaculis quis, sem.";
            int marketValue       = random.nextInt(100_000_000) + 1;
            int numberOfEmployees = random.nextInt(5000) + 1;

            String companySQL = String.format(
                    "INSERT INTO `company` (`ID`, `name`, `email`, `ADDRESS_ID`, cvr, description, marketValue, " +
                            "numberOfEmployees) VALUES (%d, '%s', '%s', %d, '%s', '%s', %d, %d);",
                    companyID,
                    name,
                    email,
                    addressID,
                    cvr,
                    description,
                    marketValue,
                    numberOfEmployees);

            writer.append(companySQL);
            writer.append('\n');

            int numberOfPhoneNumbers = numberOfPhoneNumbersDistribution.getRandom();
            for (int i = 0; i < numberOfPhoneNumbers; i++)
                generatePhoneNumber(companyID, writer);

            nextInfoEntity++;
        }

        return nCompany;
    }

    private static final List<String> streetNames = Arrays.asList("Kullevej", "Kronhjortevej", "Møllevangen", "Hjortekærsvej",
            "Ryllevej", "Østgaardsvej", "Milanovicvej", "Baunebjergvej");

    private static int generateAddress(int cities, FileWriter writer) throws Exception
    {
        String SQL = String.format(
                "INSERT INTO `address` (`id`, `information`, `street`, `city_id`) VALUES (%d, '%s', '%s', %d);",
                nextAddressID,
                random.nextInt(500) + 1,
                random(streetNames),
                random(cities));

        writer.append(SQL);
        writer.append('\n');

        return nextAddressID++;
    }

    private static final List<String> phoneNumberDescriptions = Arrays.asList("Mobil", "Hjem", "Arbejde", "Bil",
            "Feriehjem");

    private static int generatePhoneNumber(int owner, FileWriter writer) throws Exception
    {
        String phoneNumber = String.valueOf(random.nextInt(99_99_99_99));
        String description = random(phoneNumberDescriptions);

        String SQL = String.format(
                "INSERT INTO `phone` (`ID`, `DESCRIPTION`, `NUMBER`, `OWNER_ID`) VALUES (%d, '%s', '%s', %d);",
                nextPhoneNumberID,
                random(phoneNumberDescriptions),
                phoneNumber,
                owner);

        writer.append(SQL);
        writer.append('\n');

        return nextPhoneNumberID++;
    }

    private static int generateCities(FileWriter writer) throws Exception
    {
        int               numberOfCities = 0;
        String            citiesPath     = Generator.class.getResource(CITIES_FILE).getFile().replaceAll("%20", " ");
        FileInputStream   citiesStream   = new FileInputStream(citiesPath);
        InputStreamReader citiesReader   = new InputStreamReader(citiesStream, "UTF-8");
        BufferedReader    reader         = new BufferedReader(citiesReader);

        String line;
        while ((line = reader.readLine()) != null) {
            writer.append(line);
            writer.append('\n');
            numberOfCities++;
        }

        return numberOfCities;
    }

    private static final List<Hobby> hobbyList = Arrays.asList(
            Hobby.of(1, "Soccer", "Association football, more commonly known as football or soccer, is a team sport " +
                    "played between two teams of eleven players with a spherical ball. It is played by 250 million " +
                    "players in over 200 countries and dependencies, making it the world's most popular sport."),
            Hobby.of(2, "Handball", "Handball (also known as team handball, fieldball, European handball or Olympic " +
                    "handball) is a team sport in which two teams of seven players each (six outfield players " +
                    "and a goalkeeper) pass a ball using their hands with the aim of throwing it into the goal of " +
                    "the other team."),
            Hobby.of(3, "Esports", "eSports (also known as electronic sports, esports, e-sports, or " +
                    "competitive/professional [video] gaming) are a form of competition using video games."),
            Hobby.of(4, "Fishing", "Fishing is the activity of trying to catch fish. Fish are normally caught in the " +
                    "wild. Techniques for catching fish include hand gathering, spearing, netting, angling and " +
                    "trapping. “Fishing” may include catching aquatic animals other than fish, such as molluscs, " +
                    "cephalopods, crustaceans, and echinoderms."),
            Hobby.of(5, "Running", "Running is a method of terrestrial locomotion allowing humans and other animals " +
                    "to move rapidly on foot. Running is a type of gait characterized by an aerial phase in which" +
                    " all feet are above the ground (though there are exceptions[1]). This is in contrast to " +
                    "walking, where one foot is always in contact with the ground, the legs are kept mostly " +
                    "straight and the center of gravity vaults over the stance leg or legs in an inverted " +
                    "pendulum fashion."),
            Hobby.of(6, "Baking", "Baking is a method of cooking food that uses prolonged dry heat, normally in an " +
                    "oven, but also in hot ashes, or on hot stones. The most common baked item is bread but many " +
                    "other types of foods are baked."),
            Hobby.of(7, "Cooking", "Cooking or cookery is the art, technology, science and craft of preparing food " +
                    "for consumption. Cooking techniques and ingredients vary widely across the world, from " +
                    "grilling food over an open fire to using electric stoves, to baking in various types of " +
                    "ovens, reflecting unique environmental, economic, and cultural traditions and trends."),
            Hobby.of(8, "Ice skating", "Ice skating is the act of motion by wearer of the ice skates to propel the " +
                    "participant across a sheet of ice. This can be done for a variety of reasons, " +
                    "including exercise, leisure, traveling, and various sports."),
            Hobby.of(9, "Programming", "Computer programming is the process of designing and building an executable " +
                    "computer program for accomplishing a specific computing task."),
            Hobby.of(10, "Acting", "Acting is an activity in which a story is told by means of its enactment by an " +
                    "actor or actress who adopts a character—in theatre, television, film, radio, or any other " +
                    "medium that makes use of the mimetic mode."),
            Hobby.of(11, "Painting", "Painting is the practice of applying paint, pigment, color or other medium to a" +
                    "solid surface (support base). The medium is commonly applied to the base with a brush, but " +
                    "other implements, such as knives, sponges, and airbrushes, can be used. The final work is " +
                    "also called a painting."),
            Hobby.of(12, "Dance", "Dance is a performing art form consisting of purposefully selected sequences of " +
                    "human movement. This movement has aesthetic and symbolic value, and is acknowledged as dance " +
                    "by performers and observers within a particular culture."));

    private static int generateHobbies(FileWriter writer) throws Exception
    {

        for (Hobby hobby : hobbyList) {

            String SQL = String.format("INSERT INTO hobby (id, name, description) VALUES (%d, '%s', '%s');",
                    hobby.id,
                    hobby.name,
                    hobby.description);

            writer.append(SQL);
            writer.append('\n');
        }

        return hobbyList.size();
    }

    private static List<Hobby> getNHobbies(int n)
    {
        if (hobbyList.size() < n)
            throw new IllegalStateException();

        List<Hobby>  results = new ArrayList<>();
        Set<Integer> used    = new HashSet<>();
        while (n > 0) {
            int randomIndex = random.nextInt(hobbyList.size());
            if (used.contains(randomIndex))
                continue;

            results.add(hobbyList.get(randomIndex));
            n--;
        }

        return results;
    }

    private static final class Hobby
    {
        public final int    id;
        public final String name;
        public final String description;

        public Hobby(int id, String name, String description)
        {
            this.id = id;
            this.name = name;
            this.description = description.replaceAll("[^a-zA-Z0-9\\s,.\\(\\)]", "");
        }

        public static Hobby of(int id, String name, String description)
        {
            return new Hobby(id, name, description);
        }
    }

    private static <E> E random(List<E> list)
    {
        return list.get(random.nextInt(list.size()));
    }

    private static int random(int bound)
    {
        return random.nextInt(bound) + 1;
    }
}
