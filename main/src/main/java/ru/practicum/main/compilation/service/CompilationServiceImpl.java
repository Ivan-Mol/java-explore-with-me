package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.model.CompilationMapper;
import ru.practicum.main.compilation.storage.CompilationRepository;
import ru.practicum.main.event.dto.EventMapper;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.utils.ValidationHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ValidationHelper validationHelper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        List<Event> events;
        if (compilationDto.getEvents() == null) {
            events = eventRepository.findAll();
        } else {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        List<EventShortDto> shortEvents = events.stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilationRepository.save(CompilationMapper.toCompilation(compilationDto, events)), shortEvents);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto request) {
        Compilation compilation = compilationRepository.getByIdAndCheck(compId);
        validationHelper.isTitleToLong(request.getTitle());
        compilation.setPinned(request.isPinned());
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        if (request.getEvents() != null && !request.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findAllById(request.getEvents()));
        }
        List<EventShortDto> shortEvents = compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), shortEvents);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationRepository
                .getAllByPinned(pinned, pageable).stream()
                .map(c -> CompilationMapper.toCompilationDto(c, c.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.getByIdAndCheck(compId);
        List<EventShortDto> shortEvents = compilation
                .getEvents()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilation, shortEvents);
    }
}
