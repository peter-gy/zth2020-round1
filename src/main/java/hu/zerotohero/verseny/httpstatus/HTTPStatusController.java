package hu.zerotohero.verseny.httpstatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("http-status")
public class HTTPStatusController {

    @GetMapping("/getStatusDescription")
    public String getStatusDescription(@RequestParam(value = "statusCode", defaultValue = "500") Integer statusCode) {
        try {
            return HttpStatus.valueOf(statusCode).name();
        } catch (IllegalArgumentException e) {
            String reason = String.format("[%d] is not a valid status code.", statusCode);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }
    }
}
