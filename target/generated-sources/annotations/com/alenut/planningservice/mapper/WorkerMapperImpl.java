package com.alenut.planningservice.mapper;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-05T23:52:28+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class WorkerMapperImpl implements WorkerMapper {

    @Override
    public Worker toEntity(WorkerBaseDto dto) {
        if ( dto == null ) {
            return null;
        }

        Worker worker = new Worker();

        worker.setEmail( dto.getEmail() );
        worker.setName( dto.getName() );
        worker.setPhone( dto.getPhone() );

        return worker;
    }

    @Override
    public WorkerDto toDto(Worker entity) {
        if ( entity == null ) {
            return null;
        }

        WorkerDto workerDto = new WorkerDto();

        workerDto.setEmail( entity.getEmail() );
        workerDto.setName( entity.getName() );
        workerDto.setPhone( entity.getPhone() );

        return workerDto;
    }

    @Override
    public void updateEntityFromDto(WorkerUpdateDto dto, Worker entity) {
        if ( dto == null ) {
            return;
        }

        entity.setEmail( dto.getEmail() );
        entity.setName( dto.getName() );
        entity.setPhone( dto.getPhone() );
    }
}
