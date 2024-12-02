package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Media;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    List<Media> fromMediaResponseList(List<MediaResponse> mediaResponseList);

    List<MediaResponse> toMediaResponseList(List<Media> media);
}
