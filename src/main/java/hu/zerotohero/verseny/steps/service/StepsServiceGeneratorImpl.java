package hu.zerotohero.verseny.steps.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Primary
@Service
public class StepsServiceGeneratorImpl implements StepsService {

    @Override
    public long getNumberOfSteps(int numberOfStairs, List<Integer> stepSizeList) {
        if (numberOfStairs < 0)
            throw new IllegalArgumentException("numberOfStairs should be non-negative");

        if (stepSizeList.stream().anyMatch(i -> i <= 0))
            throw new IllegalArgumentException("stepSizeList should not contain non-positive values");

        List<List<Integer>> lists = generateRepresentations(numberOfStairs)
                .get(numberOfStairs - 1);
        System.out.println("lists = " + lists);
        return lists.stream()
                .filter(stepSizeList::containsAll)
                .count();
    }

    private static List<List<List<Integer>>> generateRepresentations(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        List<List<List<Integer>>> representations = new ArrayList<>();

        // base cases
        representations.add(Collections.singletonList(Collections.singletonList(1)));
        if (n == 1) return representations;

        representations.add(Arrays.asList(Collections.singletonList(2), Arrays.asList(1, 1)));
        if (n == 2) return representations;

        for (int i = 3; i <= n; ++i) {
            List<List<Integer>> representation = new ArrayList<>();
            // generate 'natural' representations
            for (int j = i; j >= 1; --j) {
                List<Integer> cur = new ArrayList<>();
                cur.add(j);
                if (j != i) cur.add(i - j);
                representation.add(cur);
            }
            representations.add(representation);

            // generate 'recursive' representations dynamically
            for (int j = 1; j <= i - 2; ++j) {
                List<Integer> toReplace = representation.get(j);
                Integer anchor = toReplace.get(0);
                List<List<Integer>> prev = representations.get(anchor - 1); // using the already generated values
                for (int k = 1; k < prev.size(); ++k) {
                    ArrayList<Integer> curExtra = new ArrayList<>(toReplace);
                    curExtra.remove(0);
                    curExtra.addAll(0, prev.get(k));
                    representation.add(curExtra);
                }

            }

        }
        return representations;
    }

}
