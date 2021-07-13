package com.example.learningjava;

import com.example.learningjava.repository.PersonRepository;

public class Main {
    public static void main(String[] arr){
        PersonRepository personRepository = new PersonRepository();
//        personRepository.groupPeopleByCity();
//        System.out.println(personRepository.groupPeopleByCity());
//        System.out.println(personRepository.groupByCityThenCount());
//        System.out.println(personRepository.groupByJobThenCount());
        System.out.println(personRepository.fiveTopJobs());
    }
}
