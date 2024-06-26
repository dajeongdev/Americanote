package com.coffee.americanote.coffee.service;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.coffee.repository.CoffeeRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public List<CoffeeResponse> getAllCoffeeData() {
        List<CoffeeResponse> allCoffeeData = new ArrayList<>();
        for (Coffee coffee : coffeeRepository.findAll()) {
            List<CoffeeFlavour> flavours = new ArrayList<>(coffee.getFlavours());
            allCoffeeData.add(new CoffeeResponse(coffee, flavours));
        }
        return allCoffeeData;
    }
}
