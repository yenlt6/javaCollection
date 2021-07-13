package com.example.learningjava.repository;

import com.example.learningjava.model.Person;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {
    private List<Person> people = new ArrayList<Person>();

    public PersonRepository() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File file;
        try {
//      file = ResourceUtils.getFile("classpath:static/person.json");
            file = ResourceUtils.getFile("classpath:static/personsmall.json");
//            file = ResourceUtils.getFile("classpath:static/person.json");
            people.addAll(mapper.readValue(file, new TypeReference<List<Person>>() {
            }));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getAll() {
        return people;
    }

    //bài tập 1:  Trả về danh sách 'developer' ở các thành phố Hanoi, Saigon, Shanghai
    public Map<String, List<Person>> getListDeveloperinCity() {
        return people
                .stream()
                .filter(p -> p.getJob().equals("developer") && (p.getCity().equals("Hanoi") || p.getCity().equals("Shanghai") || p.getCity().equals("Saigon")))
                .collect(Collectors.groupingBy(Person::getCity));
    }

    public Map<String, List<Person>> groupDevelopersByCity() {
        return people
                .stream()
                .filter(p -> p.getJob().equals("developer"))
                .collect(Collectors.groupingBy(Person::getCity));
    }


    //bài tập 2:  Tìm ra thành phố có độ tuổi trung bình của lập trình viên trẻ nhất
    public List<Map.Entry<String, Double>> ciTyHaveDeveloperYoungest() {
        Map<String, List<Person>> developersByCity = groupDevelopersByCity();
        Map<String, Double> averageDeveloperAge = developersByCity.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().collect(Collectors.averagingInt(Person::getAge))));

        return averageDeveloperAge.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(1)
                .collect(Collectors.toList());
    }

    //bài tập 3:  Liệt kê tỷ lệ nam/nữ ở từng thành phố
    public Map<String, Double> caculateRatioMaleFemale() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(p -> p.getKey(), p -> ratioofMaleFemale(p.getValue())));
    }


    //Bài tập 4. Tính mức lương trung bình của tất cả những người trên 30 tuổi
    public Double averageSalaryPeopleAbove30() {
        return people
                .stream()
                .filter(p -> p.getAge() > 30)
                .collect(Collectors.averagingInt(Person::getSalary));
    }



    //2.0 Học java collection
    public List<String> groupPeopleExample() {
        // https://www.baeldung.com/java-groupingby-collector
        System.out.println("Debuging ================");

        // Accumulate names into a List
        List<String> list = people.stream().map(Person::getName).collect(Collectors.toList());
        return list;
    }


    //2.1 Nhóm những người trong cùng thành phố lại
    public Map<String, List<Person>> groupPeopleByCity() {
        // https://www.baeldung.com/java-groupingby-collector
        System.out.println("Debuging ================");
        var variable = people.stream();
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity));
    }



    public Map<String, Long> groupByCityThenCount() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
    }

    //2.2 Nhóm các nghề nghiệp và đếm số người làm
    public Map<String, Long> groupByJobThenCount() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.counting()));
    }

    //2.3 Tìm 5 nghề có nhiều người làm nhất, đếm từ cao xuống thấp
    public Map<String, Long> fiveTopJobs() {
        Map<String, Long> jobGroupedThenCount = groupByJobThenCount();

        return jobGroupedThenCount
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));
    }

    //2.4 Tìm 5 thành phố có nhiều người trong danh sách ở nhất, đếm từ cao xuống thấp
    public Map<String, Long> fiveTopPopulationCities() {
        //Nhóm theo City sau đó đếm số People trong City
        Map<String, Long> groupByCityThenCount = people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));


        return groupByCityThenCount.entrySet()  //Chuyển kết quả sang Set<Map.Entry<K, V>>
                .stream() //Với List và Set interface thì chúng ta có stream để duyệt
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  //Sắp xếp theo Value
                .limit(5)  //Giới hạn 5 phần tử
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                )); //Collectors.toMap để nhặt ra phần tử tạo ra LinkedHashMap mới
    }

    //Tìm ra nghề nào có nhiều nhất trong một nhóm người bất kỳ
    public Map.Entry<String, Long> topJobInPeopleGroup(List<Person> peopleGroup) {
        Optional<Map.Entry<String, Long>> result = peopleGroup
                .stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.counting()))  //Nhóm theo Job
                .entrySet()
                .stream()
                .collect(Collectors.maxBy(Map.Entry.comparingByValue())); //Tìm lớn nhất

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Cannot find top job in people group");
        }

    }

    //2.5 Trong mỗi thành phố, hãy tìm ra nghề nào được làm nhiều nhất
    public Map<String, Map.Entry<String, Long>> topJobByNumerInEachCity() {
        Map<String, List<Person>> groupPeopleByCity = groupPeopleByCity();

        return groupPeopleByCity.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> topJobInPeopleGroup(entry.getValue()))
    /*
    Chuyển đổi Map<City, List<Person>> sang 
    Map<City, <Job, JobCount>>
    */
                );
    }

    //2.6 Ứng với một nghề, hãy tính mức lương trung bình
    public Map<String, Double> averageSalaryByJob() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.averagingInt(Person::getSalary)));
    }

    //2.7 Năm thành phố có mức lương trung bình cao nhất
    public List<Map.Entry<String, Double>> fiveCitiesHasTopAverageSalary() {
        Map<String, List<Person>> groupPeopleByCity = groupPeopleByCity();

        Map<String, Double> cityAverageSalary = groupPeopleByCity.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .collect(Collectors.averagingInt(Person::getSalary))));


        return cityAverageSalary.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
    }

    //2.8 Năm thành phố có mức lương trung bình của developer cao nhất
    public List<Map.Entry<String, Double>> fiveCitiesHasTopSalaryForDeveloper() {

        Map<String, List<Person>> groupDevelopersByCity = people
                .stream()
                .filter(p -> p.getJob().equals("developer"))
                .collect(Collectors.groupingBy(Person::getCity));

        Map<String, Double> cityAverageSalary = groupDevelopersByCity.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .collect(Collectors.averagingInt(Person::getSalary))));


        return cityAverageSalary.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
    }

    //2.9 Tuổi trung bình từng nghề nghiệp
    public Map<String, Double> averageAgePerJob() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.averagingInt(Person::getAge)));
    }

    //2.10 Tuổi trung bình ở từng thành phố
    public Map<String, Double> averageAgePerCity() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.averagingInt(Person::getAge)));
    }


    //2.11 Tuổi cao nhất trong mỗi thành phố
    public List<Map.Entry<String, Integer>> maxAgePerCity() {
        return people
                .stream()
                .collect(Collectors.groupingBy(Person::getCity, //nhóm người theo thành phố
                        Collectors.maxBy(Comparator.comparing(Person::getAge)))) //tìm ra người cao tuổi nhất
                .values() //lấy danh sách những người cao tuổi trong từng thành phố
                .stream() //duyệt
                .map(person -> Map.entry(person.get().getCity(), person.get().getAge())) //Tạo Map.entry<Tên thành phố, tuổi>
                .collect(Collectors.toList()); //chuyển nó sang thành List
    }

    //2.12 tỷ lệ nam / nữ trong toàn bộ dữ liệu
    public Double maleFemaleRatio() {
        Long maleCount = people
                .stream()
                .filter(p -> p.getGender().equals("Male"))
                .collect(Collectors.counting());

        return (double) maleCount / (double) (people.size() - maleCount);
    }

    //2.13 tỷ lệ nam / nữ trong một list people
    public Double ratioofMaleFemale(List<Person> people) {
        Long maleCount = people
                .stream()
                .filter(p -> p.getGender().equals("Male"))
                .collect(Collectors.counting());

        return (double) maleCount / (double) (people.size() - maleCount);
    }
}
