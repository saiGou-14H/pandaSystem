package com.saigou.api.service;

import com.saigou.entity.ControlTimestamp;

import java.util.List;

public interface IControlTimestampService {
    List<ControlTimestamp> getByControlId(Long controlId);
    ControlTimestamp getByControlIdAndTimestamp(Long controlId, Long timestamp);
    List<ControlTimestamp> getByControlIdAndTimestampBetween(Long controlId, Long start_time, Long end_time);
    List<ControlTimestamp> listByControlIdAndTimestampByMinutesAgo(Long controlId,Integer minutes);
}
