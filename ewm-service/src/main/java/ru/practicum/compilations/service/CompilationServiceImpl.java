package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PaginationSetup;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.handler.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilations.dto.CompilationMapper.mapToCompilation;
import static ru.practicum.compilations.dto.CompilationMapper.mapToCompilationDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService{

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAllCompilation(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
           return compilationRepository.findAll(new PaginationSetup(from, size, Sort.unsorted())).getContent().stream()
                    .map(CompilationMapper::mapToCompilationDto)
                    .collect(Collectors.toList());
        }

        return compilationRepository.findAllByPinned(pinned, new PaginationSetup(from, size, Sort.unsorted()))
                .getContent().stream()
                .map(CompilationMapper::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + id + " was not found"));

        return mapToCompilationDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.save(mapToCompilation(compilationDto));
        // добавить список ид.

        return mapToCompilationDto(compilation);
    }

    @Transactional
    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId +" was not found"));

        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilationByID(Long compId, NewCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId +" was not found"));
        Boolean pinned = compilationDto.getPinned();
        String title = compilationDto.getTitle();

        // обновление списка

        if (pinned != null) {
            compilation.setPinned(pinned);
        }
        if (title != null) {
            compilation.setTitle(title);
        }

        return mapToCompilationDto(compilationRepository.save(compilation));
    }
}