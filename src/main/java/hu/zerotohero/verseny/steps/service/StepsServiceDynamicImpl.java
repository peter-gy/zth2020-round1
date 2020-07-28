package hu.zerotohero.verseny.steps.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepsServiceDynamicImpl implements StepsService {

    @Override
    public int getNumberOfSteps(int numberOfStairs, List<Integer> stepSizeList) {
        int[] count = new int[numberOfStairs + 1];
        count[0] = 1;

        for (int i = 1; i <= numberOfStairs; i++)
            for (Integer stepSize : stepSizeList)
                if (i >= stepSize)
                    count[i] += count[i - stepSize];

        return count[numberOfStairs];
    }

}
