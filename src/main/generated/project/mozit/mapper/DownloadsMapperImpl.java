package project.mozit.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.mozit.domain.Downloads;
import project.mozit.dto.DownloadsDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T15:44:51+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class DownloadsMapperImpl implements DownloadsMapper {

    @Override
    public Downloads toEntity(DownloadsDTO downloadsDTO) {
        if ( downloadsDTO == null ) {
            return null;
        }

        Downloads downloads = new Downloads();

        downloads.setEditNum( downloadsDTO.getEditNum() );
        downloads.setDownloadNum( downloadsDTO.getDownloadNum() );
        downloads.setPersonalList( downloadsDTO.getPersonalList() );
        downloads.setHazardousList( downloadsDTO.getHazardousList() );
        downloads.setFaceMosaic( downloadsDTO.getFaceMosaic() );

        return downloads;
    }

    @Override
    public DownloadsDTO toDto(Downloads downloads) {
        if ( downloads == null ) {
            return null;
        }

        DownloadsDTO downloadsDTO = new DownloadsDTO();

        downloadsDTO.setDownloadNum( downloads.getDownloadNum() );
        downloadsDTO.setFaceMosaic( downloads.getFaceMosaic() );
        downloadsDTO.setHazardousList( downloads.getHazardousList() );
        downloadsDTO.setPersonalList( downloads.getPersonalList() );
        downloadsDTO.setEditNum( downloads.getEditNum() );

        return downloadsDTO;
    }
}
