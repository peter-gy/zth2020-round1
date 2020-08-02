package hu.zerotohero.verseny.steps.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StepsServiceSetImpl implements StepsService {

    @Override
    public long getNumberOfSteps(int numberOfStairs, List<Long> stepSizeList) {
        if (numberOfStairs < 0)
            throw new IllegalArgumentException("numberOfStairs should be non-negative");

        if (stepSizeList.stream().anyMatch(i -> i <= 0))
            throw new IllegalArgumentException("stepSizeList should not contain non-positive values");

        if (stepSizeList.size() == 1 && numberOfStairs % stepSizeList.get(0) != 0) return 0;
        if (stepSizeList.size() == 1 && numberOfStairs == stepSizeList.get(0)) return 1;

        List<List<List<Integer>>> representations = new ArrayList<>();
        // base cases
        representations.add(Arrays.asList(Arrays.asList(1)));
        representations.add(Arrays.asList(Arrays.asList(2), Arrays.asList(1, 1)));


        for (int i = 3; i <= numberOfStairs; ++i) {
            List<List<Integer>> representation = new ArrayList<>();
            for (int j = i; j >= 1; --j) {
                List<Integer> cur = new ArrayList<>();
                cur.add(j);
                if (j != i) cur.add(i - j);
                representation.add(cur);
            }
            representations.add(representation);

            for (int j = 1; j <= i - 2; ++j) {
                List<Integer> toReplace = representation.get(j);
                Integer anchor = toReplace.get(0);
                List<List<Integer>> prev = representations.get(anchor - 1);
                List<List<Integer>> prevSub = prev.subList(1, prev.size());
                for (List<Integer> list : prevSub) {
                    ArrayList<Integer> curExtra = new ArrayList<>(toReplace);
                    curExtra.remove(0);
                    curExtra.addAll(0, list);
                    representation.add(curExtra);
                }
            }


        }
        representations.forEach(System.out::println);
        return 0;
    }

    public static void main(String[] args) {
        StepsService stepsService = new StepsServiceSetImpl();
        stepsService.getNumberOfSteps(5, Arrays.asList(1L, 2L, 3L, 4L, 5L));
    }
}
