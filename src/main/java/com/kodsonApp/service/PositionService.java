package com.kodsonApp.service;

import com.kodsonApp.domain.Position;
import com.kodsonApp.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public Position save(Position position) {
        return positionRepository.save(position);
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public Optional<Position> findById(String id) {
        return positionRepository.findById(id);
    }

    public void deleteById(String id) {
        positionRepository.deleteById(id);
    }
}
