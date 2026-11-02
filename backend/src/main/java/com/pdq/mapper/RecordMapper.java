package com.pdq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pdq.entity.Record;
import org.apache.ibatis.annotations.Mapper;

// 调用记录的mapper
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
