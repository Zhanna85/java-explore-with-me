package ru.practicum.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.model.Compilation;

@UtilityClass
public class CompilationMapper {

    public static Compilation mapToCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());

        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        // добавить dto.setEvents();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.getPinned());
        dto.setTitle(compilation.getTitle());
        return dto;
    }
}