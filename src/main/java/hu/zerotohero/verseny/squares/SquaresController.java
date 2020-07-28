package hu.zerotohero.verseny.squares;

import hu.zerotohero.verseny.squares.service.SquareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/squares")
@RequiredArgsConstructor
public class SquaresController {

    private final SquareService squareService;

    @GetMapping("/getNumberOfSquares")
    public Integer getNumberOfSquares(@RequestBody List<Point> points) {
        try {
            return squareService.getNumberOfSquares(points);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
