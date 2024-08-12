package com.o11ezha.controlpanel.mapper;

import com.o11ezha.controlpanel.DTO.model.CommentModel;
import com.o11ezha.controlpanel.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentModel commentToCommentModel(CommentEntity comment);
    CommentEntity commentDTOToComment(CommentModel commentModel);

    @Mapping(target = "commentId", ignore = true)
    CommentModel commentToCommentModelWithoutId(CommentEntity task);

    @Mapping(target = "commentId", ignore = true)
    CommentEntity commentDTOToCommentWithoutId(CommentModel taskModel);
}
