package com.example.mapper;

import com.example.dto.SoundPageQueryDto;
import com.example.entity.Sound;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SoundMapper {
    public Page<Sound> pageQuery(SoundPageQueryDto soundPageQueryDto) ;

    void upload(Sound sound);
@Delete(("delete from sound where id = #{id}"))
    void deleteById(Integer id);

    void update(Sound sound);
@Select("select * from sound where id = #{id}")
    Sound getById(Integer id);
}
