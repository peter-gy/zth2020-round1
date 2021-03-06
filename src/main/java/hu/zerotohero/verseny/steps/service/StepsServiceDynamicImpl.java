package hu.zerotohero.verseny.steps.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepsServiceDynamicImpl implements StepsService {

    @Override
    public long getNumberOfSteps(int numberOfStairs, List<Integer> stepSizeList) {

        if (numberOfStairs < 0)
            throw new IllegalArgumentException("numberOfStairs should be non-negative");

        if (stepSizeList.stream().anyMatch(i -> i <= 0))
            throw new IllegalArgumentException("stepSizeList should not contain non-positive values");

        if (stepSizeList.size() == 1 && numberOfStairs % stepSizeList.get(0) != 0) return 0;
        if (stepSizeList.size() == 1 && numberOfStairs == stepSizeList.get(0)) return 1;

        // Stepping 1 is always a possibility
        if (!stepSizeList.contains(1)) stepSizeList.add(1);

        long[] count = new long[numberOfStairs + 1];
        count[0] = 1;

        for (long i = 1; i <= numberOfStairs; i++)
            for (long stepSize : stepSizeList)
                if (i >= stepSize)
                    count[(int) i] += count[(int) (i - stepSize)];

        return count[numberOfStairs];
    }

}
