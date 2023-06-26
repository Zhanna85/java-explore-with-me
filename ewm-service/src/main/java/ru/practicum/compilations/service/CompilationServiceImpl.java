package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAllCompilation(Boolean pinned, Integer from, Integer size) {

        Page<Compilation> compilations = compilationRepository.findAll()
        return null;
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        return null;
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        return null;
    }

    @Override
    public void deleteCompilationById(Long compId) {

    }

    @Override
    public CompilationDto updateCompilationByID(Long compId, NewCompilationDto compilationDto) {
        return null;
    }
}