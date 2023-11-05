package com.alenut.planningservice.mapper;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.model.entity.Worker;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-05T23:19:35+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class ShiftMapperImpl implements ShiftMapper {

    @Override
    public Shift toEntity(ShiftBaseDto dto) {
        if ( dto == null ) {
            return null;
        }

        Shift shift = new Shift();

        shift.setType( dto.getType() );
        shift.setWorkDay( dto.getWorkDay() );

        return shift;
    }

    @Override
    public ShiftDto toDto(Shift entity) {
        if ( entity == null ) {
            return null;
        }

        ShiftDto shiftDto = new ShiftDto();

        shiftDto.setWorkerId( entityWorkerId( entity ) );
        shiftDto.setType( entity.getType() );
        shiftDto.setWorkDay( entity.getWorkDay() );
        shiftDto.setId( entity.getId() );

        return shiftDto;
    }

    @Override
    public void updateEntityFromDto(ShiftUpdateDto dto, Shift entity) {
        if ( dto == null ) {
            return;
        }

        entity.setType( dto.getType() );
        entity.setWorkDay( dto.getWorkDay() );
    }

    private Long entityWorkerId(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        Worker worker = shift.getWorker();
        if ( worker == null ) {
            return null;
        }
        Long id = worker.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
