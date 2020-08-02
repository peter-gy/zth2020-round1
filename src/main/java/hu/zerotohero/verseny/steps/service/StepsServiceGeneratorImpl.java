package hu.zerotohero.verseny.steps.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
public class StepsServiceGeneratorImpl implements StepsService {

    @Override
    public long getNumberOfSteps(int numberOfStairs, List<Integer> stepSizeList) {
        if (numberOfStairs < 0)
            throw new IllegalArgumentException("numberOfStairs should be non-negative");

        if (stepSizeList.stream().anyMatch(i -> i <= 0))
            throw new IllegalArgumentException("stepSizeList should not contain non-positive values");

        if (numberOfStairs > 20)
            throw new IllegalArgumentException("This input would kill my Heroku dyno... Don't try to hurt her please🥺");

        // remove duplicates
        stepSizeList = new ArrayList<>(new HashSet<>(stepSizeList));
        if (stepSizeList.isEmpty()) return 1;
        // add 1 if not present
        if (!stepSizeList.contains(1)) stepSizeList.add(1);

        List<List<Integer>> variations = generateRepresentations(numberOfStairs)
                .get(numberOfStairs - 1)
                .stream()
                .filter(stepSizeList::containsAll)
                .collect(Collectors.toList());

        //variations.forEach(System.out::println);
        return variations.size();
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
