package com.hciot.service;

import com.hciot.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Page<Type> listType(Pageable pageable);

    List<Type> listType();
}
