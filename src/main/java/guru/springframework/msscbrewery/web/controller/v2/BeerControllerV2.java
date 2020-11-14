package guru.springframework.msscbrewery.web.controller.v2;

import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v2/beer")
@RestController
@RequiredArgsConstructor
public class BeerControllerV2 {

    private final BeerServiceV2 beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDtoV2> getBeer(@PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity handlePost(BeerDtoV2 beerDto) {
        BeerDtoV2 savedDto = beerService.saveNewBeer(beerDto);
        HttpHeaders headers = new HttpHeaders();
        //todo add hostname to utl
        headers.add("Location", "/api/v1/beer" +
                savedDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleUpdate(@PathVariable("beerId") UUID beerId, BeerDtoV2 beerDto) {
        beerService.updateBeer(beerId, beerDto);
    }

    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId) {
        beerService.deleteById(beerId);
    }
}
