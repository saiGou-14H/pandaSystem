package com.saigou.api.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.api.mapper.IControlTimestampMapper;
import com.saigou.api.service.IControlTimestampService;
import com.saigou.entity.ControlTimestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class ControlTimestampServiceImpl extends ServiceImpl<IControlTimestampMapper, ControlTimestamp> implements IControlTimestampService {
    private final IControlTimestampMapper controlTimestampMapper;
    @Override
    public List<ControlTimestamp> getByControlId(Long controlId) {
        return controlTimestampMapper.selectList(new QueryWrapper<ControlTimestamp>().eq("control_id",controlId));
    }

    @Override
    public ControlTimestamp getByControlIdAndTimestamp(Long controlId, Long timestamp) {
        return controlTimestampMapper.selectOne(new QueryWrapper<ControlTimestamp>().eq("control_id",controlId).eq("timestamp",timestamp));
    }

    @Override
    public List<ControlTimestamp> getByControlIdAndTimestampBetween(Long controlId, Long start_time, Long end_time) {
        if(start_time!=null&&end_time!=null){
            return controlTimestampMapper.selectList(new QueryWrapper<ControlTimestamp>().eq("control_id",controlId).between("timestamp",start_time,end_time));
        }
        return null;
    }

    @Override
    public List<ControlTimestamp> listByControlIdAndTimestampByMinutesAgo(Long controlId,Integer minutes) {
        Long now_time = System.currentTimeMillis();
        Long start_time = now_time-minutes*60*1000;
        System.out.println("start_time:"+start_time);
        System.out.println("now_time:"+now_time);
        if(start_time!=null&&now_time!=null){
            return controlTimestampMapper.selectList(new QueryWrapper<ControlTimestamp>().eq("control_id",controlId).between("timestamp",start_time,now_time));
        }
        return null;
    }
}
