package com.abol.abol.app.repositories;

import com.abol.abol.app.models.CustomFiles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository  extends PagingAndSortingRepository<CustomFiles, Integer> {
    Optional<List<CustomFiles>> findByAuthor(String author, Pageable pageable);



}
