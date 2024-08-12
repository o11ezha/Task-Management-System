package com.o11ezha.controlpanel.mapper;

import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskModel taskToTaskModel(TaskEntity task);
    TaskEntity taskDTOToTask(TaskModel taskDTO);

    @Mapping(target = "taskId", ignore = true)
    TaskModel taskToTaskModelWithoutId(TaskEntity task);

    @Mapping(target = "taskId", ignore = true)
    TaskEntity taskDTOToTaskWithoutId(TaskModel taskModel);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "taskPerformer", ignore = true)
    TaskEntity taskDTOToTaskWithoutIdAndPerformer(TaskModel taskModel);
}
