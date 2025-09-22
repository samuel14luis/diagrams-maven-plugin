package com.example.pets.expose;

@Slf4j
public class DogManagementApiImpl implements DogManagementApi {

    @RestClient
    private ManagePetsProxy petsProxy;

    @RestClient
    private ManageFishProxy fishProxy;

    @RestClient
    private ManageBirdProxy birdProxy;

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

    @Override
    public Uni<Fish> getFish(String id) {
        log.info("Fetching fish with id: {}", id);
        return fishProxy.getFish(id)
                .onFailure().invoke(ex -> log.error("Failed to fetch or map fish with id: {}", id, ex));
    }

    @Override
    public Uni<Bird> getBird(String id) {
        log.info("Fetching bird with id: {}", id);
        return birdProxy.getBird(id)
                .onFailure().invoke(ex -> log.error("Failed to fetch or map bird with id: {}", id, ex));
    }

}