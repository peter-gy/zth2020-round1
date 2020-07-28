package hu.zerotohero.verseny.steps;

import hu.zerotohero.verseny.steps.service.StepsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("number-of-steps")
@RequiredArgsConstructor
public class StepsController {

    private final StepsService stepsService;

    @GetMapping("/getNumberOfSteps")
    public Integer getNumberOfSteps(@RequestParam(value = "numberOfStair") Integer numberOfStair,
                                    @RequestParam(value = "stepSizeList") List<Integer> stepSizeList) {
        return stepsService.getNumberOfSteps(numberOfStair, stepSizeList);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        String message = String.format("%s: %s", ex.getClass().getSimpleName(), ex.getLocalizedMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
