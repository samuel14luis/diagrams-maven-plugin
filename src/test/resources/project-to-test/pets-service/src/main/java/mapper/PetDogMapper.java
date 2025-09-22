package com.example.pets;

import con.example.pets.model.api.Dog;
import com.example.pets.thirdparty.dog.model.PetDog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetDogMapper {
    PetDog toPetDog(Dog dog);
    Dog toDog(PetDog petDog);
}