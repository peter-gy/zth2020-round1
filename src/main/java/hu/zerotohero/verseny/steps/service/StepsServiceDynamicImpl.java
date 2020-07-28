package hu.zerotohero.verseny.steps.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepsServiceDynamicImpl implements StepsService {

    @Override
    public int getNumberOfSteps(int numberOfStairs, List<Integer> stepSizeList) {

        if (numberOfStairs < 0)
            throw new IllegalArgumentException("numberOfStairs should be non-negative");

        if (stepSizeList.stream().anyMatch(i -> i <= 0))
            throw new IllegalArgumentException("stepSizeList should not contain non-positive values");

        // Stepping 1 is always a possibility
        if (!stepSizeList.contains(1)) stepSizeList.add(1);

        int[] count = new int[numberOfStairs + 1];
        count[0] = 1;

        for (int i = 1; i <= numberOfStairs; i++)
            for (Integer stepSize : stepSizeList)
                if (i >= stepSize)
                    count[i] += count[i - stepSize];

        return count[numberOfStairs];
    }

}
