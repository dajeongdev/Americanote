package com.coffee.americanote.coffee.service;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse.CoffeeFlavourResponse;
import com.coffee.americanote.coffee.repository.CoffeeFlavourRepository;
import com.coffee.americanote.coffee.repository.CoffeeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final CoffeeFlavourRepository coffeeFlavourRepository;

    public Map<CoffeeResponse, List<CoffeeFlavourResponse>> getAllCoffeeData() {
        Map<CoffeeResponse, List<CoffeeFlavourResponse>> allCoffeeData = new HashMap<>();
        for (Coffee coffee : coffeeRepository.findAll()) {
            List<CoffeeFlavourResponse> flavours = new ArrayList<>();
            for (CoffeeFlavour flavour : coffee.getFlavours()) {
                flavours.add(new CoffeeFlavourResponse(flavour));
            }
            allCoffeeData.put(new CoffeeResponse(coffee), flavours);
        }
        return allCoffeeData;
    }
}
