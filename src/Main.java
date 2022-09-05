import java.util.*;
import java.util.stream.Collectors;

/**
 * 1. Find the number of people under 18;
 * 2. get a list of last names of men from 18 to 27 years old;
 * 3. get a list sorted by last name of potentially able-bodied people with higher education in the sample
 * (from 18 to 60 years old for women and up to 65 for men)
 */
public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(random.nextInt(names.size())),
                    families.get(random.nextInt(families.size())),
                    random.nextInt(100),
                    Sex.values()[random.nextInt(Sex.values().length)],
                    Education.values()[random.nextInt(Education.values().length)])
            );
        }

        long personMinor = persons.parallelStream()
                .filter(person -> person.getAge() < 18)
                .count();

        List<String> conscripts = persons.parallelStream()
                .filter(person -> person.getSex() == Sex.MAN)
                .filter(person -> person.getAge() < 28)
                .filter(person -> person.getAge() > 17)
                .map(Person::getFamily)
                .collect(Collectors.toList());

        List<Person> ableBodied = persons.parallelStream()
                .filter(person -> person.getEducation() == Education.HIGHER)
                .filter(person -> person.getAge() > 17)
                .filter(person -> person.getAge() < 65)
                .filter(person -> !(person.getAge() > 59 && person.getSex() == Sex.WOMAN))
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList());

        System.out.println("Количество несовершеннолетних: " + personMinor);
        System.out.println("Количество призывников: " + conscripts.size());
        System.out.println("Количество людей трудоспособного возраста: " + ableBodied.size()  );
    }

}
