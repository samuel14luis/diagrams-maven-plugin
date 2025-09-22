package com.example.pets.expose;

@Slf4j
public class DogManagementApiImpl implements DogManagementApi {

    @RestClient
    private ManagePetsProxy petsProxy;

    @Inject
    private PetToDogMapper mapper;

    @Override
    public Uni<Dog> getDog(String id) {
        log.info("Fetching dog with id: {}", id);
        return petsProxy.getPetDog(id)
                .onItem().transform(petDog -> {
                    log.info("Mapping PetDog to Dog for id: {}", id);
                    return mapper.toDog(petDog);
                })
                .onFailure().invoke(ex -> log.error("Failed to fetch or map dog with id: {}", id, ex));
    }

}