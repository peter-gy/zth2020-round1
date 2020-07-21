package hu.zerotohero.verseny.squares.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SquareServiceImpl implements SquareService {

    @Override
    public Integer getNumberOfSquares(List<Point> points) {
        if (points == null) throw new IllegalArgumentException("Point list cannot be null");
        // remove duplicates, gain O(1) access time
        Set<Point> vertices = new HashSet<>(points);
        // no square can be formed from less than 4 vertices
        if (vertices.size() < 4) return 0;

        // keeping track of the found diagonals represented by pairs of vertices
        Set<Pair<Point, Point>> toSkip = new HashSet<>();

        Integer count = 0;
        for (Point vertexA : vertices) {
            for (Point vertexC : vertices) {
                if (vertexA.equals(vertexC) || toSkip.contains(Pair.of(vertexA, vertexC))) continue;
                Pair<Point, Point> pair = getSquareVerticesByDiagonal(vertexA, vertexC);
                if (pair == null) continue;
                if (vertices.contains(pair.getFirst()) && vertices.contains(pair.getSecond())) {
                    count++;
                    toSkip.addAll(Arrays.asList(
                            Pair.of(vertexA, vertexC), // current pair
                            Pair.of(vertexC, vertexA), // current pair reversed
                            pair, // found vertices
                            Pair.of(pair.getSecond(), pair.getFirst()) // found vertices reversed
                    ));
                }
            }
        }

        return count;
    }

    private Pair<Point, Point> getSquareVerticesByDiagonal(Point vertexA, Point vertexC) {
        double midX = (vertexA.x + vertexC.x) / 2.0;
        double midY = (vertexA.y + vertexC.y) / 2.0;
        double transformX = midY - vertexA.y;
        double transformY = vertexA.x - midX;

        double vertexBX = midX - transformX;
        double vertexBY = midY - transformY;
        double vertexDX = midX + transformX;
        double vertexDY = midY + transformY;

        return Stream.of(vertexBX, vertexBY, vertexDX, vertexDY).allMatch(this::isInt)
                ? Pair.of(new Point((int)vertexBX, (int)vertexBY), new Point((int)vertexDX, (int)vertexDY))
                : null;
    }

    private boolean isInt(Double number) {
        return number % 1 == 0;
    }
}
