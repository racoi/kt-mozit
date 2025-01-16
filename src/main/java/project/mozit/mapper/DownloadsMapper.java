package project.mozit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.mozit.domain.Downloads;
import project.mozit.dto.DownloadsDTO;

@Mapper(componentModel = "spring")
public interface DownloadsMapper {

    @Mapping(target = "editNum", source = "editNum")
    Downloads toEntity(DownloadsDTO downloadsDTO);

    DownloadsDTO toDto(Downloads downloads);
}
