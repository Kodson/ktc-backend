package com.kodsonApp.resource;

import com.kodsonApp.domain.Position;
import com.kodsonApp.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api2/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @PostMapping
    public Position createPosition(@RequestBody Position position) {
        return positionService.save(position);
    }

    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Position> getPositionById(@PathVariable String id) {
        return positionService.findById(id);
    }

    @PutMapping("/{id}")
    public Position updatePosition(@PathVariable String id, @RequestBody Position position) {
        position.setId(id);
        return positionService.save(position);
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable String id) {
        positionService.deleteById(id);
    }
}
