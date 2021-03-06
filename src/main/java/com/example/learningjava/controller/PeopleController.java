package com.example.learningjava.controller;

import com.example.learningjava.model.Person;
import com.example.learningjava.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class PeopleController {
  @Autowired private PersonRepository personRepo;
  
  @GetMapping("example")
  public List<String> exampleJavaCollection() {
    return personRepo.groupPeopleExample();

  }

  @GetMapping("groupbycity")
  public ResponseEntity<Map<String, List<Person>>> groupByCity() {
    return ResponseEntity.ok().body(personRepo.groupPeopleByCity());
  }

  @GetMapping("devhanoisaigoshanghai")
  public ResponseEntity<Map<String, List<Person>>> developerInCity() {
    return ResponseEntity.ok().body(personRepo.getListDeveloperinCity());
  }

  @GetMapping("youngestdevs")
  public List<Map.Entry<String, Double>> youngestDevs() {
    return personRepo.ciTyHaveDeveloperYoungest();
  }

 @GetMapping("malefemaleratio")
  public Map<String, Double> ratioMaleFamale() {
    return personRepo.caculateRatioMaleFemale();
  }


 @GetMapping("avgsalarypeopleabove30")
  public ResponseEntity<Double> avgSalaryPeopleAbove30() {
    return ResponseEntity.ok().body(personRepo.averageSalaryPeopleAbove30());
  }



  @GetMapping("groupbycitythencount")
  public ResponseEntity<Map<String, Long>> groupByCityThenCount() {
    return ResponseEntity.ok().body(personRepo.groupByCityThenCount());
  }

  @GetMapping("groupbyjobthencount")
  public ResponseEntity<Map<String, Long>> groupByJobThenCount() {
    return ResponseEntity.ok().body(personRepo.groupByJobThenCount());
  }

  @GetMapping("fivetopjobs")
  public ResponseEntity<Map<String, Long>> fiveTopJobs(){
    return ResponseEntity.ok().body(personRepo.fiveTopJobs());
  }

  @GetMapping("fivetoppopcities")
  public ResponseEntity<Map<String, Long>> fiveTopPopulationCities() {
    return ResponseEntity.ok().body(personRepo.fiveTopPopulationCities());
  }

  //2.5 Trong m???i th??nh ph???, h??y t??m ra ngh??? n??o ???????c l??m nhi???u nh???t
  @GetMapping("topjobcity")
  public ResponseEntity<Map<String, Map.Entry<String, Long>>> topJobByNumerInEachCity() {
    return ResponseEntity.ok().body(personRepo.topJobByNumerInEachCity());
  }

  //2.6 ???ng v???i m???t ngh???, h??y t??nh m???c l????ng trung b??nh
  @GetMapping("averagesalary")
  public ResponseEntity<Map<String, Double>> averageSalaryByJob() {
    return ResponseEntity.ok().body(personRepo.averageSalaryByJob());
  }

  //2.7 N??m th??nh ph??? c?? m???c l????ng trung b??nh cao nh???t
  @GetMapping("5citistopsalary")
  public ResponseEntity<List<Map.Entry<String,Double>>> fiveCitiesHasTopAverageSalary() {
    return ResponseEntity.ok().body(personRepo.fiveCitiesHasTopAverageSalary());
  }

  //2.8 N??m th??nh ph??? c?? m???c l????ng trung b??nh c???a developer cao nh???t
  @GetMapping("5citistopdevsalary")
  public ResponseEntity<List<Map.Entry<String,Double>>> fiveCitiesHasTopSalaryForDeveloper() {
    return ResponseEntity.ok().body(personRepo.fiveCitiesHasTopSalaryForDeveloper());
  }

  //2.9 Tu???i trung b??nh t???ng ngh??? nghi???p
  @GetMapping("averageageperjob")
  public ResponseEntity<Map<String, Double>> averageAgePerJob() {
    return ResponseEntity.ok().body(personRepo.averageAgePerJob());
  }

  //2.11 Tu???i cao nh???t ??? m???i th??nh ph???
  @GetMapping("maxagepercity")
  public ResponseEntity<List<Map.Entry<String, Integer>>> maxAgePerCity() {
    return ResponseEntity.ok().body(personRepo.maxAgePerCity());
  }

//  //2.12 t??? l??? nam / n??? trong to??n b??? d??? li???u
//  @GetMapping("malefemaleratio")
//  public ResponseEntity<Double> maleFemaleRatio() {
//    return ResponseEntity.ok().body(personRepo.maleFemaleRatio());
//  }
}
