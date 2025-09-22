package com.example.pets.expose;

@Slf4j
public class FoodManagementApiImpl implements FoodManagementApi {

    @RestClient
    private ManageVegetarianFoodProxy vegetarianFoodProxy;

    @RestClient
    private ManageTraditionalFoodProxy traditionalFoodProxy;

    @Override
    public Multi<Food> getFood(String type) {
        log.info("Fetching food of type: {}", type);
        Multi<Food> vegetarianFood = vegetarianFoodProxy.getVegetarianFood(type)
                .onFailure().invoke(ex -> log.error("Failed to fetch vegetarian food of type: {}", type, ex));

        Multi<Food> traditionalFood = traditionalFoodProxy.getTraditionalFood(type)
                .onFailure().invoke(ex -> log.error("Failed to fetch traditional food of type: {}", type, ex));

        return vegetarianFood.merge(traditionalFood);
    }

}